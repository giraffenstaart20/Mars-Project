package be.howest.ti.mars.logic.domain;

import java.util.Objects;

public class Dome {

    private final int id;
    private final String domeName;
    private final double latitude;
    private final double longitude;
    private final double surface;

    public Dome(int id, String domeName, double latitude, double longitude, double surface) {
        this.id = id;
        this.domeName = domeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.surface = surface;
    }

    public int getId() {
        return id;
    }

    public String getDomeName() {
        return domeName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getSurface() {
        return surface;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dome dome = (Dome) o;
        return id == dome.id && Double.compare(dome.latitude, latitude) == 0 && Double.compare(dome.longitude, longitude) == 0 && Double.compare(dome.surface, surface) == 0 && Objects.equals(domeName, dome.domeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, domeName, latitude, longitude, surface);
    }
}
