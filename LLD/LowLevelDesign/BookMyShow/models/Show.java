package LowLevelDesign.BookMyShow.models;

import java.time.LocalDateTime;
import java.util.List;

public class Show {
    private final String id;
    private final Movie movie;
    private final Screen screen;
    private final Theater theater;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public Show(String id, Movie movie, Screen screen, Theater theater, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.movie = movie;
        this.screen = screen;
        this.theater = theater;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public List<Seat> getSeats(){
        return screen.getSeats();
    }

    @Override
    public String toString() {
        return "Show{" +
                "id='" + id + '\'' +
                ", movie=" + movie +
                ", screen=" + screen +
                ", theater=" + theater +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    public String getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

    public Screen getScreen() {
        return screen;
    }

    public Theater getTheater() {
        return theater;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
}
