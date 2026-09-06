package LowLevelDesign.CustomMap;

// CustomMap — HashMap implementation using array of doubly-linked lists
//
// Data Structure:
//   - Array of buckets, each bucket is a doubly-linked list with dummy head + dummy tail sentinels
//   - Dummy head/tail have null keys — used to detect list boundaries
//   - Hash index = Math.abs(key.hashCode()) % DEFAULT_SIZE
//
// Collision resolution: Separate chaining (linked list per bucket)
// Rehashing: Triggered when nodeCount >= DEFAULT_SIZE * LOAD_FACTOR (0.75)
//            Doubles the array size and reinserts all existing entries
//
// Operations:
//   put(key, value)    → insert or update. O(1) avg, O(n) worst (collision)
//   get(key)           → lookup by key. O(1) avg
//   remove(key)        → delete by key. O(1) avg
//
// Internals:
//   insert(key, value, nodes[]) → private, used by both put() and reHash()
//   init(size, nodes[])         → initializes each bucket with dummy head/tail
//   reHash()                    → doubles capacity, reinitializes, reinserts all entries

/*

Classes

- CustomMap<K, V>
    - DEFAULT_SIZE: int = 10          ← initial bucket count
    - LOAD_FACTOR: double = 0.75
    - nodeCount: int                  ← total entries across all buckets
    - nodes: Node<K,V>[]              ← array of bucket heads (dummy sentinels)
    + put(key, value): boolean        ← insert or update
    + get(key): V                     ← lookup, returns null if not found
    + remove(key): boolean            ← delete, returns false if not found

- Node<K, V>
    - key: K
    - value: V
    - next: Node
    - prev: Node

Each bucket: dummyHead ↔ [data nodes...] ↔ dummyTail
             (dummyHead.key = null)       (dummyTail.key = null)
*/

public class Main {

    static void check(String label, Object expected, Object actual) {
        boolean pass = expected == null ? actual == null : expected.equals(actual);
        System.out.println((pass ? "PASS" : "FAIL") + " | " + label
                + (pass ? "" : " → expected=" + expected + " actual=" + actual));
    }

    public static void main(String[] args) {
        CustomMap<String, Integer> map = new CustomMap<>();

        // ===== Test 1: Basic put and get =====
        System.out.println("===== Test 1: Basic put and get =====");
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        check("get(a)", 1, map.get("a"));
        check("get(b)", 2, map.get("b"));
        check("get(c)", 3, map.get("c"));

        // ===== Test 2: Update existing key =====
        System.out.println("\n===== Test 2: Update existing key =====");
        map.put("a", 99);
        check("get(a) after update", 99, map.get("a"));

        // ===== Test 3: get non-existent key =====
        System.out.println("\n===== Test 3: get missing key =====");
        check("get(z) = null", null, map.get("z"));

        // ===== Test 4: remove =====
        System.out.println("\n===== Test 4: remove =====");
        check("remove(b) = true", true, map.remove("b"));
        check("get(b) after remove = null", null, map.get("b"));
        check("remove(b) again = false", false, map.remove("b"));

        // ===== Test 5: Rehash triggered — insert enough entries =====
        System.out.println("\n===== Test 5: Rehash (insert 8+ entries, DEFAULT_SIZE=10, threshold=7) =====");
        CustomMap<Integer, String> map2 = new CustomMap<>();
        for (int i = 0; i < 15; i++) {
            map2.put(i, "val" + i);
        }
        boolean allFound = true;
        for (int i = 0; i < 15; i++) {
            if (!("val" + i).equals(map2.get(i))) {
                allFound = false;
                System.out.println("FAIL | missing key=" + i);
            }
        }
        check("all 15 entries found after rehash", true, allFound);

        // ===== Test 6: Collision — keys with same hash bucket =====
        System.out.println("\n===== Test 6: Collision handling =====");
        CustomMap<String, String> map3 = new CustomMap<>();
        map3.put("Aa", "first");   // "Aa" and "BB" have same hashCode in Java
        map3.put("BB", "second");
        check("get(Aa)", "first",  map3.get("Aa"));
        check("get(BB)", "second", map3.get("BB"));

        // ===== Test 7: Negative hashCode key =====
        System.out.println("\n===== Test 7: Key with negative hashCode =====");
        CustomMap<Integer, String> map4 = new CustomMap<>();
        map4.put(-1, "negative");
        check("get(-1)", "negative", map4.get(-1));

        // ===== Test 8: Object as value =====
        System.out.println("\n===== Test 8: Object as value =====");
        CustomMap<String, int[]> map5 = new CustomMap<>();
        map5.put("coords", new int[]{10, 20});
        check("get(coords)[0]", 10, map5.get("coords")[0]);
        check("get(coords)[1]", 20, map5.get("coords")[1]);

        // ===== Test 9: Custom object as key (with equals + hashCode) =====
        System.out.println("\n===== Test 9: Custom object as key =====");

        class Point {
            final int x, y;
            Point(int x, int y) { this.x = x; this.y = y; }

            @Override
            public boolean equals(Object o) {
                if (!(o instanceof Point p)) return false;
                return x == p.x && y == p.y;
            }

            @Override
            public int hashCode() { return 31 * x + y; }
        }

        CustomMap<Point, String> map6 = new CustomMap<>();
        Point p1 = new Point(1, 2);
        Point p2 = new Point(3, 4);
        map6.put(p1, "point-1-2");
        map6.put(p2, "point-3-4");
        check("get(1,2)", "point-1-2", map6.get(new Point(1, 2)));
        check("get(3,4)", "point-3-4", map6.get(new Point(3, 4)));
        check("get(9,9) = null", null, map6.get(new Point(9, 9)));

        // update via equal key (different object reference)
        map6.put(new Point(1, 2), "updated");
        check("update via equal key", "updated", map6.get(p1));

        // remove via equal key
        check("remove(1,2)", true, map6.remove(new Point(1, 2)));
        check("get(1,2) after remove = null", null, map6.get(p1));

        // ===== Test 10: Custom object as value =====
        System.out.println("\n===== Test 10: Custom object as value =====");

        class Person {
            final String name;
            final int age;
            Person(String name, int age) { this.name = name; this.age = age; }
        }

        CustomMap<String, Person> map7 = new CustomMap<>();
        map7.put("u1", new Person("Alice", 30));
        map7.put("u2", new Person("Bob", 25));
        check("get(u1).name", "Alice", map7.get("u1").name);
        check("get(u2).age",  25,      map7.get("u2").age);

        map7.put("u1", new Person("Alice Updated", 31));
        check("update u1 name", "Alice Updated", map7.get("u1").name);
    }
}
