import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class GPXInfos implements Serializable {
    private List<Waypoint> waypointList = new ArrayList<>();
    private String creator;

    public void addWaypoint(Waypoint waypoint) {
        waypointList.add(waypoint);
    }

    public int getWaypointCount() {
        return waypointList.size();
    }

    public Waypoint getWaypoint(int index) {
        return waypointList.get(index);
    }

    public List<Waypoint> getWaypoints() {
        return waypointList;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void printGpx() {
        System.out.println("Gpx Size: " + getWaypointCount());
        for (Waypoint waypoint : waypointList) {
            System.out.println("Creator: " + getCreator() +
                    " Latitude: " + waypoint.getLatitude() +
                    " Longitude: " + waypoint.getLongitude() +
                    " Elevation: " + waypoint.getElevation() +
                    " Time: " + waypoint.getTime());
        }
    }
}
