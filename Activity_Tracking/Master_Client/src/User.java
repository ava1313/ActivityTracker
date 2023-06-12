import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private List<Waypoint> waypoints;

    public User(String name) {
        this.name = name;
        this.waypoints = new ArrayList<>();
    }

    public void addWaypoint(Waypoint waypoint) {
        waypoints.add(waypoint);
    }

    public String getName() {
        return name;
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }
}