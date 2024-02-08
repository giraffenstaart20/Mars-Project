package be.howest.ti.mars.logic.domain.statistics;

import be.howest.ti.mars.logic.domain.Dome;
import be.howest.ti.mars.logic.util.DamageLevel;

public class MeteorShower implements Statistics {
    private final int id;
    final int domeId;
    private final DamageLevel damageLevel;
    private final String date;
    private final Double longitude;
    private final Double latitude;
    private final Dome dome;

    public MeteorShower(int id, int domeId, DamageLevel damageLevel, String date, Double longitude, Double latitude, Dome dome) {
        this.id = id;
        this.domeId = domeId;
        this.damageLevel = damageLevel;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
        this.dome = dome;
    }
    @Override
    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public DamageLevel getDamageLevel() {
        return damageLevel;
    }

    @Override
    public Double getLongitude() {
        return longitude;
    }

    @Override
    public Double getLatitude() {
        return latitude;
    }

    @Override
    public Dome getDome() {
        return dome;
    }
}
