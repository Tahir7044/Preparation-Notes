package LowLevelDesign.Elevator;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Elevator {
    private static final int MAX_FLOOR = 10;
    private Set<Request> requests;
    private Direction direction;
    private int currentFloor;

    public Elevator(){
        this.direction = Direction.IDLE;
        requests = new HashSet<>();
        this.currentFloor = 0;
    }

    public void step(){
        // FIFO();   //bad solution
        // findTheNearestOne(); // good
        scan(); // best
    }

    private void scan() {
        if(requests.isEmpty()){
            direction = Direction.IDLE;
            return;
        }

        if(direction == Direction.IDLE){
            direction = getDirectionForNearestRequest();
        }

        RequestType pickUpRequestType = direction ==  Direction.UP ? RequestType.PICK_UP : RequestType.PICK_DOWN;

        Request pickUpRequest = new Request(currentFloor, pickUpRequestType);
        Request destinationRequest = new Request(currentFloor, RequestType.DESTINATION);



        if(requests.contains(pickUpRequest) || requests.contains(destinationRequest)){
            requests.remove(pickUpRequest);
            requests.remove(destinationRequest);
            if(requests.isEmpty()) {
                direction = Direction.IDLE;
            }
            return;
        }

        if(!hasRequestsAhead(direction)){
            direction =direction == Direction.UP ? Direction.DOWN : Direction.UP;
            return;
        }

        //move One Floor
        if(direction == Direction.UP){
            currentFloor++;
        }else if(direction == Direction.DOWN){
            currentFloor--;
        }
    }


    private boolean hasRequestsAhead(Direction direction){
        for(Request request: requests){
            if(direction == Direction.UP && request.getFloor()> currentFloor){
                return true;
            }
            if(direction == Direction.DOWN && request.getFloor() < currentFloor){
                return true;
            }
        }
        return false;
    }

    private Direction getDirectionForNearestRequest(){
        Request target = null;
        int distance = Integer.MAX_VALUE;
        for(Request req: requests){
            int curDistance = Math.abs(req.getFloor() - currentFloor);
            if(curDistance<distance || (distance == curDistance && (target == null ||  req.getFloor() < target.getFloor()))){
                target = req;
                distance = curDistance;
            }
        }
        return target.getRequestType() == RequestType.PICK_DOWN ? Direction.DOWN : Direction.UP;
    }

//    private void findTheNearestOne(){
//        if(requests.isEmpty()){
//            direction = Direction.IDLE;
//            return;
//        }
//        Request target =null;
//        int distance = Integer.MAX_VALUE;
//        for(Request req: requests){
//            int curDistance = Math.abs(req.getFloor() - currentFloor);
//            if(curDistance<distance || (distance == curDistance && (target == null ||  req.getFloor() < target.getFloor()))){
//                target = req;
//                distance = curDistance;
//            }
//        }
//        if(target.getFloor()>currentFloor){
//            currentFloor++;
//        }
//        if(target.getFloor()<currentFloor){
//            currentFloor--;
//        }
//        if(target.getFloor()==currentFloor){
//            requests.pop();
//        }
//    }

//    private void FIFO(){
//        if(requests.isEmpty()){
//            direction = Direction.IDLE;
//            return;
//        }
//        Request target = requests.peek();
//        if(target.getFloor()>currentFloor){
//            currentFloor++;
//        }
//        if(target.getFloor()<currentFloor){
//            currentFloor--;
//        }
//        if(target.getFloor()==currentFloor){
//            requests.pop();
//        }
//    }

    public boolean addRequest(Request request){
        if(request.getFloor()<0 || request.getFloor()>MAX_FLOOR){
            return false;
        }
        if(request.getFloor() == currentFloor){
            return false;
        }
        return requests.add(request);
    }

    public int getFloor(){
        return this.currentFloor;
    }

    public Direction getDirection(){
        return this.direction;
    }

    public boolean hasRequestsAtOrBeyond(int floor, Direction dir){
        for(Request request: requests){
            if(dir == Direction.UP && request.getFloor() >= floor) {
                // Has a stop at or above the requested floor
                if (request.getRequestType() == RequestType.PICK_UP || request.getRequestType() == RequestType.DESTINATION) {
                    return true;
                }
            }
            if(dir == Direction.DOWN && request.getFloor() <= floor){
                // Has a stop at or below the requested floor
                if(request.getRequestType() == RequestType.PICK_DOWN || request.getRequestType() == RequestType.DESTINATION){
                    return true;
                }
            }
        }
        return false;
    }
}
