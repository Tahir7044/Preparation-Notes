package LowLevelDesign.ParkingLot.model;

import LowLevelDesign.ParkingLot.enums.VehicleType;

public class Car extends Vehicle {
    public Car(String number){
        super(number, VehicleType.CAR);
    }
}