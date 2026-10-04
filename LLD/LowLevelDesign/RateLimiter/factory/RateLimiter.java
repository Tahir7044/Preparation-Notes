package LowLevelDesign.RateLimiter.factory;

import LowLevelDesign.RateLimiter.enums.RateLimiterType;
import LowLevelDesign.RateLimiter.model.RateLimiterConfig;

public abstract class RateLimiter {
    public final RateLimiterConfig config;
    public final RateLimiterType type;

    public RateLimiter(RateLimiterConfig config, RateLimiterType type) {
        this.config = config;
        this.type = type;
    }

    public abstract boolean requestAllow(String userId);
}
