package LowLevelDesign.BookMyShow.strategy.paymentStrategy;

import LowLevelDesign.BookMyShow.models.Booking;

public interface PaymentStrategy {
    boolean pay(Booking booking);
}
