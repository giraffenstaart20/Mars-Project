package be.howest.ti.mars.web;

import be.howest.ti.mars.logic.controller.MockMarsController;
import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.web.bridge.MarsOpenApiBridge;
import be.howest.ti.mars.web.bridge.MarsRtcBridge;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(VertxExtension.class)
@SuppressWarnings({"PMD.JUnitTestsShouldIncludeAssert","PMD.AvoidDuplicateLiterals"})
/*
 * PMD.JUnitTestsShouldIncludeAssert: VertxExtension style asserts are marked as false positives.
 * PMD.AvoidDuplicateLiterals: Should all be part of the spec (e.g., urls and names of req/res body properties, ...)
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OpenAPITest {

    private static final int PORT = 8080;
    private static final String HOST = "localhost";
    public static final String MSG_200_EXPECTED = "If all goes right, we expect a 200 status";
    public static final String MSG_201_EXPECTED = "If a resource is successfully created.";
    public static final String MSG_204_EXPECTED = "If a resource is successfully deleted";
    private Vertx vertx;
    private WebClient webClient;

    @BeforeAll
    void deploy(final VertxTestContext testContext) {
        Repositories.shutdown();
        vertx = Vertx.vertx();

        WebServer webServer = new WebServer(new MarsOpenApiBridge(new MockMarsController()), new MarsRtcBridge());
        vertx.deployVerticle(
                webServer,
                testContext.succeedingThenComplete()
        );
        webClient = WebClient.create(vertx);
    }

    @AfterAll
    void close(final VertxTestContext testContext) {
        vertx.close(testContext.succeedingThenComplete());
        webClient.close();
        Repositories.shutdown();
    }
    @Test
    void getDomes(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/domes").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            response.bodyAsJsonObject().getJsonArray("domes").size() > 0,
                            "No domes found"
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getUsers(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/users").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            response.bodyAsJsonObject().getJsonArray("users").size() > 0,
                            "No users found"
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getCompanies(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/companies").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            response.bodyAsJsonObject().getJsonArray("companies").size() > 0,
                            "No companies found"
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getCompany(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/company/1").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("name")),
                            "No company found"
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getCompanyWithInvalidID(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/company/333").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("name")),
                            "No company found"
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getOxygenLeaks(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/oxygenLeaks").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            response.bodyAsJsonObject().getJsonArray("oxygenLeaks").size() > 0,
                            "No oxygen leaks found"
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getPopulation(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/population").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            response.bodyAsJsonObject().getJsonArray("population").size() > 0,
                            "No population found"
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getAppointments(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/appointments").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            response.bodyAsJsonObject().getJsonArray("appointments").size() > 0,
                            "No appointments found"
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void createAppointment(final VertxTestContext testContext) {
        Map<String, String> testAppointment = Map.of(
                "date", "2021-01-01",
                "time", "12:00:00",
                "topic", "some topic",
                "employee_name", "Bob",
                "expertise", "some expertise"
        );
        webClient.post(PORT, HOST, "/api/appointment").sendJsonObject(createAppointment(testAppointment))
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(201, response.statusCode(), MSG_201_EXPECTED);
                    assertEquals(
                            testAppointment.get("date"),
                            response.bodyAsJsonObject().getString("date"),
                            "Appointment does not match " + testAppointment);
                    testContext.completeNow();
                }));
    }

    private JsonObject createAppointment(Map<String, String> appointmentData) {
        return new JsonObject().put("date", appointmentData.get("date"))
                .put("time", appointmentData.get("time"))
                .put("topic", appointmentData.get("topic"))
                .put("employee_name", appointmentData.get("employee_name"))
                .put("expertise", appointmentData.get("expertise"));
    }

    @Test
    void deleteAppointment(final VertxTestContext testContext) {
        webClient.delete(PORT, HOST, "/api/appointment/1").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(204, response.statusCode(), MSG_204_EXPECTED);
                    testContext.completeNow();
                }));
    }

    @Test
    void getMedicalDispatches(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/medicalDispatches").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            response.bodyAsJsonObject().getJsonArray("medicalDispatches").size() > 0,
                            "No medical dispatches found"
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getMeteorShowers(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/meteorShowers").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            response.bodyAsJsonObject().getJsonArray("meteorShowers").size() > 0,
                            "No meteor showers found"
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getDustStorms(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/dustStorms").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            response.bodyAsJsonObject().getJsonArray("dustStorms").size() > 0,
                            "No dust storms found"
                    );
                    testContext.completeNow();
                }));
    }

}