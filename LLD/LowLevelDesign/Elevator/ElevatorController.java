package LowLevelDesign.Elevator;

import java.util.ArrayList;
import java.util.List;

public class ElevatorController {
    private static final int MAX_FLOOR = 10;
    List<Elevator> elevators;

    public ElevatorController(int numElevators) {
        elevators = new ArrayList<>();
        for (int i = 0; i < numElevators; i++) {
            elevators.add(new Elevator());
        }
    }

    public void step(){
        for(Elevator e: elevators){
            e.step();
        }
    }
    public boolean requestElevator(int floor, RequestType type){

        if(floor<0 || floor> MAX_FLOOR){
            return false;
        }

        if(type == RequestType.DESTINATION) {
            return false;
        }

        Request request = new Request(floor, type);
        Elevator best = findBestElevator(request);
        return best.addRequest(request);
    }

    public boolean pressFloorButton(int elevatorIndex, int floor) {
        if (elevatorIndex < 0 || elevatorIndex >= elevators.size()) return false;
        if (floor < 0 || floor > MAX_FLOOR) return false;
        return elevators.get(elevatorIndex).addRequest(new Request(floor, RequestType.DESTINATION));
    }

    private Elevator findBestElevator(Request request){
        Elevator best = findCommittedToFloor(request);
        if(best != null){
            return best;
        }
        best = findNearestIdle(request);
        if(best != null){
            return best;
        }
        return findNearest(request);
    }

    private Elevator findCommittedToFloor(Request request){
        int floor = request.getFloor();
        Direction direction = request.getRequestType() == RequestType.PICK_UP ? Direction.UP: Direction.DOWN;
        Elevator nearest = null;
        int distance = Integer.MAX_VALUE;
        for(Elevator e: elevators){
            int elevatorFloor = e.getFloor();
            Direction elevatorDirection = e.getDirection();
            if(elevatorDirection != direction){
                continue;
            }
            if((elevatorDirection == Direction.UP && elevatorFloor > floor )
                    || (elevatorDirection == Direction.DOWN && elevatorFloor < floor )){
                continue;
            }
            if(!e.hasRequestsAtOrBeyond(floor,direction)){
                continue;
            }
            int dis = Math.abs(floor-elevatorFloor);
            if(dis<distance){
                distance = dis;
                nearest = e;
            }
        }
        return nearest;
    }

    private Elevator findNearestIdle(Request request){
        int floor = request.getFloor();
        Elevator nearest = null;
        int distance = Integer.MAX_VALUE;
        for(Elevator e: elevators){
            if(e.getDirection() == Direction.IDLE){
                int elevatorFloor = e.getFloor();
                int dis = Math.abs(floor-elevatorFloor);
                if(dis<distance){
                    distance = dis;
                    nearest = e;
                }
            }
        }
        return nearest;
    }

    private Elevator findNearest(Request request){
        int floor = request.getFloor();
        Elevator nearest = null;
        int distance = Integer.MAX_VALUE;
        for(Elevator e: elevators){
            int elevatorFloor = e.getFloor();
            int dis = Math.abs(floor-elevatorFloor);
            if(dis<distance){
                distance = dis;
                nearest = e;
            }
        }
        return nearest;
    }

}
