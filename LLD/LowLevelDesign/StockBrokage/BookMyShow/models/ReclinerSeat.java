package LowLevelDesign.BookMyShow.models;

import LowLevelDesign.BookMyShow.enums.SeatType;

public class ReclinerSeat extends Seat {
    public ReclinerSeat(String id, Double price) {
        super(id,price);
    }

    @Override
    public SeatType getSeatType(){
        return SeatType.RECLINER;
    }
}
