class Waypoint {
    private String latitude;
    private String longitude;
    private String elevation;
    private String time;

    public Waypoint(String latitude, String longitude, String elevation, String time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
        this.time = time;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getElevation() {
        return elevation;
    }

    public String getTime() {
        return time;
    }
}