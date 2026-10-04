package LowLevelDesign.RateLimiter.factory;

import LowLevelDesign.RateLimiter.enums.RateLimiterType;
import LowLevelDesign.RateLimiter.model.RateLimiterConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class TokenBucketRateLimiter extends RateLimiter {

    private final Map<String, Integer> tokens = new ConcurrentHashMap<String, Integer>();
    private final Map<String, Long> lastRefillTime = new ConcurrentHashMap<>();




    public TokenBucketRateLimiter(RateLimiterConfig config){
        super(config, RateLimiterType.TOKEN_BUCKET);
    }

    @Override
    public boolean requestAllow(String userId) {
        AtomicBoolean allowed = new AtomicBoolean(false);
        long now = System.currentTimeMillis();
        tokens.compute(userId, (id, availableToken) -> {
            int currentTokens = refillTokens(id, availableToken, now);
            if(currentTokens>0){
                allowed.set(true);
                return currentTokens -1;
            }
            return currentTokens;
        });
        return allowed.get();
    }

    private int refillTokens(String userId,  Integer availableToken, long now){
        int currentTokens = (availableToken != null) ? availableToken : config.getMaximumRequest();
        double refillRate = (double) config.getWindowInSecond()/config.getMaximumRequest();
        lastRefillTime.putIfAbsent(userId, now);
        long lastRefill = lastRefillTime.get(userId);

        long elapsedSeconds = (now - lastRefill)/1000;
        int refillTokens = (int) (elapsedSeconds/refillRate);
        currentTokens = Math.min(config.getMaximumRequest(), refillTokens+currentTokens);

        if(refillTokens>0){
            lastRefillTime.put(userId, now);
        }
        return currentTokens;
    }
}
