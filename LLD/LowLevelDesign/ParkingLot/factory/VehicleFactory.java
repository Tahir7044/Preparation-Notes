package LowLevelDesign.ParkingLot.factory;

import LowLevelDesign.ParkingLot.model.Vehicle;
import LowLevelDesign.ParkingLot.model.Car;
import LowLevelDesign.ParkingLot.model.Bike;
import LowLevelDesign.ParkingLot.model.Truck;
import LowLevelDesign.ParkingLot.enums.VehicleType;

public class VehicleFactory {
    public static Vehicle create(String number, VehicleType type){
        return switch(type){
            case CAR -> new Car(number);
            case BIKE -> new Bike(number);
            case TRUCK -> new Truck(number);
        };
    }
}