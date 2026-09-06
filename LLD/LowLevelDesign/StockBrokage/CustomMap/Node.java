package LowLevelDesign.CustomMap;

public class Node <K,V>{
       K key;
       V value;
       Node next;
       Node prev;
       public Node(K key, V value){
           this.key = key;
           this.value =  value;
       }

        public Node(){
            this.key = null;
            this.value =  null;
        }
}
