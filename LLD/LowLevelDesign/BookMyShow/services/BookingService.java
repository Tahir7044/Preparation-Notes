package LowLevelDesign.BookMyShow.services;

import LowLevelDesign.BookMyShow.enums.BookingStatus;
import LowLevelDesign.BookMyShow.enums.PaymentType;
import LowLevelDesign.BookMyShow.models.Booking;
import LowLevelDesign.BookMyShow.models.Seat;
import LowLevelDesign.BookMyShow.models.Show;
import LowLevelDesign.BookMyShow.strategy.LockingStrategy.LockProvider;
import LowLevelDesign.BookMyShow.strategy.paymentStrategy.PaymentStrategy;
import LowLevelDesign.BookMyShow.strategy.paymentStrategy.PaymentStrategyFactory;

import java.util.List;
import java.util.UUID;

public class BookingService {
    private final static long TTL = 5000L;
    private final LockProvider lockProvider;

    public BookingService(LockProvider lockProvider){
        this.lockProvider = lockProvider;
    }

    public Booking createBooking(String userId, Show show, List<Seat> selectedSeats) {
        for (Seat seat : selectedSeats) {
            String key = show.getId() + ":" + seat.getId();
            if (!lockProvider.tryLock(key, userId, TTL)) {
                throw new IllegalStateException("Seat not available: " + seat.getId());
            }
        }
        double totalCost = 0;
        for (Seat seat : selectedSeats) {
            totalCost += seat.getPrice();
        }
        return new Booking(
                UUID.randomUUID().toString(),
                userId,
                show.getId(),
                selectedSeats,
                totalCost
        );
    }

    public void confirmBooking(Booking booking, PaymentType paymentType){
        if(booking.getBookingStatus() != BookingStatus.CREATED){
            throw new IllegalStateException("Booking not in CREATED state");
        }

        for (Seat seat : booking.getSeats()) {
            String key = booking.getShowId() + ":" + seat.getId();
            if (lockProvider.isLockExpired(key) || !lockProvider.isLockedBy(key, booking.getUserId())) {
                throw new IllegalStateException("Seat lock expired or not owned: " + seat.getId());
            }
        }

        booking.setPaymentType(paymentType);
        PaymentStrategy paymentStrategy = PaymentStrategyFactory.create(paymentType);
        boolean paid = paymentStrategy.pay(booking);

        if (paid) {
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            for (Seat seat : booking.getSeats()) {
                lockProvider.unlock(booking.getShowId() + ":" + seat.getId());
            }
        } else {
            booking.setBookingStatus(BookingStatus.FAILED);
        }
    }
}
