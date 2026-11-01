package LowLevelDesign.LRUWithExpiration.Strategy.TTLStrategy;

public class AbsoluteTTLStrategy implements TTLStrategy {

    @Override
    public long onCreate(long now, long ttl) {
        return now + ttl;
    }

    @Override
    public long onUpdate(long now, long ttl, long currentExpiry) {
        return now + ttl;
    }

    @Override
    public long onRead(long now, long ttl, long currentExpiry) {
        return currentExpiry;
    }
}
