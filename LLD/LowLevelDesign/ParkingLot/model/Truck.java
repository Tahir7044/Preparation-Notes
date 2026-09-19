package LowLevelDesign.ParkingLot.model;

import LowLevelDesign.ParkingLot.enums.VehicleType;

public class Truck extends Vehicle {
    public Truck(String number){
        super(number, VehicleType.TRUCK);
    }
}