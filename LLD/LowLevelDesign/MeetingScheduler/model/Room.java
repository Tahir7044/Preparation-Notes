package model;

public class Room {
    private final String roomId;
    private final int capacity; //TODO will add checks later

    public Room(String id, int capacity){
        this.capacity = capacity;
        this.roomId = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public int getCapacity() {
        return capacity;
    }
}
