package be.howest.ti.mars.logic.domain.statistics;

import be.howest.ti.mars.logic.domain.Dome;
import be.howest.ti.mars.logic.util.TypeOfDispatch;

public class MedicalDispatch implements Statistics {
    private final int id;
    private final TypeOfDispatch dispachType;
    final int domeId;
    private final String date;
    private final Double longitude;
    private final Double latitude;
    private final Dome dome;

    public MedicalDispatch(int id, TypeOfDispatch dispatchType, int domeId, String date, Double longitude, Double latitude, Dome dome) {
        this.id = id;
        this.dispachType = dispatchType;
        this.domeId = domeId;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
        this.dome = dome;
    }

    public int getId() {
        return id;
    }

    public TypeOfDispatch getDispachType() {
        return dispachType;
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
