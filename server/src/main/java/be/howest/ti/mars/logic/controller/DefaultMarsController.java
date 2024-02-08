package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.domain.statistics.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * DefaultMarsController is the default implementation for the MarsController interface.
 * The controller shouldn't even know that it is used in an API context..
 *
 * This class and all other classes in the logic-package (or future sub-packages)
 * should use 100% plain old Java Objects (POJOs). The use of Json, JsonObject or
 * Strings that contain encoded/json data should be avoided here.
 * Keep libraries and frameworks out of the logic packages as much as possible.
 * Do not be afraid to create your own Java classes if needed.
 */
public class DefaultMarsController implements MarsController {
    @Override
    public List<Dome> getDomes() {
        List<Dome> domes = Repositories.getH2Repo().getDomes();
        if(domes.isEmpty()) {
            throw new NoSuchElementException("No domes found!");
        }
        return domes;
    }

    @Override
    public List<User> getUsers() {
        List<User> users = Repositories.getH2Repo().getUsers();
        if (users.isEmpty()) {
            throw new NoSuchElementException("No users found!");
        }
        return users;
    }

    @Override
    public List<Company> getCompanies() {
        List<Company> companies = Repositories.getH2Repo().getCompanies();
        if (companies.isEmpty()) {
            throw new NoSuchElementException("No companies found!");
        }
        return companies;
    }

    @Override
    public Company getCompany(int companyId) {
        Company company = Repositories.getH2Repo().getCompany(companyId);
        if (null == company) {
            throw new NoSuchElementException(String.format("No company with id: %d", companyId));
        }
        return company;
    }

    @Override
    public List<OxygenLeak> getOxygenLeaks() {
        List<OxygenLeak> oxygenLeaks = Repositories.getH2Repo().getOxygenLeaks();
        if (oxygenLeaks.isEmpty()) {
            throw new NoSuchElementException("No oxygen leaks found!");
        }
        return oxygenLeaks;
    }

    @Override
    public List<Appointment> getAppointments() {
        List<Appointment> appointments = Repositories.getH2Repo().getAppointments();
        if (appointments.isEmpty()) {
            throw new NoSuchElementException("No appointments found!");
        }
        return appointments;
    }

    @Override
    public Appointment createAppointment(Map<String, String> appointment) {
        if (appointment.isEmpty()) {
            throw new IllegalArgumentException("No appointment provided!");
        }
        return Repositories.getH2Repo().insertAppointment(appointment);
    }

    @Override
    public List<Population> getPopulation() {
        List<Population> population = Repositories.getH2Repo().getPopulation();
        if (population.isEmpty()) {
            throw new NoSuchElementException("No population found!");
        }
        return population;
    }

    @Override
    public List<MedicalDispatch> getMedicalDispatches() {
        List<MedicalDispatch> medicalDispatches = Repositories.getH2Repo().getMedicalDispatches();
        if (medicalDispatches.isEmpty()) {
            throw new NoSuchElementException("No medical dispatches found!");
        }
        return medicalDispatches;
    }

    @Override
    public List<MeteorShower> getMeteorShowers() {
        List<MeteorShower> meteorShowers = Repositories.getH2Repo().getMeteorShowers();
        if (meteorShowers.isEmpty()) {
            throw new NoSuchElementException("No meteor showers found!");
        }
        return meteorShowers;
    }

    @Override
    public List<DustStorm> getDustStorms() {
        List<DustStorm> dustStorms = Repositories.getH2Repo().getDustStorms();
        if (dustStorms.isEmpty()) {
            throw new NoSuchElementException("No dust storms found!");
        }
        return dustStorms;
    }

    @Override
    public void deleteAppointment(int appointmentId) {
        if (null == Repositories.getH2Repo().getAppointment(appointmentId))
            throw new NoSuchElementException(String.format("No appointment with id: %d", appointmentId));

        Repositories.getH2Repo().deleteAppointment(appointmentId);
    }

    @Override
    public void getAppointment(int id) {
        if (null == Repositories.getH2Repo().getAppointment(id))
            throw new NoSuchElementException(String.format("No appointment with id: %d", id));
    }
}