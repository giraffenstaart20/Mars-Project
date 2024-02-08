package be.howest.ti.mars.web.bridge;

import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.domain.statistics.*;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.util.ArrayList;
import java.util.List;

/**
 * The Response class is responsible for translating the result of the controller into
 * JSON responses with an appropriate HTTP code.
 */
public class Response {

    private Response() {
    }

    private static void sendOkJsonResponse(RoutingContext ctx, JsonObject response) {
        sendJsonResponse(ctx, 200, response);
    }

    private static void sendEmptyResponse(RoutingContext ctx, int statusCode) {
        ctx.response()
                .setStatusCode(statusCode)
                .end();
    }

    private static void sendJsonResponse(RoutingContext ctx, int statusCode, Object response) {
        ctx.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setStatusCode(statusCode)
                .end(Json.encodePrettily(response));
    }

    public static void sendFailure(RoutingContext ctx, int code, String quote) {
        sendJsonResponse(ctx, code, new JsonObject()
                .put("failure", code)
                .put("cause", quote));
    }

    public static void sendDomes(RoutingContext ctx, List<Dome> domes) {
        List<JsonObject> domeJsons = new ArrayList<>();
        for (Dome dome : domes) {
            domeJsons.add(JsonObject.mapFrom(dome));
        }
        sendOkJsonResponse(ctx, new JsonObject().put("domes", domeJsons));
    }

    public static void sendUsers(RoutingContext ctx, List<User> users) {
        List<JsonObject> userJsons = new ArrayList<>();
        for (User user : users) {
            userJsons.add(JsonObject.mapFrom(user));
        }
        sendOkJsonResponse(ctx, new JsonObject().put("users", userJsons));
    }

    public static void sendCompanies(RoutingContext routingContext, List<Company> companies) {
        List<JsonObject> companyJsons = new ArrayList<>();
        for (Company company : companies) {
            companyJsons.add(JsonObject.mapFrom(company));
        }
        sendOkJsonResponse(routingContext, new JsonObject().put("companies", companyJsons));
    }

    public static void sendCompany(RoutingContext routingContext, Company company) {
        sendOkJsonResponse(routingContext, JsonObject.mapFrom(company));
    }

    public static void sendOxygenLeaks(RoutingContext routingContext, List<OxygenLeak> oxygenLeaks) {
        List<JsonObject> oxygenLeakJsons = new ArrayList<>();
        for (OxygenLeak oxygenLeak : oxygenLeaks) {
            oxygenLeakJsons.add(JsonObject.mapFrom(oxygenLeak));
        }
        sendOkJsonResponse(routingContext, new JsonObject().put("oxygenLeaks", oxygenLeakJsons));
    }

    public static void sendAppointments(RoutingContext routingContext, List<Appointment> appointments) {
        List<JsonObject> appointmentJsons = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentJsons.add(JsonObject.mapFrom(appointment));
        }
        sendOkJsonResponse(routingContext, new JsonObject().put("appointments", appointmentJsons));
    }

    public static void sendAppointmentCreated(RoutingContext routingContext, Appointment appointment) {
        sendJsonResponse(routingContext, 201, JsonObject.mapFrom(appointment));
    }

    public static void sendPopulation(RoutingContext routingContext, List<Population> population) {
        List<JsonObject> populationJsons = new ArrayList<>();
        for (Population pop : population) {
            populationJsons.add(JsonObject.mapFrom(pop));
        }
        sendOkJsonResponse(routingContext, new JsonObject().put("population", populationJsons));
    }

    public static void sendMedicalDispatches(RoutingContext routingContext, List<MedicalDispatch> medicalDispatches) {
        List<JsonObject> medicalDispatchJsons = new ArrayList<>();
        for (MedicalDispatch medicalDispatch : medicalDispatches) {
            medicalDispatchJsons.add(JsonObject.mapFrom(medicalDispatch));
        }
        sendOkJsonResponse(routingContext, new JsonObject().put("medicalDispatches", medicalDispatchJsons));
    }

    public static void sendMeteorShowers(RoutingContext routingContext, List<MeteorShower> meteorShowers) {
        List<JsonObject> meteorShowerJsons = new ArrayList<>();
        for (MeteorShower meteorShower : meteorShowers) {
            meteorShowerJsons.add(JsonObject.mapFrom(meteorShower));
        }
        sendOkJsonResponse(routingContext, new JsonObject().put("meteorShowers", meteorShowerJsons));
    }

    public static void sendDustStorms(RoutingContext routingContext, List<DustStorm> dustStorms) {
        List<JsonObject> dustStormJsons = new ArrayList<>();
        for (DustStorm dustStorm : dustStorms) {
            dustStormJsons.add(JsonObject.mapFrom(dustStorm));
        }
        sendOkJsonResponse(routingContext, new JsonObject().put("dustStorms", dustStormJsons));
    }

    public static void sendAppointmentDeleted(RoutingContext routingContext) {
        sendEmptyResponse(routingContext, 204);
    }
}
