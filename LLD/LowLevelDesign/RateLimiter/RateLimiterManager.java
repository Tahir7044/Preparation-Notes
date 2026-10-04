package LowLevelDesign.RateLimiter;

import LowLevelDesign.RateLimiter.enums.RateLimiterType;
import LowLevelDesign.RateLimiter.enums.TierPlan;
import LowLevelDesign.RateLimiter.factory.RateLimiter;
import LowLevelDesign.RateLimiter.factory.RateLimiterFactory;
import LowLevelDesign.RateLimiter.model.RateLimiterConfig;
import LowLevelDesign.RateLimiter.model.User;

import java.util.HashMap;
import java.util.Map;

public class RateLimiterManager {
    private final Map<TierPlan, RateLimiter> ratelimiters = new HashMap<TierPlan, RateLimiter>();
    public RateLimiterManager(){
        ratelimiters.put(
                TierPlan.FREE,
                RateLimiterFactory.createRateLimiter(RateLimiterType.FIXED_WINDOW, new RateLimiterConfig(10, 60))
        );

        ratelimiters.put(
                TierPlan.PREMIUM,
                RateLimiterFactory.createRateLimiter(RateLimiterType.SLIDING_WINDOW_LOG, new RateLimiterConfig(100, 60))
        );
    }

    public boolean allowRequest(User user){
        RateLimiter rateLimiter = ratelimiters.get(user.getPlanTier());
        if(rateLimiter==null){
            throw new IllegalArgumentException("invalid user");
        }
        return rateLimiter.requestAllow(user.getUserId());
    }

}
