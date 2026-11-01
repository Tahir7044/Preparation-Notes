package LowLevelDesign.LRUWithExpiration.model;

import LowLevelDesign.LRUWithExpiration.Cache;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExpiryCleaner {
    private final Cache<?, ?> cache;
    private final long intervalMs;
    private final ScheduledExecutorService executorService;

    public ExpiryCleaner(Cache<?, ?> cache, long intervalMs){
        this.intervalMs = intervalMs;
        this.cache = cache;
        this.executorService = Executors.newSingleThreadScheduledExecutor(r -> {
                                Thread t = new Thread(r, "cache-expiry-cleaner");
                                t.setDaemon(true);
                                return t;
                            });
    }

    public void start(){
            executorService.scheduleAtFixedRate(()->{
                try{ cache.removeExpired();}
                catch (Exception e){e.printStackTrace();}
            }, intervalMs, intervalMs, TimeUnit.MILLISECONDS);
    }
    public void stop(){
        executorService.shutdown();
    }
}
