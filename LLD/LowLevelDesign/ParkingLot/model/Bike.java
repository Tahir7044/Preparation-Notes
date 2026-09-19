package LowLevelDesign.ParkingLot.model;

import LowLevelDesign.ParkingLot.enums.VehicleType;

public class Bike extends Vehicle {
    public Bike(String number){
        super(number, VehicleType.BIKE);
    }
}