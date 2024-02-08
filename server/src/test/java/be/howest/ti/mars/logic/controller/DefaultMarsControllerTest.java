package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.util.Colony;
import be.howest.ti.mars.logic.util.DamageLevel;
import be.howest.ti.mars.logic.util.DangerLevel;
import be.howest.ti.mars.logic.util.TypeOfDispatch;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultMarsControllerTest {

    private static final String URL = "jdbc:h2:./db-18";

    @BeforeAll
    void setupTestSuite() {
        Repositories.shutdown();
        JsonObject dbProperties = new JsonObject(Map.of("url", "jdbc:h2:./db-18",
                "username", "",
                "password", "",
                "webconsole.port", 9000));
        Repositories.configure(dbProperties);
    }

    @BeforeEach
    void setupTest() {
        Repositories.getH2Repo().generateData();
    }

    @Test
    void getDomes() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        var domes = sut.getDomes();

        //Assert
        assertEquals(15, domes.size());
    }

    @Test
    void getUsers() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        var users = sut.getUsers();

        //Assert
        assertEquals(4, users.size());
    }

    @Test
    void getUserDetails() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        var user = sut.getUsers().get(0);

        //Assert
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("123 Main Street", user.getHomeAddress());
        assertEquals("none", user.getPremium());
    }

    @Test
    void getCompanies() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        var companies = sut.getCompanies();

        //Assert
        assertEquals(19, companies.size());
    }

    @Test
    void getCompany(){
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        var company = sut.getCompany(1);

        //Assert
        assertEquals("Coca Cola", company.getName());
    }

    @Test
    void getAppointments() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        var appointments = sut.getAppointments();

        //Assert
        assertEquals(2, appointments.size());
    }

    @Test
    void createAppointment() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        Map<String, String> appointmentData = Map.of(
                "date", "2021-01-01",
                "time", "12:00:00",
                "topic", "some topic",
                "employee_name", "Bob",
                "expertise", "some expertise"
        );
        var appointment = sut.createAppointment(appointmentData);

        //Assert
        assertEquals(2, appointment.getId());
        assertEquals("2021-01-01", appointment.getDate());
        assertEquals("12:00:00", appointment.getTime());
        assertEquals("some topic", appointment.getTopic());
        assertEquals("Bob", appointment.getEmployeeName());
        assertEquals("some expertise", appointment.getExpertise());
    }

    @Test
    void deleteAppointment() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        sut.deleteAppointment(1);

        //Assert
        assertThrows(NoSuchElementException.class, () -> sut.getAppointment(1));
    }

    @Test
    void getPopulation() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        var population = sut.getPopulation();

        //Assert
        assertEquals(8, population.size());
        assertEquals(0, population.get(0).getId());
        assertEquals(160, population.get(1).getSize());
        assertEquals("2021-11-30", population.get(1).getDate());
        assertEquals(-24.837979149921235, population.get(2).getLatitude());
        assertEquals(-67.1185514831543, population.get(3).getLongitude());
        assertEquals(Colony.SURFACE, population.get(3).getColony());
    }

    @Test
    void getMedicalDispatches(){
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        var medicalDispatches = sut.getMedicalDispatches();

        //Assert
        assertEquals(11, medicalDispatches.size());
        assertEquals(0, medicalDispatches.get(0).getId());
        assertEquals(TypeOfDispatch.POLICE, medicalDispatches.get(1).getDispachType());
        assertEquals(medicalDispatches.get(1).getDome(), medicalDispatches.get(2).getDome());
        assertEquals(medicalDispatches.get(1).getDome().getId(), medicalDispatches.get(2).getDome().getId());
        assertEquals(medicalDispatches.get(1).getDome().getDomeName(), medicalDispatches.get(2).getDome().getDomeName());
        assertEquals(medicalDispatches.get(1).getDome().getSurface(), medicalDispatches.get(2).getDome().getSurface());
        assertEquals("2022-01-01", medicalDispatches.get(3).getDate());
    }

    @Test
    void getMeteorShowers(){
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        var meteorShowers = sut.getMeteorShowers();

        //Assert
        assertEquals(3, meteorShowers.size());
        assertEquals(0, meteorShowers.get(0).getId());
        assertEquals("2022-01-01", meteorShowers.get(1).getDate());
        assertEquals(12, meteorShowers.get(2).getDome().getId());
        assertEquals(DamageLevel.HIGH, meteorShowers.get(2).getDamageLevel());
    }

    @Test
    void getDustStorms(){
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        var dustStorms = sut.getDustStorms();

        //Assert
        assertEquals(2, dustStorms.size());
        assertEquals(0, dustStorms.get(0).getId());
        assertEquals("2022-01-01", dustStorms.get(1).getDate());
        assertEquals(2, dustStorms.get(1).getDome().getId());
        assertEquals(DamageLevel.HIGH, dustStorms.get(1).getDamageLevel());
    }

    @Test
    void getOxygenLeaks(){
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        var oxygenLeaks = sut.getOxygenLeaks();

        //Assert
        assertEquals(79, oxygenLeaks.size());
        assertEquals(0, oxygenLeaks.get(0).getId());
        assertEquals("2022-12-05", oxygenLeaks.get(1).getDate());
        assertEquals(0, oxygenLeaks.get(2).getDome().getId());
        assertEquals(DangerLevel.LOW, oxygenLeaks.get(2).getDangerLevel());
    }
}
