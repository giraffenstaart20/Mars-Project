package be.howest.ti.mars.logic.domain.statistics;

import be.howest.ti.mars.logic.domain.Dome;
import be.howest.ti.mars.logic.util.DangerLevel;

public class OxygenLeak implements Statistics {
    private final int id;
    private final DangerLevel dangerLevel;
    final int domeId;
    private final String date;

    private final Double longitude;
    private final Double latitude;
    private final Dome dome;

    public OxygenLeak(int id, DangerLevel dangerLevel, int domeId, String date, Double longitude, Double latitude, Dome dome) {
        this.id = id;
        this.dangerLevel = dangerLevel;
        this.domeId = domeId;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
        this.dome = dome;
    }

    public int getId() {
        return id;
    }

    public DangerLevel getDangerLevel() {
        return dangerLevel;
    }


    public String getDate() {
        return date;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Dome getDome() {
        return dome;
    }
}