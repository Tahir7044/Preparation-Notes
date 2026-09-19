package LowLevelDesign.ParkingLot.model;

import LowLevelDesign.ParkingLot.enums.VehicleType;
import java.util.HashMap;
import java.util.Map;

public class ParkingFloor {
    private final String floorId;
    private final Map<String, Spot> spots;

    public ParkingFloor(String floorId){
        this.floorId = floorId;
        spots = new HashMap<>();
    }

    public String getFloorId() { return floorId; }

    public Map<String, Spot> getSpots() { return spots; }

    public void addSpot(Spot spot){
        spots.put(spot.getSpotId(), spot);
    }

    public Spot findAvailableSpot(VehicleType vehicleType){
        for(Spot spot: spots.values()){
            if(spot.getSpotType() == vehicleType && spot.occupy()){
                return spot;
            }
        }
        return null;
    }
}