package LowLevelDesign.BookMyShow.models;

import LowLevelDesign.BookMyShow.enums.SeatType;

public abstract class Seat {
    private final String id;
    private final Double price;

    public Seat(String id, Double price) {
        this.id = id;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public Double getPrice() {
        return price;
    }

    public abstract SeatType getSeatType();
}
