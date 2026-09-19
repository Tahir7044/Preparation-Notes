package LowLevelDesign.BookMyShow.strategy.paymentStrategy;

import LowLevelDesign.BookMyShow.models.Booking;

public class CardPaymentStrategy implements PaymentStrategy {
    public boolean pay(Booking booking) {
        System.out.println("successfully paid " + booking.getAmount() + " for booking "+ booking.getBookingId() + "through card");
        return true;
    }
}
