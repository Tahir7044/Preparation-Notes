package LowLevelDesign.ParkingLot.factory;

import LowLevelDesign.ParkingLot.enums.PricingStrategyType;
import LowLevelDesign.ParkingLot.strategy.PricingStrategy.PricingStrategy;
import LowLevelDesign.ParkingLot.strategy.PricingStrategy.TimeBasedPricing;
import LowLevelDesign.ParkingLot.strategy.PricingStrategy.EventBasedPricing;

public class PricingStrategyFactory {
    public static PricingStrategy create(PricingStrategyType type){
        return switch(type){
            case TIME_BASED -> new TimeBasedPricing();
            case EVENT_BASED -> new EventBasedPricing();
        };
    }
}