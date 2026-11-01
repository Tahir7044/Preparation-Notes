package LowLevelDesign.LRUWithExpiration.Strategy.EvictionStrategy;

import LowLevelDesign.LRUWithExpiration.model.DoublyLinkedList;
import LowLevelDesign.LRUWithExpiration.model.Node;

import java.util.HashMap;
import java.util.Map;

public class LRUEvictStrategy<K> implements EvictionStrategy<K> {

    public DoublyLinkedList<K> list;
    public Map<K, Node<K>> nodeMap;

    public LRUEvictStrategy(){
        list = new DoublyLinkedList<>();
        nodeMap = new HashMap<>();
    }

    @Override
    public void keyAccessed(K key) {
        Node<K> node = nodeMap.get(key);
        if(node==null){
            node = new Node<>(key);
            nodeMap.put(key, node);
            list.addToHead(node);
        } else {
            list.moveToHead(node);
        }

    }

    @Override
    public void keyRemoved(K key) {
        Node<K> node = nodeMap.remove(key);
        if(node!=null){
            list.remove(node);
        }
    }

    @Override
    public K evict() {
        if(list.isEmpty()) {
            return null;
        }
        Node<K> node = list.removeTail();
        nodeMap.remove(node.key);
        return node.key;
    }
}
