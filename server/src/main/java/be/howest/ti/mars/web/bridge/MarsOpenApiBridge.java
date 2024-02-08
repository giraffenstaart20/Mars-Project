package be.howest.ti.mars.web.bridge;

import be.howest.ti.mars.logic.controller.DefaultMarsController;
import be.howest.ti.mars.logic.controller.MarsController;
import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.domain.statistics.*;
import be.howest.ti.mars.web.exceptions.MalformedRequestException;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.openapi.RouterBuilder;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * In the MarsOpenApiBridge class you will create one handler-method per API operation.
 * The job of the "bridge" is to bridge between JSON (request and response) and Java (the controller).
 * <p>
 * For each API operation you should get the required data from the `Request` class.
 * The Request class will turn the HTTP request data into the desired Java types (int, String, Custom class,...)
 * This desired type is then passed to the controller.
 * The return value of the controller is turned to Json or another Web data type in the `Response` class.
 */
public class MarsOpenApiBridge {
    private static final Logger LOGGER = Logger.getLogger(MarsOpenApiBridge.class.getName());
    private final MarsController controller;

    public Router buildRouter(RouterBuilder routerBuilder) {
        LOGGER.log(Level.INFO, "Installing cors handlers");
        routerBuilder.rootHandler(createCorsHandler());

        LOGGER.log(Level.INFO, "Installing failure handlers for all operations");
        routerBuilder.operations().forEach(op -> op.failureHandler(this::onFailedRequest));

        LOGGER.log(Level.INFO, "Installing handler for: getDomes");
        routerBuilder.operation("getDomes").handler(this::getDomes);

        LOGGER.log(Level.INFO, "Installing handler for: getUsers");
        routerBuilder.operation("getUsers").handler(this::getUsers);

        LOGGER.log(Level.INFO, "Installing handler for: getCompanies");
        routerBuilder.operation("getCompanies").handler(this::getCompanies);

        LOGGER.log(Level.INFO, "Installing handler for: getCompany");
        routerBuilder.operation("getCompany").handler(this::getCompany);

        LOGGER.log(Level.INFO, "Installing handler for: getOxygenLeaks");
        routerBuilder.operation("getOxygenLeaks").handler(this::getOxygenLeaks);

        LOGGER.log(Level.INFO, "Installing handler for: getAppointments");
        routerBuilder.operation("getAppointments").handler(this::getAppointments);

        LOGGER.log(Level.INFO, "Installing handler for: createAppointment");
        routerBuilder.operation("createAppointment").handler(this::createAppointment);

        LOGGER.log(Level.INFO, "Installing handler for: deleteAppointment");
        routerBuilder.operation("deleteAppointment").handler(this::deleteAppointment);

        LOGGER.log(Level.INFO, "Installing handler for: getPopulation");
        routerBuilder.operation("getPopulation").handler(this::getPopulation);

        LOGGER.log(Level.INFO, "Installing handler for: getMedicalDispatches");
        routerBuilder.operation("getMedicalDispatches").handler(this::getMedicalDispatches);

        LOGGER.log(Level.INFO, "Installing handler for: getMeteorShowers");
        routerBuilder.operation("getMeteorShowers").handler(this::getMeteorShowers);

        LOGGER.log(Level.INFO, "Installing handler for: getDustStorms");
        routerBuilder.operation("getDustStorms").handler(this::getDustStorms);

        LOGGER.log(Level.INFO, "All handlers are installed, creating router.");
        return routerBuilder.createRouter();
    }

    public MarsOpenApiBridge() {
        this.controller = new DefaultMarsController();
    }

    public MarsOpenApiBridge(MarsController controller) {
        this.controller = controller;
    }


    public void getDomes(RoutingContext ctx) {
        List<Dome> domes = controller.getDomes();

        Response.sendDomes(ctx, domes);
    }

    public void getUsers(RoutingContext ctx) {
        List<User> users = controller.getUsers();

        Response.sendUsers(ctx, users);
    }

    private void getCompanies(RoutingContext routingContext) {
        List<Company> companies = controller.getCompanies();

        Response.sendCompanies(routingContext, companies);
    }

    private void getCompany(RoutingContext routingContext) {
        Company company = controller.getCompany(Request.from(routingContext).getCompanyId());

        Response.sendCompany(routingContext, company);
    }

    private void getOxygenLeaks(RoutingContext routingContext) {
        List<OxygenLeak> oxygenLeaks = controller.getOxygenLeaks();

        Response.sendOxygenLeaks(routingContext, oxygenLeaks);
    }

    private void getAppointments(RoutingContext routingContext) {
        List<Appointment> appointments = controller.getAppointments();

        Response.sendAppointments(routingContext, appointments);
    }

    private void createAppointment(RoutingContext routingContext) {
        Map<String, String> appointment = Request.from(routingContext).getAppointment();

        Response.sendAppointmentCreated(routingContext, controller.createAppointment(appointment));
    }

    private void deleteAppointment(RoutingContext routingContext) {
        int appointmentId = Request.from(routingContext).getAppointmentId();

        controller.deleteAppointment(appointmentId);

        Response.sendAppointmentDeleted(routingContext);
    }

    private void getPopulation(RoutingContext routingContext) {
        List<Population> population = controller.getPopulation();

        Response.sendPopulation(routingContext, population);
    }

    private void getMedicalDispatches(RoutingContext routingContext) {
        List<MedicalDispatch> medicalDispatches = controller.getMedicalDispatches();

        Response.sendMedicalDispatches(routingContext, medicalDispatches);
    }

    private void getMeteorShowers(RoutingContext routingContext) {
        List<MeteorShower> meteorShowers = controller.getMeteorShowers();

        Response.sendMeteorShowers(routingContext, meteorShowers);
    }

    private void getDustStorms(RoutingContext routingContext) {
        List<DustStorm> dustStorms = controller.getDustStorms();

        Response.sendDustStorms(routingContext, dustStorms);
    }

    private void onFailedRequest(RoutingContext ctx) {
        Throwable cause = ctx.failure();
        int code = ctx.statusCode();
        String quote = Objects.isNull(cause) ? "" + code : cause.getMessage();

        // Map custom runtime exceptions to a HTTP status code.
        LOGGER.log(Level.INFO, "Failed request", cause);
        if (cause instanceof IllegalArgumentException) {
            code = 400;
        } else if (cause instanceof MalformedRequestException) {
            code = 400;
        } else if (cause instanceof NoSuchElementException) {
            code = 404;
        } else {
            LOGGER.log(Level.WARNING, "Failed request", cause);
        }

        Response.sendFailure(ctx, code, quote);
    }

    private CorsHandler createCorsHandler() {
        return CorsHandler.create(".*.")
                .allowedHeader("x-requested-with")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("Access-Control-Allow-Credentials")
                .allowCredentials(true)
                .allowedHeader("origin")
                .allowedHeader("Content-Type")
                .allowedHeader("Authorization")
                .allowedHeader("accept")
                .allowedMethod(HttpMethod.HEAD)
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedMethod(HttpMethod.PATCH)
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.PUT);
    }
}
