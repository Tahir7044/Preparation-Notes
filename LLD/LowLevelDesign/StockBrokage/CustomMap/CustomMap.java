package LowLevelDesign.CustomMap;



public class CustomMap<K, V> {
    private final int CAPACITY = (int)1e6;
    private int DEFAULT_SIZE = 10;
    private double LOAD_FACTOR = 0.75;
    int nodeCount = 0;
    Node<K,V>[] nodes;



    public CustomMap(){
        nodes = new Node[this.DEFAULT_SIZE];
        init(this.DEFAULT_SIZE, this.nodes);
    }

    private void init(int size, Node[] nodes){
        for(int i=0;i<size;i++){
            nodes[i] = new Node<>();
            nodes[i].next = new Node<>();
            nodes[i].next.prev = nodes[i];
        }
    }

    private int index(K key) {
        return Math.abs(key.hashCode()) % this.DEFAULT_SIZE;
    }

    public boolean put(K key, V value){
        int hashKey = index(key);
        Node head = nodes[hashKey];

        while(head.next.key!=null){
            if(key.equals(head.next.key)){
                head.next.value = value;
                return true;
            }
            head = head.next;
        }

        Node newNode = new Node(key, value);
        Node temp = head.next;
        newNode.next = head.next;
        newNode.prev = head;
        temp.prev = newNode;
        head.next = newNode;
        this.nodeCount++;

        int threshold = (int)(this.DEFAULT_SIZE * this.LOAD_FACTOR);
        if(threshold<=this.nodeCount){
            reHash();
        }
        return true;
    }

    public V get(K key){
        int hashKey = index(key);
        Node head = nodes[hashKey];
        while(head.next.key!=null){
            if(key.equals(head.next.key)){
                return (V)head.next.value;
            }
            head = head.next;
        }
        return null;
    }

    public boolean remove(K key){
        int hashKey = index(key);
        Node head = nodes[hashKey];
        while(head.next.key!=null){
            if(key.equals(head.next.key)){
                Node toRemove = head.next;
                head.next = toRemove.next;
                toRemove.next.prev = head;
                this.nodeCount--;
                return true;
            }
            head = head.next;
        }
        return false;
    }


    private void reHash(){
        int oldSize = this.DEFAULT_SIZE;
        Node<K, V>[] oldNodes = this.nodes;
        this.DEFAULT_SIZE = this.DEFAULT_SIZE * 2;
        this.nodes = new Node[this.DEFAULT_SIZE];
        init(this.DEFAULT_SIZE, this.nodes);
        this.nodeCount = 0;
        for(int i=0;i<oldSize;i++){
            Node head = oldNodes[i];
            while(head.next.key!=null){
                put((K)head.next.key, (V)head.next.value);
                head = head.next;
            }
        }
    }
}
