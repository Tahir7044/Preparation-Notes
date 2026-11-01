package LowLevelDesign.LRUWithExpiration.Strategy.TTLStrategy;

public interface TTLStrategy {
    long onCreate(long now, long ttl);
    long onRead(long now, long ttl, long currentExpiry);
    long onUpdate(long now, long ttl, long currentExpiry);
}
