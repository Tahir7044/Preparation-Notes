package LowLevelDesign.ParkingLot.model;

import LowLevelDesign.ParkingLot.ParkingLot;
import LowLevelDesign.ParkingLot.enums.PaymentMode;
import java.time.LocalDateTime;

public class ExitGate extends Gate {
    public ExitGate(String id){
        super(id);
    }
    public void unparkVehicle(String ticketId, LocalDateTime exitTime, PaymentMode method){
        ParkingLot.getInstance().unparkVehicle(ticketId, exitTime, method);
    }
}