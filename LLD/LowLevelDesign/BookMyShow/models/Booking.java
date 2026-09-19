package LowLevelDesign.BookMyShow.models;

import LowLevelDesign.BookMyShow.enums.BookingStatus;
import LowLevelDesign.BookMyShow.enums.PaymentType;

import java.util.List;

public class Booking {
    private final String bookingId;
    private final String userId;
    private final String showId;
    private final List<Seat> seats;
    private BookingStatus bookingStatus;
    private PaymentType paymentType;
    private double amount;

    public Booking(String bookingId, String userId, String showId, List<Seat> seats, double amount) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.showId = showId;
        this.seats = seats;
        this.amount =amount;
        this.bookingStatus = BookingStatus.CREATED;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public String getShowId() {
        return showId;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", userId='" + userId + '\'' +
                ", showId='" + showId + '\'' +
                ", seats=" + seats +
                ", bookingStatus=" + bookingStatus +
                ", paymentType=" + paymentType +
                ", amount=" + amount +
                '}';
    }
}
