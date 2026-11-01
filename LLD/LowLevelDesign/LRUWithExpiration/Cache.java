package LowLevelDesign.LRUWithExpiration;

import LowLevelDesign.LRUWithExpiration.Strategy.EvictionStrategy.EvictionStrategy;
import LowLevelDesign.LRUWithExpiration.Strategy.TTLStrategy.TTLStrategy;
import LowLevelDesign.LRUWithExpiration.model.Clock;
import LowLevelDesign.LRUWithExpiration.model.Entry;
import LowLevelDesign.LRUWithExpiration.model.ExpiryCleaner;
import LowLevelDesign.LRUWithExpiration.model.SystemClock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Cache<K, V> {
    public EvictionStrategy<K> evictionStrategy;
    public TTLStrategy ttlStrategy;
    private final int capacity;
    private final long ttl;
    private final Clock clock;
    private final ReentrantLock lock;
    private final Map<K, Entry<V>> mappedByKey;
    private final ExpiryCleaner cleaner;

    public Cache(int capacity, long ttlSeconds, TTLStrategy ttlStrategy, EvictionStrategy<K> evictionStrategy){
        this(capacity, ttlSeconds, ttlStrategy, evictionStrategy, new SystemClock());
    }

    public Cache(int capacity, long ttlSeconds, TTLStrategy ttlStrategy, EvictionStrategy<K> evictionStrategy, Clock clock){

        if(capacity<=0) {
            throw new IllegalArgumentException("invalid capacity: " + capacity);
        }

        if(ttlSeconds<=0) {
            throw new IllegalArgumentException("invalid TTL: " + ttlSeconds);
        }

        this.capacity = capacity;
        lock = new ReentrantLock();
        mappedByKey = new HashMap<>();
        this.ttl = TimeUnit.SECONDS.toMillis(ttlSeconds);
        this.evictionStrategy = evictionStrategy;
        this.cleaner = new ExpiryCleaner(this,  Math.max(100L, ttl/2));
        this.ttlStrategy = ttlStrategy;
        this.clock = clock;
        this.cleaner.start();
    }

    public void put(K key, V val){
        lock.lock();
        try{
            long now = clock.now();
            Entry<V> entry = mappedByKey.get(key);
            if(entry!=null){
                entry.value = val;
                entry.expiry = ttlStrategy.onUpdate(now, ttl, entry.expiry);
            } else {
                if(mappedByKey.size()>=capacity){
                    K victim = evictionStrategy.evict();
                    if(victim==null) throw new IllegalStateException("no node found to evict");
                    mappedByKey.remove(victim);
                }
                mappedByKey.put(key, new Entry<V>(val, ttlStrategy.onCreate(now,ttl)));
            }
            evictionStrategy.keyAccessed(key);
        } finally {
            lock.unlock();
        }
    }

    public V get(K key){
        lock.lock();
        try{
            long now = clock.now();
            Entry<V> entry = mappedByKey.get(key);
            if(entry==null){
                return null;
            }
            if(entry.expiry<=now){
                evictionStrategy.keyRemoved(key);
                mappedByKey.remove(key);
                return null;
            }
            entry.expiry = ttlStrategy.onRead(now, ttl, entry.expiry);
            evictionStrategy.keyAccessed(key);
            return entry.value;
        } finally {
            lock.unlock();
        }
    }

    public boolean delete(K key){
        lock.lock();
        try{
            Entry<V> entry = mappedByKey.get(key);
            if(entry!=null){
                evictionStrategy.keyRemoved(key);
                mappedByKey.remove(key);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public void removeExpired(){

        lock.lock();
        try{
            long now = clock.now();
            List<K> expired = new ArrayList<>();
            for(Map.Entry<K, Entry<V>> entry : mappedByKey.entrySet()){
                if(entry.getValue().expiry<=now){
                    expired.add(entry.getKey());
                }
            }

            for(K key: expired){
                evictionStrategy.keyRemoved(key);
                mappedByKey.remove(key);
            }

        } finally{
            lock.unlock();
        }
    }

    public void shutDown(){
        this.cleaner.stop();
    }
}
