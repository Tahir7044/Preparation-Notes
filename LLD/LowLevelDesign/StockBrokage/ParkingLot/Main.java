package LowLevelDesign.ParkingLot;

// Requirements:
// 1. System manages a multi-floor parking lot with multiple spots per floor
// 2. Each spot supports a specific vehicle type (BIKE, CAR, TRUCK)
// 3. Vehicles enter via EntryGate — system finds and assigns the first available matching spot
// 4. Spot reservation is thread-safe (AtomicBoolean CAS) to handle concurrent entries
// 5. A Ticket is issued on entry, containing: ticketId, vehicle, floorId, spotId, entryTime
// 6. Vehicles exit via ExitGate — fee is calculated and payment is processed before releasing the spot
// 7. Fee calculation is pluggable via PricingStrategy (TIME_BASED or EVENT_BASED)
// 8. Payment is pluggable via PaymentStrategy (CASH, UPI, CARD)
// 9. ParkingLot is a Singleton — shared state across all gates

// Out of scope:
// - Reservation / pre-booking
// - Multiple vehicles per spot
// - Partial payments or refunds
// - Real-time monitoring / dashboard
// - Spot type upgrades (e.g., BIKE in CAR spot)

/*

Classes

- Vehicle (abstract)
    - Car, Bike, Truck

- Gate (abstract)
    - EntryGate  → parkVehicle(vehicle, entryTime) : Ticket
    - ExitGate   → unparkVehicle(ticketId, exitTime, paymentMode)

- ParkingFloor
    - floorId: String
    - spots: Map<String, Spot>
    + addSpot(spot)
    + findAvailableSpot(vehicleType): Spot   ← thread-safe via CAS

- Spot
    - spotId: String
    - spotType: VehicleType
    - occupied: AtomicBoolean
    + occupy(): boolean   ← compareAndSet(false, true)
    + vacate()

- Ticket
    - ticketId, vehicle, floorId, spotId, entryTime, paymentStatus
    (built via Ticket.builder())

- ParkingLot  [Singleton]
    - floors: ConcurrentHashMap<String, ParkingFloor>      ← thread-safe
    - activeTickets: ConcurrentHashMap<String, Ticket>     ← thread-safe
    - pricingStrategy: PricingStrategy                     ← defaults to TIME_BASED
    + parkVehicle(vehicle, entryTime): Ticket
    + unparkVehicle(ticketId, exitTime, paymentMode)       ← payment inlined, no PaymentProcessor
    + printStatus()

enum VehicleType    { BIKE, CAR, TRUCK }
enum PaymentMode    { CASH, UPI, CARD }
enum PaymentStatus  { PENDING, SUCCESS, FAILED }
enum PricingStrategyType { TIME_BASED, EVENT_BASED }

Strategy Interfaces:
    PricingStrategy  → calculateFee(type, entryTime, exitTime): double
        - TimeBasedPricing   (peak / non-peak hourly rates)
        - EventBasedPricing  (flat hourly rate per vehicle type)

    PaymentStrategy  → processPayment(ticket, amount): boolean
        - CashPayment | UpiPayment | CardPayment

Factories:
    VehicleFactory          → create(number, VehicleType): Vehicle
    PricingStrategyFactory  → create(PricingStrategyType): PricingStrategy
    PaymentStrategyFactory  → create(PaymentMode): PaymentStrategy
*/

import LowLevelDesign.ParkingLot.enums.PaymentMode;
import LowLevelDesign.ParkingLot.enums.PricingStrategyType;
import LowLevelDesign.ParkingLot.enums.VehicleType;
import LowLevelDesign.ParkingLot.factory.PricingStrategyFactory;
import LowLevelDesign.ParkingLot.factory.VehicleFactory;
import LowLevelDesign.ParkingLot.model.EntryGate;
import LowLevelDesign.ParkingLot.model.ExitGate;
import LowLevelDesign.ParkingLot.model.ParkingFloor;
import LowLevelDesign.ParkingLot.model.Spot;
import LowLevelDesign.ParkingLot.model.Ticket;
import LowLevelDesign.ParkingLot.model.Vehicle;
import LowLevelDesign.ParkingLot.strategy.PricingStrategy.PricingStrategy;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ParkingLot lot = ParkingLot.getInstance();
        EntryGate entryGate = new EntryGate("EG1");
        ExitGate exitGate = new ExitGate("XG1");

        lot.setPricingStrategy(PricingStrategyFactory.create(PricingStrategyType.EVENT_BASED));

        ParkingFloor floor1 = new ParkingFloor("Floor1");
        floor1.addSpot(new Spot("F1S1", VehicleType.BIKE));
        floor1.addSpot(new Spot("F1S2", VehicleType.CAR));
        floor1.addSpot(new Spot("F1S3", VehicleType.TRUCK));
        floor1.addSpot(new Spot("F1S4", VehicleType.CAR));
        lot.addFloor(floor1);

        System.out.println("----- Parking Status (before) -----");
        lot.printStatus();

        LocalDateTime entryTime = LocalDateTime.of(2025, 5, 21, 7, 30);

        // Test concurrent parking — only 1 BIKE spot, so one thread will be rejected
        Vehicle bike1 = VehicleFactory.create("KA01AB1234", VehicleType.BIKE);
        Vehicle bike2 = VehicleFactory.create("KA01AB5678", VehicleType.BIKE);

        Thread t1 = new Thread(() -> entryGate.parkVehicle(bike1, entryTime));
        Thread t2 = new Thread(() -> entryGate.parkVehicle(bike2, entryTime));
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("----- Parking Status (after park) -----");
        lot.printStatus();

        // Test single car park + exit
        Vehicle car = VehicleFactory.create("MH12CD5678", VehicleType.CAR);
        Ticket ticket = entryGate.parkVehicle(car, entryTime);

        System.out.println("----- Parking Status (after car park) -----");
        lot.printStatus();

        LocalDateTime exitTime = LocalDateTime.of(2025, 5, 21, 13, 15);
        exitGate.unparkVehicle(ticket.getTicketId(), exitTime, PaymentMode.UPI);

        System.out.println("----- Parking Status (after exit) -----");
        lot.printStatus();
    }
}

/*

 */