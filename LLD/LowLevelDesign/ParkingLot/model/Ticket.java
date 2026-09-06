package LowLevelDesign.ParkingLot.model;

import LowLevelDesign.ParkingLot.enums.PaymentStatus;
import java.time.LocalDateTime;

public class Ticket {
    private String ticketId;
    private LocalDateTime entryTime;
    private Vehicle vehicle;
    private String floorId;
    private String spotId;
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    public String getTicketId() { return ticketId; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public Vehicle getVehicle() { return vehicle; }
    public String getFloorId() { return floorId; }
    public String getSpotId() { return spotId; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    private Ticket() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String ticketId;
        private LocalDateTime entryTime;
        private Vehicle vehicle;
        private String floorId;
        private String spotId;

        public Builder ticketId(String ticketId) { this.ticketId = ticketId; return this; }
        public Builder entryTime(LocalDateTime entryTime) { this.entryTime = entryTime; return this; }
        public Builder vehicle(Vehicle vehicle) { this.vehicle = vehicle; return this; }
        public Builder floorId(String floorId) { this.floorId = floorId; return this; }
        public Builder spotId(String spotId) { this.spotId = spotId; return this; }

        public Ticket build() {
            Ticket ticket = new Ticket();
            ticket.ticketId = ticketId;
            ticket.entryTime = entryTime;
            ticket.vehicle = vehicle;
            ticket.floorId = floorId;
            ticket.spotId = spotId;
            return ticket;
        }
    }
}