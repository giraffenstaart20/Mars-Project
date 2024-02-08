package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.exceptions.RepositoryException;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class MarsH2RepositoryExceptionsTest {

    private static final String URL = "jdbc:h2:./db-18";

    @Test
    void getH2RepoWithNoDbFails() {
        // Arrange
        Repositories.shutdown();

        // Act + Assert
        Assertions.assertThrows(RepositoryException.class, Repositories::getH2Repo);
    }

    @Test
    void functionsWithSQLExceptionFailsNicely() {
        // Arrange
        int id = 1;
        JsonObject dbProperties = new JsonObject(Map.of("url",URL,
                "username", "",
                "password", "",
                "webconsole.port", 9000 ));
        Repositories.shutdown();
        Repositories.configure(dbProperties);
        MarsH2Repository repo = Repositories.getH2Repo();
        repo.cleanUp();

        // Act + Assert
        Assertions.assertThrows(RepositoryException.class, repo::getDomes);
        Assertions.assertThrows(RepositoryException.class, repo::getUsers);
        Assertions.assertThrows(RepositoryException.class, repo::getAppointments);
        Assertions.assertThrows(RepositoryException.class, repo::getCompanies);
        Assertions.assertThrows(RepositoryException.class, () -> repo.getCompany(id));
        Assertions.assertThrows(RepositoryException.class, repo::getOxygenLeaks);
        Assertions.assertThrows(RepositoryException.class, repo::getMedicalDispatches);
        Assertions.assertThrows(RepositoryException.class, repo::getMeteorShowers);

    }


}
