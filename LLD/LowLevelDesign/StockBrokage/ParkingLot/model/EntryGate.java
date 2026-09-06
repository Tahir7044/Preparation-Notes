package LowLevelDesign.ParkingLot.model;

import LowLevelDesign.ParkingLot.ParkingLot;
import java.time.LocalDateTime;

public class EntryGate extends Gate {
    public EntryGate(String id){
        super(id);
    }
    public Ticket parkVehicle(Vehicle vehicle, LocalDateTime entryTime){
        return ParkingLot.getInstance().parkVehicle(vehicle, entryTime);
    }
}