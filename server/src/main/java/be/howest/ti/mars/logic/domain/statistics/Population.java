package be.howest.ti.mars.logic.domain.statistics;

import be.howest.ti.mars.logic.domain.Dome;
import be.howest.ti.mars.logic.util.Colony;

public class Population implements Statistics {

    private final int id;
    final int domeId;
    private final String date;
    private final int size;
    private final double latitude;
    private final double longitude;

    private final Colony colony;
    private final Dome dome;

    public Population(int id, int domeId, String date, int size, double latitude, double longitude, Colony colony, Dome dome) {
        this.id = id;
        this.domeId = domeId;
        this.date = date;
        this.size = size;
        this.latitude = latitude;
        this.longitude = longitude;
        this.colony = colony;
        this.dome = dome;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public int getSize() {
        return size;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Colony getColony() {
        return colony;
    }

    public Dome getDome() {
        return dome;
    }
}
