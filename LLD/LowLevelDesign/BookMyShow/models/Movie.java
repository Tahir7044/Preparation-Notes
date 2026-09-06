package LowLevelDesign.BookMyShow.models;

public class Movie {
    private final String id;
    private final String name;
    private final int duration;

    public Movie(String id, String name, int duration) {
        this.id = id;
        this.name = name;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }
}
