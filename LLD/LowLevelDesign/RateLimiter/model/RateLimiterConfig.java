package LowLevelDesign.RateLimiter.model;

public class RateLimiterConfig {
    private final Integer maximumRequest;
    private final Integer windowInSecond;

    public RateLimiterConfig(Integer maximumRequest, Integer windowInSecond) {
        this.maximumRequest = maximumRequest;
        this.windowInSecond = windowInSecond;
    }

    public Integer getMaximumRequest() {
        return maximumRequest;
    }

    public Integer getWindowInSecond() {
        return windowInSecond;
    }
}
