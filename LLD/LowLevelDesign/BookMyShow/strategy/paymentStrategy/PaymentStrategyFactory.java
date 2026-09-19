package LowLevelDesign.BookMyShow.strategy.paymentStrategy;

import LowLevelDesign.BookMyShow.enums.PaymentType;

public class PaymentStrategyFactory {
    public static PaymentStrategy create(PaymentType paymentType) {
        return switch (paymentType) {
            case CASH -> new CashPaymentStrategy();
            case UPI  -> new UpiPaymentStrategy();
            case CARD -> new CardPaymentStrategy();
        };
    }
}
