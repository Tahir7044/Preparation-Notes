package LowLevelDesign.ParkingLot.model;

public abstract class Gate{
    private final String id;

    protected Gate(String id) {
        this.id = id;
    }

    public String getId() { return id; }
}