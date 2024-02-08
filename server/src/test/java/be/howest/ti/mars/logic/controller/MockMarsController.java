package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.domain.Appointment;
import be.howest.ti.mars.logic.domain.statistics.*;
import be.howest.ti.mars.logic.util.Colony;
import be.howest.ti.mars.logic.util.DamageLevel;
import be.howest.ti.mars.logic.util.DangerLevel;
import be.howest.ti.mars.logic.util.TypeOfDispatch;

import java.util.List;
import java.util.Map;

public class MockMarsController implements MarsController {
    @Override
    public List<Dome> getDomes() {
        double surface = 40.3;
        List<Dome> domes = List.of(
                new Dome(1, "Dome 1", 1.0, 1.0, surface),
                new Dome(2, "Dome 2", 2.0, 2.0, surface),
                new Dome(3, "Dome 3", 3.0, 3.0, surface));
        return domes;
    }

    @Override
    public List<User> getUsers() {
        List<User> users = List.of(
                new User(1, "joe", "mama", "123 secondstreet", "none"),
                new User(2, "bob", "mama", "123 secondstreet", "standard"),
                new User(3, "rob", "mama", "123 secondstreet", "premium"));
        return users;
    }

    @Override
    public List<Company> getCompanies() {
        List<Company> companies = List.of(
                new Company(1, "company 1", "123 secondstreet", 20, 1),
                new Company(2, "company 2", "123 secondstreet", 40, 2),
                new Company(3, "company 3", "123 secondstreet", 60, 3)
        );
        return companies;
    }

    @Override
    public Company getCompany(int companyId) {
        return new Company(companyId, "company " + companyId, "123 secondstreet", 20, 1);
    }

    @Override
    public List<OxygenLeak> getOxygenLeaks() {
        double surface = 40.3;
        List<OxygenLeak> oxygenLeaks = List.of(
                new OxygenLeak(1, DangerLevel.HIGH,1, "2022-01-01", 2.0, 3.2, new Dome(1, "Dome 1", 1.0, 1.0, surface)),
                new OxygenLeak(2, DangerLevel.MEDIUM,2, "2022-01-01", 6.366, 78.3214, new Dome(1, "Dome 1", 1.0, 1.0, surface)),
                new OxygenLeak(3, DangerLevel.LOW,3, "2022-01-01", -63.666, 47.635, new Dome(1, "Dome 1", 1.0, 1.0, surface))
        );
        return oxygenLeaks;
    }
    @Override
    public List<Appointment> getAppointments() {
        List<Appointment> appointments = List.of(
                new Appointment(1, "2022-01-01", "15:00:00", "location", "2", "sales"),
                new Appointment(2, "2022-01-01", "15:00:00", "location", "6", "developer"),
                new Appointment(3, "2022-01-01", "15:00:00", "location", "3", "sales"),
                new Appointment(4, "2022-01-01", "15:00:00", "location", "4", "sales")
                );
        return appointments;
    }

    @Override
    public Appointment createAppointment(Map<String, String> appointment) {
        return new Appointment(1, appointment.get("date"), appointment.get("time"), appointment.get("topic"), appointment.get("employeeName"), appointment.get("expertise"));
    }

    @Override
    public List<Population> getPopulation() {
        int domeId = 1;
        String date = "2022-01-01";
        Dome dome = new Dome(domeId, "Dome 1", 1.0, 1.0, 40.3);
        List<Population> population = List.of(
                new Population(1, domeId, date, 34, 100, -951, Colony.MINE, dome),
                new Population(2, domeId, date, 60, 100, -951, Colony.SURFACE, dome),
                new Population(3, domeId, date, 34, 100, -951, Colony.MINE, dome),
                new Population(4, domeId, date, 34, 100, -951, Colony.SPACESTATION, dome),
                new Population(5, domeId, date, 34, 100, -951, Colony.MINE, dome),
                new Population(6, domeId, date, 34, 100, -951, Colony.SURFACE, dome)
        );
        return population;

    }

    @Override
    public List<MedicalDispatch> getMedicalDispatches() {
        int domeId = 1;
        Dome dome = new Dome(domeId, "Dome 1", 1.0, 1.0, 40.3);
        List<MedicalDispatch> medicalDispatches = List.of(
                new MedicalDispatch(1, TypeOfDispatch.AMBULANCE, domeId, "2022-01-01", 2.0, 3.2, dome),
                new MedicalDispatch(2, TypeOfDispatch.POLICE, domeId, "2022-01-01", 6.366, 78.3214, dome),
                new MedicalDispatch(3, TypeOfDispatch.FIRE_DEPARTMENT, domeId, "2022-01-01", -63.666, 47.635, dome),
                new MedicalDispatch(4, TypeOfDispatch.AMBULANCE, domeId, "2022-01-01", 2.0, 3.2, dome),
                new MedicalDispatch(5, TypeOfDispatch.POLICE, domeId, "2022-01-01", 6.366, 78.3214, dome)
        );
        return medicalDispatches;
    }

    @Override
    public List<MeteorShower> getMeteorShowers() {
        int domeId = 1;
        Dome dome = new Dome(domeId, "Dome 1", 1.0, 1.0, 40.3);
        List<MeteorShower> meteorShowers = List.of(
                new MeteorShower(1,domeId, DamageLevel.LOW, "2022-12-12", 3.2,2.0, dome),
                new MeteorShower(2,domeId, DamageLevel.MEDIUM, "2022-12-12", 3.2,2.0, dome),
                new MeteorShower(3,domeId, DamageLevel.HIGH, "2022-12-12", 3.2,2.0, dome)
        );
        return meteorShowers;
    }

    @Override
    public List<DustStorm> getDustStorms() {
        int domeId = 1;
        Dome dome = new Dome(domeId, "Dome 1", 1.0, 1.0, 40.3);
        List<DustStorm> dustStorms = List.of(
                new DustStorm(1,domeId, DamageLevel.LOW, "2022-12-12", 3.2,2.0, dome),
                new DustStorm(2,domeId, DamageLevel.MEDIUM, "2022-12-12", 3.2,2.0, dome),
                new DustStorm(3,domeId, DamageLevel.HIGH, "2022-12-12", 3.2,2.0, dome)
        );
        return dustStorms;
    }

    @Override
    public void deleteAppointment(int appointmentId) {
        System.out.println("Appointment with id " + appointmentId + " deleted");
    }

    @Override
    public void getAppointment(int id) {
        System.out.println("Appointment with id " + id + " deleted");
    }

}
