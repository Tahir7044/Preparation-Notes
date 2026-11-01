package LowLevelDesign.LRUWithExpiration.model;

public class DoublyLinkedList<K> {

    private final Node<K> head;
    private final Node<K> tail;

    public DoublyLinkedList(){
        head = new Node<>(null);
        tail = new Node<>(null);
        head.next = tail;
        tail.prev = head;
    }

    public boolean isEmpty(){
        return head.next==tail;
    }

    public void remove(Node<K> node){
        Node<K> prev = node.prev;
        prev.next = node.next;
        node.next.prev = prev;
        node.next = node.prev = null;
    }

    public void addToHead(Node<K> node){
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    public Node<K> removeTail() {
        if (isEmpty()) return null;
        Node<K> node = tail.prev;
        remove(node);
        return node;
    }

    public void moveToHead(Node<K> node){
        remove(node);
        addToHead(node);
    }

}
