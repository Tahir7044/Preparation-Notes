package LowLevelDesign.ParkingLot.strategy.PricingStrategy;

import LowLevelDesign.ParkingLot.enums.VehicleType;
import java.time.LocalDateTime;

public interface PricingStrategy {
    double calculateFee(VehicleType type, LocalDateTime entryTime, LocalDateTime exitTime);
}