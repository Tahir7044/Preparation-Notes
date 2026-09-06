package LowLevelDesign.ParkingLot.factory;

import LowLevelDesign.ParkingLot.enums.PaymentMode;
import LowLevelDesign.ParkingLot.strategy.PaymentStrategy.PaymentStrategy;
import LowLevelDesign.ParkingLot.strategy.PaymentStrategy.CashPayment;
import LowLevelDesign.ParkingLot.strategy.PaymentStrategy.UpiPayment;
import LowLevelDesign.ParkingLot.strategy.PaymentStrategy.CardPayment;

public class PaymentStrategyFactory {
    public static PaymentStrategy create(PaymentMode mode){
        return switch(mode){
            case CASH -> new CashPayment();
            case UPI -> new UpiPayment();
            case CARD -> new CardPayment();
        };
    }
}