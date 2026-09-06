package LowLevelDesign.ParkingLot.strategy.PaymentStrategy;

import LowLevelDesign.ParkingLot.model.Ticket;

public interface PaymentStrategy {
    boolean processPayment(Ticket ticket, double amount);
}