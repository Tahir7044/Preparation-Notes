package LowLevelDesign.LRUWithExpiration.model;

public class Node<K> {
    public K key;
    public Node<K> next;
    public Node<K> prev;

    public Node(K key){
        this.key = key;
        this.next = null;
        this.prev = null;
    }
}
