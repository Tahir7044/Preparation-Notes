package LowLevelDesign.LRUWithExpiration.model;

public class Entry<V> {
    public V value;
    public long expiry;

    public Entry(V val, long expiry){
        this.value = val;
        this.expiry = expiry;
    }
}
