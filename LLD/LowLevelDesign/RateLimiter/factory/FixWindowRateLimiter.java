package LowLevelDesign.RateLimiter.factory;

import LowLevelDesign.RateLimiter.enums.RateLimiterType;
import LowLevelDesign.RateLimiter.model.RateLimiterConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class FixWindowRateLimiter extends RateLimiter {

    private final Map<String, Integer> requestCount = new ConcurrentHashMap<>();
    private final Map<String, Long> windowStart = new ConcurrentHashMap<>();

    public FixWindowRateLimiter(RateLimiterConfig config){
        super(config, RateLimiterType.FIXED_WINDOW);
    }

    @Override
    public boolean requestAllow(String userId) {
        AtomicBoolean allowed = new AtomicBoolean(false);
        long currentReqWindow = System.currentTimeMillis() / 1000 / config.getWindowInSecond();

        requestCount.compute(userId, (id, count) -> {
            long lastReqWindow = windowStart.getOrDefault(id, currentReqWindow);

            if (lastReqWindow != currentReqWindow) {
                // window expired -> reset counter and window of last req
                windowStart.put(id, currentReqWindow);
                allowed.set(true);
                return 1; // first request in new window
            }

            if (count == null) count = 0;

            if (count < config.getMaximumRequest()) {
                allowed.set(true);
                return count + 1; // increment count
            }

            return count; // remain same
        });

        return allowed.get();
    }
}
