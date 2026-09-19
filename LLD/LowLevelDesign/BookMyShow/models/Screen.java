package LowLevelDesign.BookMyShow.models;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Screen {
    private final String id;
    private final Map<String, Seat> seats;

    public Screen(String id) {
        this.id = id;
        seats = new HashMap<>();
    }

    public void addSeat(Seat seat){
        seats.put(seat.getId(), seat);
    }

//    public Seat getSeat(String seatId) {
//        return seats.get(seatId);
//    }

    public String getId() {
        return id;
    }

    public List<Seat> getSeats() {
        return new ArrayList<>(seats.values());
    }
}
