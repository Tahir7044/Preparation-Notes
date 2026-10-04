package LowLevelDesign.RateLimiter.factory;

import LowLevelDesign.RateLimiter.enums.RateLimiterType;
import LowLevelDesign.RateLimiter.model.RateLimiterConfig;

public class RateLimiterFactory {

    public static RateLimiter createRateLimiter(RateLimiterType type, RateLimiterConfig config){
        return switch (type) {
            case FIXED_WINDOW -> new FixWindowRateLimiter(config);
            case TOKEN_BUCKET -> new TokenBucketRateLimiter(config);
            case SLIDING_WINDOW_LOG -> new SlidingWindowLogRateLimiter(config);
            default -> throw new IllegalArgumentException("Unsupported rate limiter type: " + type);
        };
    }
}
