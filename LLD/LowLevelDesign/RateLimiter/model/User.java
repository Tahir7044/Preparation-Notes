package LowLevelDesign.RateLimiter.model;

import LowLevelDesign.RateLimiter.enums.TierPlan;

public class User {
    private final String userId;
    private final TierPlan planTier;

    public User(String userId, TierPlan planTier) {
        this.userId = userId;
        this.planTier = planTier;
    }

    public String getUserId() {
        return userId;
    }

    public TierPlan getPlanTier() {
        return planTier;
    }
}
