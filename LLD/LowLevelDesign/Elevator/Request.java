package LowLevelDesign.Elevator;

import java.util.Objects;

public class Request {
    int floor;
    RequestType type;

    public Request(int floor, RequestType type){
        this.floor = floor;
        this.type = type;
    }

    public int getFloor(){
        return this.floor;
    }

    public RequestType getRequestType(){
        return this.type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Request)) return false;
        Request r = (Request) o;
        return floor == r.floor && type == r.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(floor, type);
    }
}
