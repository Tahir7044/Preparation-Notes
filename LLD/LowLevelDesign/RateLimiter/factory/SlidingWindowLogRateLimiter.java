package LowLevelDesign.RateLimiter.factory;

import LowLevelDesign.RateLimiter.enums.RateLimiterType;
import LowLevelDesign.RateLimiter.model.RateLimiterConfig;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class SlidingWindowLogRateLimiter extends RateLimiter{

    private final Map<String, Queue<Long>> requestLog = new ConcurrentHashMap<>();

    public SlidingWindowLogRateLimiter(RateLimiterConfig config){
        super(config, RateLimiterType.SLIDING_WINDOW);
    }
    @Override
    public boolean requestAllow(String userId) {
        AtomicBoolean allowed = new AtomicBoolean(false);
        long now = System.currentTimeMillis() / 1000;

        requestLog.compute(userId, (id, log) -> {
            if (log == null) log = new ArrayDeque<>();

            while (!log.isEmpty() && (now - log.peek()) >= config.getWindowInSecond()) {
                log.poll();
            }

            if (log.size() < config.getMaximumRequest()) {
                log.add(now);      // record this request
                allowed.set(true);     // mark allowed
            }

            return log;
        });

        return allowed.get();
    }
}
