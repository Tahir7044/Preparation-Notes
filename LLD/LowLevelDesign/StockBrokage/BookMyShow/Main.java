package LowLevelDesign.BookMyShow;

// Functional Requirements:
// 1. User can browse movies and shows at a theater
// 2. User selects seats from a show — seats are temporarily locked (TTL-based) to prevent double booking
// 3. User confirms the booking within TTL window and pays via selected payment method
// 4. On successful payment, booking is CONFIRMED and seat locks are released
// 5. On payment failure, booking is marked FAILED (locks expire naturally or can be released)
// 6. Concurrent users attempting the same seat — only first one gets the lock

// Non-Functional Requirements:
// - Thread-safe seat locking via ConcurrentHashMap.compute (atomic CAS)
// - Lock TTL prevents seats from being held indefinitely if user abandons flow
// - Background sweeper cleans expired locks every minute
// - Payment method is pluggable via Strategy pattern

// Out of scope:
// - User authentication / login
// - Search and filtering of movies
// - Seat map / layout rendering
// - Cancellation and refunds
// - Notification / email on booking

/*

Classes

- Movie
    - id, name, duration

- Theater
    - id, name
    - screens: Map<String, Screen>
    + addScreen(screen)
    + getScreen(screenId)

- Screen
    - id
    - seats: Map<String, Seat>
    + addSeat(seat)
    + getSeats(): List<Seat>

- Seat (abstract)
    - id, price
    + getSeatType(): SeatType    ← abstract, implemented by subclasses
    - RegularSeat  → SeatType.REGULAR
    - ReclinerSeat → SeatType.RECLINER

- Show
    - id, movie, screen, theater
    - startDate, endDate: LocalDateTime
    + getSeats(): List<Seat>     ← delegates to screen

- Booking
    - bookingId, userId, showId
    - seats: List<Seat>
    - amount: double
    - bookingStatus: BookingStatus    ← CREATED → CONFIRMED / FAILED
    - paymentType: PaymentType

- BookingService
    + createBooking(userId, show, selectedSeats): Booking   ← locks seats, throws if unavailable
    + confirmBooking(booking, paymentType)                  ← validates lock, pays, updates status

Strategy Pattern:
    PaymentStrategy → pay(booking): boolean
        - CashPaymentStrategy | UpiPaymentStrategy | CardPaymentStrategy
    PaymentStrategyFactory → create(PaymentType): PaymentStrategy

    LockProvider → tryLock / unlock / isLockExpired / isLockedBy
        - InMemoryLockProvider   ← ConcurrentHashMap + TTL + background sweeper

enum SeatType     { REGULAR, RECLINER }
enum BookingStatus { CREATED, CONFIRMED, FAILED, CANCELLED }
enum PaymentType  { CASH, UPI, CARD }

*/

import LowLevelDesign.BookMyShow.enums.BookingStatus;
import LowLevelDesign.BookMyShow.enums.PaymentType;
import LowLevelDesign.BookMyShow.models.*;
import LowLevelDesign.BookMyShow.services.BookingService;
import LowLevelDesign.BookMyShow.strategy.LockingStrategy.InMemoryLockProvider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Main {

    public static void main(String[] args) {

        // --- Setup ---
        Movie movie = new Movie("M1", "Interstellar", 169);

        Screen screen = new Screen("SC1");
        Seat s1 = new RegularSeat("S1", 200.0);
        Seat s2 = new RegularSeat("S2", 200.0);
        Seat s3 = new ReclinerSeat("S3", 500.0);
        screen.addSeat(s1);
        screen.addSeat(s2);
        screen.addSeat(s3);

        Theater theater = new Theater("T1", "PVR Cinemas");
        theater.addScreen(screen);

        Show show = new Show("SH1", movie, screen, theater,
                LocalDateTime.now(), LocalDateTime.now().plusHours(3));

        BookingService bookingService = new BookingService(new InMemoryLockProvider());

        // ===== Scenario 1: Successful booking with UPI payment =====
        System.out.println("===== Scenario 1: Successful booking (UPI) =====");
        Booking booking1 = null;
        try {
            booking1 = bookingService.createBooking("user1", show, List.of(s1, s2));
            System.out.println("Booking created: " + booking1.getBookingId());
            System.out.println("Status: " + booking1.getBookingStatus());
            System.out.println("Amount: ₹" + booking1.getAmount());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // ===== Scenario 2: Concurrent booking — same seats still locked by user1 =====
        System.out.println("\n===== Scenario 2: Concurrent booking — seat already locked =====");
        try {
            Booking booking2 = bookingService.createBooking("user2", show, List.of(s1));
            System.out.println("Booking created: " + booking2.getBookingId());
        } catch (Exception e) {
            System.out.println("Blocked as expected: " + e.getMessage());
        }

        // Now user1 confirms — locks released
        try {
            bookingService.confirmBooking(booking1, PaymentType.UPI);
            System.out.println("user1 confirmed: " + booking1.getBookingStatus());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // ===== Scenario 3: Double confirmation — booking already CONFIRMED =====
        System.out.println("\n===== Scenario 3: Confirm already-confirmed booking =====");
        try {
            Booking booking3 = bookingService.createBooking("user3", show, List.of(s3));
            bookingService.confirmBooking(booking3, PaymentType.CARD);
            System.out.println("First confirm: " + booking3.getBookingStatus());

            bookingService.confirmBooking(booking3, PaymentType.CASH);
        } catch (Exception e) {
            System.out.println("Blocked as expected: " + e.getMessage());
        }

        // ===== Scenario 4: Booking with CASH payment =====
        System.out.println("\n===== Scenario 4: New seats booked with CASH =====");
        Screen screen2 = new Screen("SC2");
        Seat s4 = new RegularSeat("S4", 300.0);
        Seat s5 = new ReclinerSeat("S5", 600.0);
        screen2.addSeat(s4);
        screen2.addSeat(s5);

        Show show2 = new Show("SH2", movie, screen2, theater,
                LocalDateTime.now().plusHours(4), LocalDateTime.now().plusHours(7));
        try {
            Booking booking4 = bookingService.createBooking("user4", show2, List.of(s4, s5));
            System.out.println("Booking created. Amount: ₹" + booking4.getAmount());
            bookingService.confirmBooking(booking4, PaymentType.CASH);
            System.out.println("Status: " + booking4.getBookingStatus());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // ===== Scenario 5: Two threads racing for the same seat =====
        System.out.println("\n===== Scenario 5: Concurrent threads racing for same seat =====");
        Screen screen3 = new Screen("SC3");
        Seat contested = new RegularSeat("S6", 250.0);
        screen3.addSeat(contested);
        Show show3 = new Show("SH3", movie, screen3, theater,
                LocalDateTime.now().plusHours(8), LocalDateTime.now().plusHours(11));

        CountDownLatch startGun = new CountDownLatch(1);

        Thread t1 = new Thread(() -> {
            try {
                startGun.await();
                Booking b = bookingService.createBooking("userA", show3, List.of(contested));
                System.out.println("userA WON the seat. BookingId: " + b.getBookingId());
                bookingService.confirmBooking(b, PaymentType.UPI);
                System.out.println("userA confirmed. Status: " + b.getBookingStatus());
            } catch (Exception e) {
                System.out.println("userA LOST: " + e.getMessage());
            }
        }, "Thread-userA");

        Thread t2 = new Thread(() -> {
            try {
                startGun.await();
                Booking b = bookingService.createBooking("userB", show3, List.of(contested));
                System.out.println("userB WON the seat. BookingId: " + b.getBookingId());
                bookingService.confirmBooking(b, PaymentType.UPI);
                System.out.println("userB confirmed. Status: " + b.getBookingStatus());
            } catch (Exception e) {
                System.out.println("userB LOST: " + e.getMessage());
            }
        }, "Thread-userB");

        t1.start();
        t2.start();
        startGun.countDown();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ignored) {}
    }
}
