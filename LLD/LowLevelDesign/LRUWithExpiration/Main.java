package LowLevelDesign.LRUWithExpiration;

import LowLevelDesign.LRUWithExpiration.Strategy.EvictionStrategy.LRUEvictStrategy;
import LowLevelDesign.LRUWithExpiration.Strategy.TTLStrategy.AbsoluteTTLStrategy;
import LowLevelDesign.LRUWithExpiration.Strategy.TTLStrategy.SlidingTTLStrategy;
import LowLevelDesign.LRUWithExpiration.model.Clock;
import LowLevelDesign.LRUWithExpiration.model.SystemClock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

// Functional Requirements:
// 1. put(key, value) — insert or update; the entry becomes most-recently-used and its expiry is (re)computed
// 2. get(key) — returns the value, or null on miss / expired; a hit becomes most-recently-used
// 3. delete(key) — removes the entry, returns true if it existed
// 4. When the cache is full, a put of a NEW key evicts the least-recently-used entry
// 5. Expired entries are removed automatically (lazily on get + by a background sweeper)
// 6. Expiry behaviour is selectable: Absolute TTL (get does not extend) or Sliding TTL (get extends)

// Non-Functional Requirements:
// - Thread-safe: one ReentrantLock guards the map, the eviction strategy and the sweeper
// - O(1) get / put / delete: HashMap + doubly linked list (sentinel head / tail)
// - Expiry is pluggable via TTLStrategy (Strategy pattern)
// - Eviction is pluggable via EvictionStrategy (Strategy pattern) — LFU can be dropped in later
// - Time is injected via Clock so expiry can be tested without sleeping

// Out of scope:
// - Per-key TTL (a global TTL keeps expiry order == LRU order for sliding TTL)
// - Distributed cache / persistence
// - Hit / miss statistics, eviction listeners
// - LFU eviction (interface is ready, implementation is not)
// - Null keys / null values

/*

Classes

- Node<K>                                ← linked list node, owned by the eviction strategy
    - key, prev, next

- Entry<V>                               ← stored in the cache map
    - value, expiry: long (epoch ms)

- DoublyLinkedList<K>
    - head, tail                         ← sentinels, no null checks needed
    + addToHead(node), remove(node), moveToHead(node), removeTail(): Node, isEmpty()

- Cache<K, V>
    - mappedByKey: Map<K, Entry<V>>
    - capacity, ttl (ms)
    - ttlStrategy: TTLStrategy
    - evictionStrategy: EvictionStrategy<K>
    - clock: Clock
    - lock: ReentrantLock
    - cleaner: ExpiryCleaner
    + put(key, value)
    + get(key): V
    + delete(key): boolean
    + removeExpired()                    ← called by the sweeper, under the lock
    + shutDown()

- ExpiryCleaner
    - scheduler: ScheduledExecutorService  (daemon thread)
    + start(), stop()                    ← runs cache.removeExpired() every max(100ms, ttl/2)

Strategy Pattern:
    TTLStrategy → onCreate(now, ttl), onUpdate(now, ttl, currentExpiry), onRead(now, ttl, currentExpiry): long
        - AbsoluteTTLStrategy   ← onRead keeps currentExpiry
        - SlidingTTLStrategy    ← onRead returns now + ttl
    EvictionStrategy<K> → keyAccessed(key), keyRemoved(key), evict(): K
        - LRUEvictStrategy      ← DoublyLinkedList + Map<K, Node<K>>
        - LFUEvictStrategy      (out of scope)

Clock → now(): long
    - SystemClock               ← System.currentTimeMillis()
    - FakeClock (test only)     ← advance(ms)

*/

public class Main {

    static class FakeClock implements Clock {
        private volatile long now = 0;
        public long now() { return now; }
        public void advance(long ms) { now += ms; }
    }

    static int failures = 0;

    static void check(String name, boolean condition) {
        System.out.println((condition ? "PASS  " : "FAIL  ") + name);
        if (!condition) failures++;
    }

    static final long SEC = 1000L;

    public static void main(String[] args) throws Exception {
        testLruEviction();
        testUpdateDoesNotEvict();
        testAbsoluteTtl();
        testSlidingTtl();
        testPutResetsTtl();
        testDelete();
        testExpiredEntryFreesCapacity();
        testSweeperRemovesWithoutGet();
        testConcurrentAccess();
        testValidation();

        System.out.println();
        System.out.println(failures == 0 ? "ALL TESTS PASSED" : failures + " TEST(S) FAILED");
        System.exit(failures == 0 ? 0 : 1);
    }

    // Scenario 1: capacity 2 — put a, put b, get a, put c  →  b is the LRU and gets evicted
    static void testLruEviction() {
        System.out.println("-- LRU eviction");
        FakeClock clock = new FakeClock();
        Cache<String, Integer> cache = new Cache<>(2, 10, new SlidingTTLStrategy(), new LRUEvictStrategy<>(), clock);
        cache.put("a", 1);
        cache.put("b", 2);
        cache.get("a");
        cache.put("c", 3);
        check("b evicted (least recently used)", cache.get("b") == null);
        check("a kept (recently read)", Integer.valueOf(1).equals(cache.get("a")));
        check("c kept (newest)", Integer.valueOf(3).equals(cache.get("c")));
        cache.shutDown();
    }

    // Scenario 2: updating an existing key must not evict anything, even when full
    static void testUpdateDoesNotEvict() {
        System.out.println("-- update on a full cache does not evict");
        FakeClock clock = new FakeClock();
        Cache<String, Integer> cache = new Cache<>(2, 10, new SlidingTTLStrategy(), new LRUEvictStrategy<>(), clock);
        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("a", 10);
        check("a updated to new value", Integer.valueOf(10).equals(cache.get("a")));
        check("b still present", Integer.valueOf(2).equals(cache.get("b")));
        cache.shutDown();
    }

    // Scenario 3: Absolute TTL = 10s. A get at t=6s must NOT extend the life; the key dies at t=10s
    static void testAbsoluteTtl() {
        System.out.println("-- Absolute TTL");
        FakeClock clock = new FakeClock();
        Cache<String, Integer> cache = new Cache<>(5, 10, new AbsoluteTTLStrategy(), new LRUEvictStrategy<>(), clock);
        cache.put("a", 1);
        clock.advance(6 * SEC);
        check("t=6s  alive", Integer.valueOf(1).equals(cache.get("a")));
        clock.advance(5 * SEC);
        check("t=11s expired even though it was read at t=6s", cache.get("a") == null);
        cache.shutDown();
    }

    // Scenario 4: Sliding TTL = 10s. Every get pushes the expiry forward
    static void testSlidingTtl() {
        System.out.println("-- Sliding TTL");
        FakeClock clock = new FakeClock();
        Cache<String, Integer> cache = new Cache<>(5, 10, new SlidingTTLStrategy(), new LRUEvictStrategy<>(), clock);
        cache.put("a", 1);
        clock.advance(6 * SEC);
        check("t=6s  alive", Integer.valueOf(1).equals(cache.get("a")));
        clock.advance(6 * SEC);
        check("t=12s alive (read at t=6s extended it to t=16s)", Integer.valueOf(1).equals(cache.get("a")));
        clock.advance(11 * SEC);
        check("t=23s expired (no access for 11s)", cache.get("a") == null);
        cache.shutDown();
    }

    // Scenario 5: a put on an existing key restarts the TTL under BOTH strategies
    static void testPutResetsTtl() {
        System.out.println("-- put resets TTL (absolute)");
        FakeClock clock = new FakeClock();
        Cache<String, Integer> cache = new Cache<>(5, 10, new AbsoluteTTLStrategy(), new LRUEvictStrategy<>(), clock);
        cache.put("a", 1);
        clock.advance(6 * SEC);
        cache.put("a", 2);
        clock.advance(6 * SEC);
        check("t=12s alive (re-put at t=6s, expires t=16s)", Integer.valueOf(2).equals(cache.get("a")));
        cache.shutDown();
    }

    // Scenario 6: delete removes the entry and reports whether it existed
    static void testDelete() {
        System.out.println("-- delete");
        FakeClock clock = new FakeClock();
        LRUEvictStrategy<String> strategy = new LRUEvictStrategy<>();
        Cache<String, Integer> cache = new Cache<>(5, 10, new SlidingTTLStrategy(), strategy, clock);
        cache.put("a", 1);
        check("delete existing returns true", cache.delete("a"));
        check("get after delete is null", cache.get("a") == null);
        check("delete missing returns false", !cache.delete("a"));
        check("eviction strategy also forgot the key", !strategy.nodeMap.containsKey("a"));
        cache.shutDown();
    }

    // Scenario 7: an expired entry that is read is removed, so capacity is freed for new keys
    static void testExpiredEntryFreesCapacity() {
        System.out.println("-- expired entry frees capacity");
        FakeClock clock = new FakeClock();
        Cache<String, Integer> cache = new Cache<>(2, 10, new AbsoluteTTLStrategy(), new LRUEvictStrategy<>(), clock);
        cache.put("a", 1);
        cache.put("b", 2);
        clock.advance(11 * SEC);
        check("a expired", cache.get("a") == null);
        cache.put("c", 3);
        cache.put("d", 4);
        check("c present", Integer.valueOf(3).equals(cache.get("c")));
        check("d present", Integer.valueOf(4).equals(cache.get("d")));
        cache.shutDown();
    }

    // Scenario 8: the background sweeper removes an expired key with NO get call.
    // Uses the real clock (the scheduler runs on real time); TTL = 1s, sweep every 500ms.
    static void testSweeperRemovesWithoutGet() throws InterruptedException {
        System.out.println("-- background sweeper");
        LRUEvictStrategy<String> strategy = new LRUEvictStrategy<>();
        Cache<String, Integer> cache = new Cache<>(5, 1, new AbsoluteTTLStrategy(), strategy, new SystemClock());
        cache.put("a", 1);
        check("a tracked right after put", strategy.nodeMap.containsKey("a"));
        Thread.sleep(2000);
        check("sweeper removed a without any get", !strategy.nodeMap.containsKey("a"));
        cache.shutDown();
    }

    // Scenario 9: 8 threads hammer a small cache. No exception, and size never exceeds capacity
    static void testConcurrentAccess() throws InterruptedException {
        System.out.println("-- concurrent access");
        int capacity = 50;
        LRUEvictStrategy<Integer> strategy = new LRUEvictStrategy<>();
        Cache<Integer, Integer> cache = new Cache<>(capacity, 1, new SlidingTTLStrategy(), strategy, new SystemClock());
        int threads = 8, opsPerThread = 5000;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger errors = new AtomicInteger();
        List<Runnable> tasks = new ArrayList<>();
        for (int t = 0; t < threads; t++) {
            final int seed = t;
            tasks.add(() -> {
                try {
                    start.await();
                    java.util.Random r = new java.util.Random(seed);
                    for (int i = 0; i < opsPerThread; i++) {
                        int key = r.nextInt(200);
                        switch (r.nextInt(3)) {
                            case 0: cache.put(key, key); break;
                            case 1: Integer v = cache.get(key); if (v != null && v != key) errors.incrementAndGet(); break;
                            default: cache.delete(key);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    errors.incrementAndGet();
                }
            });
        }
        tasks.forEach(pool::submit);
        start.countDown();
        pool.shutdown();
        check("workers finished", pool.awaitTermination(30, TimeUnit.SECONDS));
        check("no exception / no wrong value", errors.get() == 0);
        check("size <= capacity", strategy.nodeMap.size() <= capacity);
        cache.shutDown();
    }

    // Scenario 10: constructor validation
    static void testValidation() {
        System.out.println("-- validation");
        boolean cap = false, ttl = false;
        try { new Cache<String, Integer>(0, 10, new SlidingTTLStrategy(), new LRUEvictStrategy<>()); }
        catch (IllegalArgumentException e) { cap = true; }
        try { new Cache<String, Integer>(5, 0, new SlidingTTLStrategy(), new LRUEvictStrategy<>()); }
        catch (IllegalArgumentException e) { ttl = true; }
        check("capacity 0 rejected", cap);
        check("ttl 0 rejected", ttl);
    }
}
