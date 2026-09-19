package LowLevelDesign.ParkingLot.model;

import LowLevelDesign.ParkingLot.ParkingLot;
import java.time.LocalDateTime;

public class EntryGate extends Gate {
    public EntryGate(String id){
        super(id);
    }
    public String parkVehicle(Vehicle vehicle, LocalDateTime entryTime){
        Ticket ticket = ParkingLot.getInstance().parkVehicle(vehicle, entryTime);
        return ticket != null ? ticket.getTicketId() : null;
    }
}