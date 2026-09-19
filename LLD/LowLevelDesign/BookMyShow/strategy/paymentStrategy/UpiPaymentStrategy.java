package LowLevelDesign.BookMyShow.strategy.paymentStrategy;

import LowLevelDesign.BookMyShow.models.Booking;

public class UpiPaymentStrategy implements PaymentStrategy{
    @Override
    public boolean pay(Booking booking) {
        System.out.println("successfully paid " + booking.getAmount() + " for booking "+ booking.getBookingId() + "through UPI");
        return true;
    }
}
