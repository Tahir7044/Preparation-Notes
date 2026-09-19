package LowLevelDesign.ParkingLot.model;

import LowLevelDesign.ParkingLot.enums.VehicleType;
import java.util.concurrent.atomic.AtomicBoolean;

public class Spot {
    private final String spotId;
    private final VehicleType spotType;
    private final AtomicBoolean occupied = new AtomicBoolean(false);

    public Spot(String spotId, VehicleType spotType) {
        this.spotId = spotId;
        this.spotType = spotType;
    }

    public boolean isOccupied(){
        return occupied.get();
    }

    public boolean occupy(){
        return occupied.compareAndSet(false, true);
    }

    public void vacate(){
        occupied.set(false);
    }

    public String getSpotId(){
        return spotId;
    }

    public VehicleType getSpotType(){
        return spotType;
    }

}