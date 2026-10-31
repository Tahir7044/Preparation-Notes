// Functional Requirements:
// 1. Organizer can schedule a meeting in a room for a time slot with participants
// 2. Organizer can add participants to an existing meeting
// 3. Organizer can reschedule a meeting (time and/or room) — meeting keeps the same id
// 4. Detect conflicts — a meeting can end and the next can start at the same time (back-to-back allowed)
// 5. Room conflicts are rejected, participant conflicts are allowed
// 6. Organizer can cancel a meeting — the room slot is freed
// 7. User can view their scheduled meetings within a time window

// Non-Functional Requirements:
// - Thread-safe room booking: per-room ReentrantLock, check + insert atomic in RoomCalendar
// - Reschedule across rooms locks both rooms in roomId order to avoid deadlock
// - Cancel / reschedule / addParticipant on the same meeting are serialized via synchronized(meeting)
// - Conflict rule is pluggable via ConflictChecker (Strategy pattern)
// - Error handling via domain exceptions (InvalidMeeting, RoomNotAvailable, Unauthorized, ...)

// Out of scope:
// - Recurring meetings
// - Notifications / RSVP (accept, reject)
// - Finding free slots for participants
// - Room capacity validation (modelled on Room, checks TODO)
// - UI

/*

Classes

- User
    - userId, name

- Room
    - roomId, capacity

- Meeting
    - meetingId, title, organizer
    - start, end: LocalDateTime
    - participants: Set<userId>          ← ConcurrentHashMap.newKeySet()
    - room: Room
    - meetingStatus: MeetingStatus       ← SCHEDULED → CANCELLED

- RoomCalendar                           ← one per room
    - meetings: TreeMap<start, Meeting>
    - lock: ReentrantLock
    - conflictChecker: ConflictChecker
    + isAvailable(start, end, excludeMeetingId): boolean
    + book(meeting)                      ← throws RoomNotAvailableException
    + release(meeting)

- UserService
    - users: Map<userId, User>
    + getUser(userId)

- RoomService
    - rooms: Map<roomId, Room>
    - roomCalendars: ConcurrentHashMap<roomId, RoomCalendar>
    + bookRoom(roomId, meeting)
    + releaseRoom(roomId, meeting)
    + reschedule(meeting, newRoomId, start, end)   ← locks both rooms in order

- MeetingService
    - meetings: Map<meetingId, Meeting>
    - userMeetings: ConcurrentHashMap<userId, Set<Meeting>>
    + createMeeting(organizerId, title, start, end, participants, roomId): Meeting
    + addParticipant(meetingId, userId, requesterId)
    + rescheduleMeeting(meetingId, requesterId, start, end, roomId): Meeting
    + cancelMeeting(meetingId, requesterId)
    + getMeetings(userId, from, to): List<Meeting>

Strategy Pattern:
    ConflictChecker → hasConflict(existing, excludeMeetingId, start, end)
        - RoomConflictChecker   ← overlap: start < other.end && other.start < end

enum MeetingStatus { SCHEDULED, CANCELLED }

*/

import model.Meeting;
import model.Room;
import model.User;
import service.MeetingService;
import service.RoomService;
import service.UserService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

class Main {

    public static void main(String[] args) {

        // --- Setup ---
        UserService userService = new UserService(Map.of(
                "alice", new User("alice", "Alice"),
                "bob", new User("bob", "Bob"),
                "carol", new User("carol", "Carol"),
                "dave", new User("dave", "Dave")));

        RoomService roomService = new RoomService(Map.of(
                "R1", new Room("R1", 10),
                "R2", new Room("R2", 6)));

        MeetingService meetingService = new MeetingService(new ConcurrentHashMap<>(), userService, roomService);

        LocalDateTime day = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime t10 = day.withHour(10), t1030 = t10.plusMinutes(30), t11 = day.withHour(11),
                t1130 = t11.plusMinutes(30), t12 = day.withHour(12);

        // ===== Scenario 1: Schedule a meeting =====
        System.out.println("===== Scenario 1: Schedule meeting in R1 10:00-11:00 =====");
        Meeting standup = meetingService.createMeeting("alice", "Standup", t10, t11, Set.of("bob"), "R1");
        System.out.println("Created: " + standup.getMeetingId() + " status=" + standup.getMeetingStatus());

        // ===== Scenario 2: Back-to-back meeting in the same room =====
        System.out.println("\n===== Scenario 2: Back-to-back in R1 11:00-12:00 =====");
        Meeting review = meetingService.createMeeting("bob", "Design Review", t11, t12, Set.of("carol"), "R1");
        System.out.println("Created: " + review.getTitle() + " (back-to-back allowed)");

        // ===== Scenario 3: Overlapping meeting in the same room =====
        System.out.println("\n===== Scenario 3: Overlap in R1 10:30-11:30 =====");
        try {
            meetingService.createMeeting("carol", "Overlap", t1030, t1130, Set.of(), "R1");
        } catch (Exception e) {
            System.out.println("Rejected as expected: " + e.getMessage());
        }

        // ===== Scenario 4: Participant conflict allowed (different room) =====
        System.out.println("\n===== Scenario 4: bob double-booked in R2 10:30-11:30 =====");
        Meeting sync = meetingService.createMeeting("dave", "Sync", t1030, t1130, Set.of("bob"), "R2");
        System.out.println("Created: " + sync.getTitle() + " (participant conflict allowed)");

        // ===== Scenario 5: Invalid time =====
        System.out.println("\n===== Scenario 5: end before start =====");
        try {
            meetingService.createMeeting("alice", "Bad", t11, t10, Set.of(), "R1");
        } catch (Exception e) {
            System.out.println("Rejected as expected: " + e.getMessage());
        }

        // ===== Scenario 6: Add participant =====
        System.out.println("\n===== Scenario 6: alice adds carol to Standup =====");
        meetingService.addParticipant(standup.getMeetingId(), "carol", "alice");
        System.out.println("Participants: " + standup.getParticipants());

        // ===== Scenario 7: Reschedule overlapping its own slot =====
        System.out.println("\n===== Scenario 7: Reschedule Standup R1 10:00-11:00 -> 09:30-10:30 =====");
        meetingService.rescheduleMeeting(standup.getMeetingId(), "alice", t10.minusMinutes(30), t1030, "R1");
        System.out.println("Now: " + standup.getStart().toLocalTime() + "-" + standup.getEnd().toLocalTime()
                + " id unchanged=" + standup.getMeetingId());

        // ===== Scenario 7b: Reschedule into a conflicting slot is rejected, meeting unchanged =====
        System.out.println("\n===== Scenario 7b: Reschedule Sync R2 -> R1 10:30-11:30 (conflicts with Design Review) =====");
        try {
            meetingService.rescheduleMeeting(sync.getMeetingId(), "dave", t1030, t1130, "R1");
        } catch (Exception e) {
            System.out.println("Rejected as expected: " + e.getMessage());
        }
        System.out.println("Sync still in " + sync.getRoom().getRoomId() + " "
                + sync.getStart().toLocalTime() + "-" + sync.getEnd().toLocalTime());

        // ===== Scenario 7c: Reschedule to another room frees the old slot =====
        System.out.println("\n===== Scenario 7c: Move Sync R2 -> R1 13:00-14:00, then book R2 10:30-11:30 =====");
        meetingService.rescheduleMeeting(sync.getMeetingId(), "dave", day.withHour(13), day.withHour(14), "R1");
        System.out.println("Sync now in " + sync.getRoom().getRoomId());
        Meeting oneOnOne = meetingService.createMeeting("carol", "1:1", t1030, t1130, Set.of("dave"), "R2");
        System.out.println("Booked old slot: " + oneOnOne.getTitle() + " in R2");

        // ===== Scenario 8: Unauthorized cancel =====
        System.out.println("\n===== Scenario 8: bob tries to cancel alice's meeting =====");
        try {
            meetingService.cancelMeeting(standup.getMeetingId(), "bob");
        } catch (Exception e) {
            System.out.println("Rejected as expected: " + e.getMessage());
        }

        // ===== Scenario 9: Cancel frees the slot =====
        System.out.println("\n===== Scenario 9: bob cancels Design Review, carol rebooks R1 11:00-12:00 =====");
        meetingService.cancelMeeting(review.getMeetingId(), "bob");
        Meeting rebook = meetingService.createMeeting("carol", "Retro", t11, t12, Set.of(), "R1");
        System.out.println("Rebooked: " + rebook.getTitle());

        // ===== Scenario 9b: Operations on a cancelled meeting are rejected =====
        System.out.println("\n===== Scenario 9b: cancel again / add participant / reschedule cancelled meeting =====");
        try {
            meetingService.cancelMeeting(review.getMeetingId(), "bob");
        } catch (Exception e) {
            System.out.println("Cancel again rejected: " + e.getMessage());
        }
        try {
            meetingService.addParticipant(review.getMeetingId(), "alice", "bob");
        } catch (Exception e) {
            System.out.println("Add participant rejected: " + e.getMessage());
        }
        try {
            meetingService.rescheduleMeeting(review.getMeetingId(), "bob", day.withHour(16), day.withHour(17), "R1");
        } catch (Exception e) {
            System.out.println("Reschedule rejected: " + e.getMessage());
        }

        // ===== Scenario 10: View meetings =====
        System.out.println("\n===== Scenario 10: bob's meetings 09:00-12:00 =====");
        meetingService.getMeetings("bob", day.withHour(9), t12).forEach(m ->
                System.out.println(m.getTitle() + " " + m.getRoom().getRoomId() + " "
                        + m.getStart().toLocalTime() + "-" + m.getEnd().toLocalTime()));

        // ===== Scenario 11: Two threads racing for the same slot =====
        System.out.println("\n===== Scenario 11: Concurrent threads booking R2 14:00-15:00 =====");
        CountDownLatch startGun = new CountDownLatch(1);
        Runnable race = () -> {
            try {
                startGun.await();
                meetingService.createMeeting("alice", Thread.currentThread().getName(),
                        day.withHour(14), day.withHour(15), Set.of(), "R2");
                System.out.println(Thread.currentThread().getName() + " WON the room");
            } catch (Exception e) {
                System.out.println(Thread.currentThread().getName() + " LOST: " + e.getMessage());
            }
        };
        Thread t1 = new Thread(race, "Thread-A");
        Thread t2 = new Thread(race, "Thread-B");
        t1.start();
        t2.start();
        startGun.countDown();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ignored) {}
    }
}
