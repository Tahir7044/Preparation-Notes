package LowLevelDesign.BookMyShow.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Theater {
    private final String id;
    private final String name;
    private final Map<String, Screen> screens;

    public Theater(String id, String name) {
        this.id = id;
        this.name = name;
        screens = new HashMap<>();
    }

    public void addScreen(Screen screen){
        screens.put(screen.getId(), screen);
    }

    public Screen getScreen(String screenId) {
        return screens.get(screenId);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Screen> getScreens() {
        return new ArrayList<>(screens.values());
    }
}
