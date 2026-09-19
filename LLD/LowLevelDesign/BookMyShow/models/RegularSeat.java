package LowLevelDesign.BookMyShow.models;

import LowLevelDesign.BookMyShow.enums.SeatType;

public class RegularSeat extends Seat {
    public RegularSeat(String id, Double price) {
        super(id,price);
    }

    @Override
    public SeatType getSeatType(){
        return SeatType.REGULAR;
    }
}



