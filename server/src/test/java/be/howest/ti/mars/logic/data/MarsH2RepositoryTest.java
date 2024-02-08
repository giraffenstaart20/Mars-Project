package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.domain.statistics.*;
import io.netty.util.internal.StringUtil;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MarsH2RepositoryTest {
    private static final String URL = "jdbc:h2:./db-18";

    @BeforeEach
    void setupTestSuite() {
        Repositories.shutdown();
        JsonObject dbProperties = new JsonObject(Map.of("url",URL,
                "username", "",
                "password", "",
                "webconsole.port", 9000 ));
        Repositories.configure(dbProperties);
    }

    @Test
    void getDomes(){

        // Act
        List<Dome> domes = Repositories.getH2Repo().getDomes();

        // Assert
        assertEquals(15, domes.size());
    }

    @Test
    void getUser(){
        // Act
        List<User> users = Repositories.getH2Repo().getUsers();

        // Assert
        Assertions.assertTrue(users.size() == 4);
    }

    @Test
    void getCompanies(){
        // Act
        List<Company> companies = Repositories.getH2Repo().getCompanies();
        System.out.println(companies.size());

        // Assert
        assertEquals(19, companies.size());
    }

    @Test
    void getCompany(){
        // Act
        Company company = Repositories.getH2Repo().getCompany(1);

        // Assert
        Assertions.assertNotNull(company);
    }

    @Test
    void getOxygenLeaks(){
        // Act
        List<OxygenLeak> oxygenLeaks = Repositories.getH2Repo().getOxygenLeaks();

        // Assert
        assertEquals(79, oxygenLeaks.size());
    }

    @Test
    void getPopulation(){
        // Act
        List<Population> population = Repositories.getH2Repo().getPopulation();
        // Assert
        assertEquals(8, population.size());
    }

    @Test
    void getAppointments(){
        // Act
        List<Appointment> appointments = Repositories.getH2Repo().getAppointments();
        // Assert
        assertEquals(2, appointments.size());
    }

    @Test
    void createAppointment(){
        // Arrange
        Map<String, String> appointmentData = Map.of(
                "date", "2021-01-01",
                "time", "12:00:00",
                "topic", "some topic",
                "employee_name", "Bob",
                "expertise", "some expertise"
        );
        // Act
        Appointment createdAppointment = Repositories.getH2Repo().insertAppointment(appointmentData);
        // Assert
        Assertions.assertNotNull(createdAppointment);
        Assertions.assertEquals("2021-01-01", createdAppointment.getDate());
        Assertions.assertEquals("12:00:00", createdAppointment.getTime());
        Assertions.assertEquals("some topic", createdAppointment.getTopic());
        Assertions.assertEquals("Bob", createdAppointment.getEmployeeName());
        Assertions.assertEquals("some expertise", createdAppointment.getExpertise());

    }

    @Test
    void deleteAppointment(){
        // Arrange
        int id = 1;
        // Act
        Repositories.getH2Repo().deleteAppointment(id);
        // Assert
        Assertions.assertNull(Repositories.getH2Repo().getAppointment(id));
    }

    @Test
    void getMedicalDispatches(){
        // Act
        List<MedicalDispatch> medicalDispatches = Repositories.getH2Repo().getMedicalDispatches();
        // Assert
        assertEquals(11, medicalDispatches.size());
    }

    @Test
    void getMeteorShowers(){
        // Act
        List<MeteorShower> meteorShowers = Repositories.getH2Repo().getMeteorShowers();
        // Assert
        assertEquals(3, meteorShowers.size());
    }

    @Test
    void getDustStorms(){
        // Act
        List<DustStorm> dustStorms = Repositories.getH2Repo().getDustStorms();
        // Assert
        assertEquals(2, dustStorms.size());
    }

}
