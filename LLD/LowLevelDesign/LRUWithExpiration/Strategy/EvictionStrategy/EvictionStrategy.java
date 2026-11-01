package LowLevelDesign.LRUWithExpiration.Strategy.EvictionStrategy;

public interface EvictionStrategy<K> {
    public void keyAccessed(K key);
    public void keyRemoved(K key);
    public K evict();
}
