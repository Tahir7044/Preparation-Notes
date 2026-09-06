package LowLevelDesign.Elevator;

// Requirements:
// 1. System manages 3 elevators serving 10 floors (0-9)
// 2. Users can request an elevator from any floor (hall call). System decides which elevator to dispatch.
// 3. Once inside, users can select one or more destination floors
// 4. Simulation runs in discrete time steps (e.g., a `step()` or `tick()` call advances time)
// 5. Elevator stops come in two types:
//     - Hall calls: Request from a floor with direction (UP or DOWN)
//     - Destination: Request from inside elevator (no direction specified)
// 6. System handles multiple concurrent pickup requests across floors
// 7. Invalid requests should be rejected (return false)
//     - Non-existent floor numbers
// 8. Requests for the current floor are treated as a no-op / already served (doors out of scope)

// Out of scope:
// - Weight capacity and passenger limits
// - Door open/close mechanics
// - Emergency stop functionality
// - Dynamic floor/elevator configuration
// - UI/rendering layer

/*

classes

- Request
- Elevator
- ElevatorController


ElevatorController:
    - elevators: List<Elevator>
    - MAX_FLOOR: int = 10

    + step()
    + requestElevator(floor, type)          ← hall call (PICK_UP / PICK_DOWN only)
    + pressFloorButton(elevatorIndex, floor) ← destination call from inside elevator

Elevator:
    - floor: int
    - direction: Direction
    - requests: Set<Request>
    - MAX_FLOOR: int = 10

    + addRequest(request): boolean
    + step()                                 ← runs SCAN algorithm
    + hasRequestsAhead(direction): boolean   ← renamed from noRequestsAhead
    + getDirectionForNearestRequest()        ← renamed from getDirectionASPerNearestRequest

enum Direction {
    IDLE,
    UP,
    Down
}

Request:
    int floor
    type: RequestType

enum RequestType {
    PICK_UP,
    PICK_DOWN,
    DESTINATION
}
*/

public class Main {

    static void simulate(Elevator elevator, int maxSteps) {
        for (int i = 0; i < maxSteps; i++) {
            System.out.println("Step " + i + " | Floor: " + elevator.getFloor() + " | Direction: " + elevator.getDirection());
            if (elevator.getDirection() == Direction.IDLE && elevator.getFloor() >= 0) {
                // still might have just become IDLE this tick, do one more step
                elevator.step();
                if (elevator.getDirection() == Direction.IDLE) break;
            } else {
                elevator.step();
            }
        }
        System.out.println("Final   | Floor: " + elevator.getFloor() + " | Direction: " + elevator.getDirection());
    }

    public static void main(String[] args) {

        // ===== Test 1: Single elevator, going UP =====
        System.out.println("===== Test 1: Hall calls going UP =====");
        Elevator e1 = new Elevator();
        // People waiting on floor 3 and 6, want to go UP
        e1.addRequest(new Request(3, RequestType.PICK_UP));
        e1.addRequest(new Request(6, RequestType.PICK_UP));
        // Passenger inside wants floor 5
        e1.addRequest(new Request(5, RequestType.DESTINATION));
        simulate(e1, 20);

        // ===== Test 2: Single elevator, going DOWN =====
        System.out.println("\n===== Test 2: Hall calls going DOWN =====");
        Elevator e2 = new Elevator();
        // People on floor 8 and 4 want to go DOWN
        e2.addRequest(new Request(8, RequestType.PICK_DOWN));
        e2.addRequest(new Request(4, RequestType.PICK_DOWN));
        simulate(e2, 20);

        // ===== Test 3: Mixed UP and DOWN (SCAN reverses direction) =====
        System.out.println("\n===== Test 3: Mixed requests (SCAN algorithm) =====");
        Elevator e3 = new Elevator();
        e3.addRequest(new Request(2, RequestType.PICK_UP));
        e3.addRequest(new Request(7, RequestType.PICK_UP));
        e3.addRequest(new Request(5, RequestType.PICK_DOWN));
        simulate(e3, 25);

        // ===== Test 4: Invalid requests (rejected) =====
        System.out.println("\n===== Test 4: Invalid requests =====");
        Elevator e4 = new Elevator();
        System.out.println("Add floor -1: " + e4.addRequest(new Request(-1, RequestType.PICK_UP)));  // false
        System.out.println("Add floor 11: " + e4.addRequest(new Request(11, RequestType.PICK_UP))); // false
        System.out.println("Add floor  0: " + e4.addRequest(new Request(0,  RequestType.PICK_UP))); // false (current floor)
        System.out.println("Add floor  5: " + e4.addRequest(new Request(5,  RequestType.PICK_UP))); // true

        // ===== Test 5: ElevatorController dispatching to 2 elevators =====
        System.out.println("\n===== Test 5: Controller with 2 elevators =====");
        ElevatorController controller = new ElevatorController(2);
        controller.requestElevator(3, RequestType.PICK_UP);
        controller.requestElevator(8, RequestType.PICK_DOWN);
        for (int i = 0; i < 15; i++) {
            System.out.println("Tick " + i
                + " | E0: floor=" + controller.elevators.get(0).getFloor()
                + " dir=" + controller.elevators.get(0).getDirection()
                + " | E1: floor=" + controller.elevators.get(1).getFloor()
                + " dir=" + controller.elevators.get(1).getDirection());
            controller.step();
        }

        // ===== Test 6: Full user journey via controller (hall call → destination) =====
        System.out.println("\n===== Test 6: Full journey — hall call then pressFloorButton =====");
        ElevatorController ctrl = new ElevatorController(1);

        // User on floor 4 presses UP
        ctrl.requestElevator(4, RequestType.PICK_UP);

        // Step until elevator reaches floor 4
        while (ctrl.elevators.get(0).getFloor() != 4) {
            ctrl.step();
        }
        System.out.println("Elevator arrived at floor 4. User boards.");

        // User inside presses floor 9
        ctrl.pressFloorButton(0, 9);
        System.out.println("User pressed floor 9 inside elevator.");

        // Step until elevator reaches floor 9
        while (ctrl.elevators.get(0).getFloor() != 9) {
            System.out.println("Floor: " + ctrl.elevators.get(0).getFloor() + " | Dir: " + ctrl.elevators.get(0).getDirection());
            ctrl.step();
        }
        System.out.println("Elevator reached floor 9. User exits.");
    }
}