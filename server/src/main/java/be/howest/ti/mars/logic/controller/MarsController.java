package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.domain.statistics.*;


import java.util.List;
import java.util.Map;

public interface MarsController {

    List<Dome> getDomes();

    List<User> getUsers();

    List<Company> getCompanies();

    Company getCompany(int companyId);

    List<OxygenLeak> getOxygenLeaks();

    List<Appointment> getAppointments();

    Appointment createAppointment(Map<String, String> appointment);

    List<Population> getPopulation();

    List<MedicalDispatch> getMedicalDispatches();

    List<MeteorShower> getMeteorShowers();

    List<DustStorm> getDustStorms();

    void deleteAppointment(int appointmentId);

    void getAppointment(int id);
}
