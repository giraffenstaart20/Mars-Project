package be.howest.ti.mars.logic.domain;

public class Appointment {
    private final int id;
    private final String date;
    private final String time;
    private final String topic;
    private final String  employeeName;
    private final String expertise;

    public Appointment(int id, String date, String time, String topic, String  employeeName, String expertise) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.topic = topic;
        this.employeeName = employeeName;
        this.expertise = expertise;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getTopic() {
        return topic;
    }

    public String  getEmployeeName() {
        return employeeName;
    }

    public String getExpertise() {
        return expertise;
    }
}