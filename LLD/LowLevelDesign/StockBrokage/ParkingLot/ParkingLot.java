package LowLevelDesign.ParkingLot;

import LowLevelDesign.ParkingLot.enums.PaymentMode;
import LowLevelDesign.ParkingLot.enums.PaymentStatus;
import LowLevelDesign.ParkingLot.enums.PricingStrategyType;
import LowLevelDesign.ParkingLot.factory.PaymentStrategyFactory;
import LowLevelDesign.ParkingLot.factory.PricingStrategyFactory;
import LowLevelDesign.ParkingLot.model.ParkingFloor;
import LowLevelDesign.ParkingLot.model.Spot;
import LowLevelDesign.ParkingLot.model.Ticket;
import LowLevelDesign.ParkingLot.model.Vehicle;
import LowLevelDesign.ParkingLot.strategy.PaymentStrategy.PaymentStrategy;
import LowLevelDesign.ParkingLot.strategy.PricingStrategy.PricingStrategy;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingLot {

    private static final ParkingLot INSTANCE = new ParkingLot();

    private final Map<String, ParkingFloor> floors = new ConcurrentHashMap<>();
    private final Map<String, Ticket> activeTickets = new ConcurrentHashMap<>();

    private PricingStrategy pricingStrategy = PricingStrategyFactory.create(PricingStrategyType.TIME_BASED);

    private ParkingLot(){
    }

    public static ParkingLot getInstance() {
        return INSTANCE;
    }

    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public void addFloor(ParkingFloor floor) {
        floors.put(floor.getFloorId(), floor);
    }

    public Ticket parkVehicle(Vehicle vehicle, LocalDateTime entryTime) {
        for (ParkingFloor floor : floors.values()) {
            Spot spot = floor.findAvailableSpot(vehicle.getType());

            if (spot != null) {
                String ticketId = UUID.randomUUID().toString();
                Ticket ticket = Ticket.builder()
                        .ticketId(ticketId)
                        .entryTime(entryTime)
                        .vehicle(vehicle)
                        .floorId(floor.getFloorId())
                        .spotId(spot.getSpotId())
                        .build();

                activeTickets.put(ticketId, ticket);
                System.out.println("Vehicle parked. Ticket: " + ticketId);
                return ticket;
            }
        }

        System.out.println("No spot available for vehicle type: " + vehicle.getType());
        return null;
    }

    public void unparkVehicle(String ticketId, LocalDateTime exitTime, PaymentMode paymentMode) {
        Ticket ticket = activeTickets.get(ticketId);
        if (ticket == null) {
            System.out.println("Invalid ticket ID.");
            return;
        }

        double fee = pricingStrategy.calculateFee(
                ticket.getVehicle().getType(),
                ticket.getEntryTime(),
                exitTime
        );

        PaymentStrategy strategy = PaymentStrategyFactory.create(paymentMode);
        boolean paid = strategy.processPayment(ticket, fee);
        ticket.setPaymentStatus(paid ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);

        if (!paid) {
            System.out.println("Vehicle cannot exit. Payment unsuccessful.");
            return;
        }

        Spot spot = floors.get(ticket.getFloorId()).getSpots().get(ticket.getSpotId());
        spot.vacate();
        activeTickets.remove(ticketId);
        System.out.println("Vehicle exited. Fee charged: " + fee);
    }

    public void printStatus() {
        floors.forEach((floorId, floor) -> {
            System.out.println("Floor: " + floorId);
            floor.getSpots().values().forEach(spot -> {
                System.out.println("  Spot " + spot.getSpotId() + " [" + spot.getSpotType() + "] - " + (spot.isOccupied() ? "Occupied" : "Free"));
            });
        });
    }
}