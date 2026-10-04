package LowLevelDesign.RateLimiter;

/*

Requirements:

1 - Rate limit users based on userId and TierPlan (FREE / PREMIUM)
2 - Support multiple rate limiting algorithms (pluggable via Factory pattern)
    - Fixed Window: counts requests per discrete time window, resets at window boundary
    - Token Bucket: refills tokens over time, each request consumes one token
    - Sliding Window Log: keeps timestamp log per request, evicts expired entries
3 - Thread-safe — concurrent users can call allowRequest() simultaneously
    - ConcurrentHashMap + compute() for atomic per-user state updates

Error handling:
    - Reject request (return false) if user exceeds threshold limit

Out of scope:
    - HTTP layer / 429 response
    - Distributed rate limiting (Redis, etc.)
    - Dynamic config changes at runtime

Classes:

User
    - userId: String
    - planTier: TierPlan

RateLimiterConfig
    - maximumRequest: int
    - windowInSecond: int

RateLimiter (abstract)
    - config: RateLimiterConfig
    - type: RateLimiterType
    + requestAllow(userId): boolean

TokenBucketRateLimiter extends RateLimiter
    - tokens: ConcurrentHashMap<userId, Integer>
    - lastRefillTime: ConcurrentHashMap<userId, Long>
    + requestAllow(userId): boolean         ← compute() + CAS refill
    - refillTokens(userId, availableToken, now): int

FixWindowRateLimiter extends RateLimiter
    - requestCount: ConcurrentHashMap<userId, Integer>
    - windowStart: ConcurrentHashMap<userId, Long>
    + requestAllow(userId): boolean         ← compute() with window reset

SlidingWindowLogRateLimiter extends RateLimiter
    - requestLog: ConcurrentHashMap<userId, Queue<Long>>
    + requestAllow(userId): boolean         ← compute() with timestamp eviction

RateLimiterFactory
    + createRateLimiter(type, config): RateLimiter

RateLimiterManager
    - rateLimiters: Map<TierPlan, RateLimiter>
    + allowRequest(user): boolean

Design Patterns:
    - Factory        → RateLimiterFactory (creates algorithm-specific limiter)
    - Strategy       → RateLimiter subclasses (pluggable algorithms)

Enums:
    TierPlan         { FREE, PREMIUM }
    RateLimiterType  { TOKEN_BUCKET, FIXED_WINDOW, SLIDING_WINDOW_LOG, ... }

*/

import LowLevelDesign.RateLimiter.enums.TierPlan;
import LowLevelDesign.RateLimiter.model.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws Exception {

        RateLimiterManager manager = new RateLimiterManager();

        User freeUser    = new User("user1", TierPlan.FREE);
        User premiumUser = new User("user2", TierPlan.PREMIUM);

        // ===== Scenario 1: FREE user — 10 requests allowed in 60s window =====
        System.out.println("===== Scenario 1: FREE user within limit =====");
        for (int i = 1; i <= 10; i++) {
            boolean allowed = manager.allowRequest(freeUser);
            System.out.println("Request " + i + ": " + (allowed ? "ALLOWED" : "REJECTED"));
        }

        // ===== Scenario 2: FREE user — 11th request rejected =====
        System.out.println("\n===== Scenario 2: FREE user exceeds limit =====");
        boolean allowed = manager.allowRequest(freeUser);
        System.out.println("Request 11: " + (allowed ? "ALLOWED" : "REJECTED"));

        // ===== Scenario 3: PREMIUM user — higher limit =====
        System.out.println("\n===== Scenario 3: PREMIUM user within limit =====");
        for (int i = 1; i <= 15; i++) {
            boolean ok = manager.allowRequest(premiumUser);
            System.out.println("Request " + i + ": " + (ok ? "ALLOWED" : "REJECTED"));
        }

        // ===== Scenario 4: Concurrent requests from FREE user =====
        System.out.println("\n===== Scenario 4: Concurrent requests =====");
        User freeUser2 = new User("user3", TierPlan.FREE);
        ExecutorService executor = Executors.newFixedThreadPool(4);
        int[] allowedCount = {0};
        int[] rejectedCount = {0};

        for (int i = 0; i < 15; i++) {
            executor.submit(() -> {
                boolean result = manager.allowRequest(freeUser2);
                synchronized (allowedCount) {
                    if (result) allowedCount[0]++;
                    else rejectedCount[0]++;
                }
            });
        }
        executor.shutdown();
        while (!executor.isTerminated()) { Thread.sleep(10); }

        System.out.println("Allowed: " + allowedCount[0] + ", Rejected: " + rejectedCount[0]);
        System.out.println("Expected: 10 allowed, 5 rejected (FREE tier = 10 req/60s)");
    }
}
