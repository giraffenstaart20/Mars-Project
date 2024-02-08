package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.domain.statistics.*;
import be.howest.ti.mars.logic.exceptions.RepositoryException;
import be.howest.ti.mars.logic.util.Colony;
import be.howest.ti.mars.logic.util.DamageLevel;
import be.howest.ti.mars.logic.util.DangerLevel;
import be.howest.ti.mars.logic.util.TypeOfDispatch;
import org.h2.tools.Server;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
This is only a starter class to use an H2 database.
In this start project there was no need for a Java interface MarsRepository.
Please always use interfaces when needed.

To make this class useful, please complete it with the topics seen in the module OOA & SD
 */

public class MarsH2Repository {
    private static final Logger LOGGER = Logger.getLogger(MarsH2Repository.class.getName());
    private static final String SQL_ALL_DOMES = "select id, domename, latitude, longitude, surface from domes;";
    private static final String SQL_ALL_USERS = "select id, firstName, lastName, homeAddress, premium role from users;";
    private static final String SQL_ALL_COMPANIES = "select id, name, section, ad_effectiveness, user_id from companies;";
    private static final String SQL_COMPANY_BY_ID = "select id, name, section, ad_effectiveness, user_id from companies where user_id = ?;";
    private static final String SQL_ALL_OXYGENLEAKS = "select * from oxygen_leaks o join domes d on o.dome_id = d.id;";
    private static final String SQL_ALL_APPOINTMENTS = "select id, date, time, topic, employee_name, expertise from appointments;";
    private static final String ID = "id";
    private static final String TOPIC = "topic";
    private static final String EXPERTISE = "expertise";

    private static final String SQL_INSERT_APPOINTMENT = "insert into appointments (`date`, `time`, `topic`, `employee_name`, `expertise`) values (?, ?, ?, ?, ?);";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    private static final String SQL_ALL_POPULATIONS = "select * from population p join domes d on p.dome_id = d.id;";
    public static final String OXYGEN_LEAKS_ID = "oxygen_leaks.id";
    public static final String OXYGEN_LEAKS_DOME_ID = "oxygen_leaks.dome_id";
    public static final String OXYGEN_LEAKS_DATE = "oxygen_leaks.date";
    public static final String OXYGEN_LEAKS_LATITUDE = "oxygen_leaks.latitude";
    public static final String OXYGEN_LEAKS_LONGITUDE = "oxygen_leaks.longitude";
    public static final String DOMES_ID = "domes.id";
    public static final String DOMES_DOMENAME = "domes.domename";
    public static final String DOMES_LATITUDE = "domes.latitude";
    public static final String DOMES_LONGITUDE = "domes.longitude";
    public static final String DOMES_SURFACE = "domes.surface";
    public static final String POPULATION_ID = "population.id";
    public static final String POPULATION_DOME_ID = "population.dome_id";
    public static final String POPULATION_SIZE = "population.size";
    public static final String POPULATION_LATITUDE = "population.latitude";
    public static final String POPULATION_LONGITUDE = "population.longitude";
    private static final String SQL_ALL_MEDICAL_DISPATCHES = "select * from medical_dispatches m join domes d on m.dome_id = d.id;";
    private static final String MEDICAL_DISPATCHES_ID = "medical_dispatches.id";
    private static final String MEDICAL_DISPATCHES_DOME_ID = "medical_dispatches.dome_id";
    private static final String MEDICAL_DISPATCHES_LATITUDE = "medical_dispatches.latitude";
    private static final String MEDICAL_DISPATCHES_LONGITUDE = "medical_dispatches.longitude";
    public static final String MEDICAL_DISPATCHES_DATE = "medical_dispatches.date";
    private static final String SQL_ALL_METEOR_SHOWERS = "select * from meteor_showers m join domes d on m.dome_id = d.id;";
    private static final String METEOR_SHOWERS_ID = "meteor_showers.id";
    private static final String METEOR_SHOWERS_DOME_ID = "meteor_showers.dome_id";
    private static final String  METEOR_SHOWERS_DATE = "meteor_showers.date";
    private static final String METEOR_SHOWERS_LONGITUDE = "meteor_showers.longitude";
    private static final String METEOR_SHOWERS_LATITUDE = "meteor_showers.latitude";
    private static final String SQL_ALL_DUST_STORMS = "select * from dust_storms d join domes dom on d.dome_id = dom.id;";
    private static final String DUST_STORMS_ID = "dust_storms.id";
    private static final String DUST_STORMS_DOME_ID = "dust_storms.dome_id";
    private static final String DUST_STORMS_DATE = "dust_storms.date";
    private static final String DUST_STORMS_LONGITUDE = "dust_storms.longitude";
    private static final String DUST_STORMS_LATITUDE = "dust_storms.latitude";
    private static final String SQL_DELETE_APPOINTMENT = "delete from appointments where id = ?;";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String EMPLOYEE_NAME = "employee_name";
    private static final String SQL_APPOINTMENT_BY_ID = "select * from appointments where id = ?;";
    public static final String POPULATION_DATE = "population.date";
    private final Server dbWebConsole;
    private final String username;
    private final String password;
    private final String url;

    public MarsH2Repository(String url, String username, String password, int console) {
        try {
            this.username = username;
            this.password = password;
            this.url = url;
            this.dbWebConsole = Server.createWebServer(
                    "-ifNotExists",
                    "-webPort", String.valueOf(console)).start();
            LOGGER.log(Level.INFO, "Database web console started on port: {0}", console);
            this.generateData();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "DB configuration failed", ex);
            throw new RepositoryException("Could not configure MarsH2repository");
        }
    }

    public void cleanUp() {
        if (dbWebConsole != null && dbWebConsole.isRunning(false))
            dbWebConsole.stop();

        try {
            Files.deleteIfExists(Path.of("./db-18.mv.db"));
            Files.deleteIfExists(Path.of("./db-18.trace.db"));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Database cleanup failed.", e);
            throw new RepositoryException("Database cleanup failed.");
        }
    }

    public void generateData() {
        try {
            executeScript("db-create.sql");
            executeScript("db-populate.sql");
        } catch (IOException | SQLException ex) {
            LOGGER.log(Level.SEVERE, "Execution of database scripts failed.", ex);
        }
    }

    private void executeScript(String fileName) throws IOException, SQLException {
        String createDbSql = readFile(fileName);
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(createDbSql);
        ) {
            stmt.executeUpdate();
        }
    }

    private String readFile(String fileName) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null)
            throw new RepositoryException("Could not read file: " + fileName);

        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public List<Dome> getDomes() {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_ALL_DOMES)
        ) {
            try (ResultSet rs = stmt.executeQuery()) {
                List<Dome> domes = new ArrayList<>();
                while (rs.next()) {
                    domes.add(new Dome(rs.getInt(ID), rs.getString("domename"), rs.getDouble(LATITUDE), rs.getDouble(LONGITUDE), rs.getDouble("surface")));
                }
                return domes;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get domes.", ex);
            throw new RepositoryException("Could not get domes.");
        }
    }

    public List<User> getUsers() {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_ALL_USERS)
        ) {
            try (ResultSet rs = stmt.executeQuery()) {
                List<User> users = new ArrayList<>();
                while (rs.next()) {
                    users.add(new User(rs.getInt(ID), rs.getString("firstname"), rs.getString("lastname"), rs.getString("homeAddress"), rs.getString("premium")));
                }
                return users;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get users.", ex);
            throw new RepositoryException("Could not get users.");
        }
    }

    public List<Company> getCompanies() {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_ALL_COMPANIES)
        ) {
            try (ResultSet rs = stmt.executeQuery()) {
                List<Company> companies = new ArrayList<>();
                while (rs.next()) {
                    companies.add(new Company(rs.getInt(ID), rs.getString("name"), rs.getString("section"), rs.getInt("ad_effectiveness"), rs.getInt("user_Id")));
                }
                return companies;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get companies.", ex);
            throw new RepositoryException("Could not get companies.");
        }
    }

    public Company getCompany(int userId) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_COMPANY_BY_ID)
        ) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Company(rs.getInt(ID), rs.getString("name"), rs.getString("section"), rs.getInt("ad_effectiveness"), rs.getInt("user_Id"));
                } else {
                    throw new RepositoryException("Could not find company with user id: " + userId);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get company.", ex);
            throw new RepositoryException("Could not get company.");
        }
    }

    public List<OxygenLeak> getOxygenLeaks() {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_ALL_OXYGENLEAKS)
        ) {
            try (ResultSet rs = stmt.executeQuery()) {
                List<OxygenLeak> oxygenLeaks = new ArrayList<>();
                while (rs.next()) {
                    String dangerLevel = rs.getString("danger_level");
                    if (dangerLevel.equals("low")) {
                        oxygenLeaks.add(new OxygenLeak(rs.getInt(OXYGEN_LEAKS_ID), DangerLevel.LOW, rs.getInt(OXYGEN_LEAKS_DOME_ID), rs.getDate(OXYGEN_LEAKS_DATE).toString(), rs.getDouble(OXYGEN_LEAKS_LATITUDE), rs.getDouble(OXYGEN_LEAKS_LONGITUDE), new Dome(rs.getInt(DOMES_ID), rs.getString(DOMES_DOMENAME), rs.getDouble(DOMES_LATITUDE), rs.getDouble(DOMES_LONGITUDE), rs.getDouble(DOMES_SURFACE))));
                    } else if (dangerLevel.equals("medium")) {
                        oxygenLeaks.add(new OxygenLeak(rs.getInt(OXYGEN_LEAKS_ID), DangerLevel.MEDIUM, rs.getInt(OXYGEN_LEAKS_DOME_ID), rs.getDate(OXYGEN_LEAKS_DATE).toString(), rs.getDouble(OXYGEN_LEAKS_LATITUDE), rs.getDouble(OXYGEN_LEAKS_LONGITUDE),  new Dome(rs.getInt(DOMES_ID), rs.getString(DOMES_DOMENAME), rs.getDouble(DOMES_LATITUDE), rs.getDouble(DOMES_LONGITUDE), rs.getDouble(DOMES_SURFACE))));
                    } else if (dangerLevel.equals("high")) {
                        oxygenLeaks.add(new OxygenLeak(rs.getInt(OXYGEN_LEAKS_ID), DangerLevel.HIGH, rs.getInt(OXYGEN_LEAKS_DOME_ID), rs.getDate(OXYGEN_LEAKS_DATE).toString(), rs.getDouble(OXYGEN_LEAKS_LATITUDE), rs.getDouble(OXYGEN_LEAKS_LONGITUDE),  new Dome(rs.getInt(DOMES_ID), rs.getString(DOMES_DOMENAME), rs.getDouble(DOMES_LATITUDE), rs.getDouble(DOMES_LONGITUDE), rs.getDouble(DOMES_SURFACE))));
                    }
                }
                return oxygenLeaks;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get oxygen leaks.", ex);
            throw new RepositoryException("Could not get oxygen leaks.");
        }
    }

    public List<Appointment> getAppointments() {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_ALL_APPOINTMENTS)
        ) {
            try (ResultSet rs = stmt.executeQuery()) {
                List<Appointment> appointments = new ArrayList<>();
                while (rs.next()) {
                    appointments.add(new Appointment(rs.getInt(ID), rs.getDate(DATE).toString(), rs.getString(TIME), rs.getString(TOPIC), rs.getString(EMPLOYEE_NAME), rs.getString(EXPERTISE)));
                }
                return appointments;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get appointments.", ex);
            throw new RepositoryException("Could not get appointments.");
        }
    }

    public Appointment insertAppointment(Map<String, String> appointment) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_APPOINTMENT, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, appointment.get(DATE));
            stmt.setString(2, appointment.get(TIME));
            stmt.setString(3, appointment.get(TOPIC));
            stmt.setString(4, appointment.get(EMPLOYEE_NAME));
            stmt.setString(5, appointment.get(EXPERTISE));
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Appointment(rs.getInt(1), appointment.get(DATE), appointment.get(TIME), appointment.get(TOPIC), appointment.get(EMPLOYEE_NAME), appointment.get(EXPERTISE));
                } else {
                    throw new RepositoryException("Could not insert appointment. because of: " + appointment);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to insert appointment.", ex);
            throw new RepositoryException("Could not insert appointment.");
        }
    }

    public List<Population> getPopulation() {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_ALL_POPULATIONS)
        ) {
            try (ResultSet rs = stmt.executeQuery()) {
                List<Population> populations = new ArrayList<>();
                while (rs.next()) {
                    String colony = rs.getString("colony");
                    if(colony.equals("MINE")) {
                        populations.add(new Population(rs.getInt(POPULATION_ID), rs.getInt(POPULATION_DOME_ID), rs.getDate(POPULATION_DATE).toString(), rs.getInt(POPULATION_SIZE), rs.getDouble(POPULATION_LATITUDE), rs.getDouble(POPULATION_LONGITUDE), Colony.MINE, new Dome(rs.getInt(DOMES_ID), rs.getString(DOMES_DOMENAME), rs.getDouble(DOMES_LATITUDE), rs.getDouble(DOMES_LONGITUDE), rs.getDouble(DOMES_SURFACE))));
                    }
                    else if(colony.equals("SPACESTATION")) {
                        populations.add(new Population(rs.getInt(POPULATION_ID), rs.getInt(POPULATION_DOME_ID), rs.getDate(POPULATION_DATE).toString(), rs.getInt(POPULATION_SIZE), rs.getDouble(POPULATION_LATITUDE), rs.getDouble(POPULATION_LONGITUDE), Colony.SPACESTATION, new Dome(rs.getInt(DOMES_ID), rs.getString(DOMES_DOMENAME), rs.getDouble(DOMES_LATITUDE), rs.getDouble(DOMES_LONGITUDE), rs.getDouble(DOMES_SURFACE))));
                    }
                    else if(colony.equals("SURFACE")) {
                        populations.add(new Population(rs.getInt(POPULATION_ID), rs.getInt(POPULATION_DOME_ID), rs.getDate(POPULATION_DATE).toString(), rs.getInt(POPULATION_SIZE), rs.getDouble(POPULATION_LATITUDE), rs.getDouble(POPULATION_LONGITUDE), Colony.SURFACE, new Dome(rs.getInt(DOMES_ID), rs.getString(DOMES_DOMENAME), rs.getDouble(DOMES_LATITUDE), rs.getDouble(DOMES_LONGITUDE), rs.getDouble(DOMES_SURFACE))));
                    }
                }
                return populations;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get populations.", ex);
            throw new RepositoryException("Could not get populations.");
        }
    }

    public List<MedicalDispatch> getMedicalDispatches() {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_ALL_MEDICAL_DISPATCHES)
        ) {
            try (ResultSet rs = stmt.executeQuery()) {
                List<MedicalDispatch> medicalDispatches = new ArrayList<>();
                while (rs.next()) {
                    String type = rs.getString("dispatch_type");
                    switch (type){
                        case "AMBULANCE":
                            medicalDispatches.add(new MedicalDispatch(rs.getInt(MEDICAL_DISPATCHES_ID), TypeOfDispatch.AMBULANCE, rs.getInt(MEDICAL_DISPATCHES_DOME_ID), rs.getDate(MEDICAL_DISPATCHES_DATE).toString(), rs.getDouble(MEDICAL_DISPATCHES_LATITUDE), rs.getDouble(MEDICAL_DISPATCHES_LONGITUDE), new Dome(rs.getInt(DOMES_ID), rs.getString(DOMES_DOMENAME), rs.getDouble(DOMES_LATITUDE), rs.getDouble(DOMES_LONGITUDE), rs.getDouble(DOMES_SURFACE))));
                            break;
                        case "EMS":
                            medicalDispatches.add(new MedicalDispatch(rs.getInt(MEDICAL_DISPATCHES_ID), TypeOfDispatch.EMS, rs.getInt(MEDICAL_DISPATCHES_DOME_ID), rs.getDate(MEDICAL_DISPATCHES_DATE).toString(), rs.getDouble(MEDICAL_DISPATCHES_LATITUDE), rs.getDouble(MEDICAL_DISPATCHES_LONGITUDE), new Dome(rs.getInt(DOMES_ID), rs.getString(DOMES_DOMENAME), rs.getDouble(DOMES_LATITUDE), rs.getDouble(DOMES_LONGITUDE), rs.getDouble(DOMES_SURFACE))));
                            break;
                        case "FIRE_DEPARTMENT":
                            medicalDispatches.add(new MedicalDispatch(rs.getInt(MEDICAL_DISPATCHES_ID), TypeOfDispatch.FIRE_DEPARTMENT, rs.getInt(MEDICAL_DISPATCHES_DOME_ID), rs.getDate(MEDICAL_DISPATCHES_DATE).toString(), rs.getDouble(MEDICAL_DISPATCHES_LATITUDE), rs.getDouble(MEDICAL_DISPATCHES_LONGITUDE), new Dome(rs.getInt(DOMES_ID), rs.getString(DOMES_DOMENAME), rs.getDouble(DOMES_LATITUDE), rs.getDouble(DOMES_LONGITUDE), rs.getDouble(DOMES_SURFACE))));
                            break;
                        case "HAZMAT_TEAM":
                            medicalDispatches.add(new MedicalDispatch(rs.getInt(MEDICAL_DISPATCHES_ID), TypeOfDispatch.HAZMAT_TEAM, rs.getInt(MEDICAL_DISPATCHES_DOME_ID), rs.getDate(MEDICAL_DISPATCHES_DATE).toString(), rs.getDouble(MEDICAL_DISPATCHES_LATITUDE), rs.getDouble(MEDICAL_DISPATCHES_LONGITUDE), new Dome(rs.getInt(DOMES_ID), rs.getString(DOMES_DOMENAME), rs.getDouble(DOMES_LATITUDE), rs.getDouble(DOMES_LONGITUDE), rs.getDouble(DOMES_SURFACE))));
                            break;
                        case "LIFEGUARD":
                            medicalDispatches.add(new MedicalDispatch(rs.getInt(MEDICAL_DISPATCHES_ID), TypeOfDispatch.LIFEGUARD, rs.getInt(MEDICAL_DISPATCHES_DOME_ID), rs.getDate(MEDICAL_DISPATCHES_DATE).toString(), rs.getDouble(MEDICAL_DISPATCHES_LATITUDE), rs.getDouble(MEDICAL_DISPATCHES_LONGITUDE), new Dome(rs.getInt(DOMES_ID), rs.getString(DOMES_DOMENAME), rs.getDouble(DOMES_LATITUDE), rs.getDouble(DOMES_LONGITUDE), rs.getDouble(DOMES_SURFACE))));
                            break;
                        case "MEDICAL_HELICOPTER":
                            medicalDispatches.add(new MedicalDispatch(rs.getInt(MEDICAL_DISPATCHES_ID), TypeOfDispatch.MEDICAL_HELICOPTER, rs.getInt(MEDICAL_DISPATCHES_DOME_ID), rs.getDate(MEDICAL_DISPATCHES_DATE).toString(), rs.getDouble(MEDICAL_DISPATCHES_LATITUDE), rs.getDouble(MEDICAL_DISPATCHES_LONGITUDE), new Dome(rs.getInt(DOMES_ID), rs.getString(DOMES_DOMENAME), rs.getDouble(DOMES_LATITUDE), rs.getDouble(DOMES_LONGITUDE), rs.getDouble(DOMES_SURFACE))));
                            break;
                        case "SEARCH_AND_RESCUE":
                            medicalDispatches.add(new MedicalDispatch(rs.getInt(MEDICAL_DISPATCHES_ID), TypeOfDispatch.SEARCH_AND_RESCUE, rs.getInt(MEDICAL_DISPATCHES_DOME_ID), rs.getDate(MEDICAL_DISPATCHES_DATE).toString(), rs.getDouble(MEDICAL_DISPATCHES_LATITUDE), rs.getDouble(MEDICAL_DISPATCHES_LONGITUDE), new Dome(rs.getInt(DOMES_ID), rs.getString(DOMES_DOMENAME), rs.getDouble(DOMES_LATITUDE), rs.getDouble(DOMES_LONGITUDE), rs.getDouble(DOMES_SURFACE))));
                            break;
                        case "TOXICOLOGY_TEAM":
                            medicalDispatches.add(new MedicalDispatch(rs.getInt(MEDICAL_DISPATCHES_ID), TypeOfDispatch.TOXICOLOGY_TEAM, rs.getInt(MEDICAL_DISPATCHES_DOME_ID), rs.getDate(MEDICAL_DISPATCHES_DATE).toString(), rs.getDouble(MEDICAL_DISPATCHES_LATITUDE), rs.getDouble(MEDICAL_DISPATCHES_LONGITUDE), new Dome(rs.getInt(DOMES_ID), rs.getString(DOMES_DOMENAME), rs.getDouble(DOMES_LATITUDE), rs.getDouble(DOMES_LONGITUDE), rs.getDouble(DOMES_SURFACE))));
                            break;
                        case "POLICE":
                            medicalDispatches.add(new MedicalDispatch(rs.getInt(MEDICAL_DISPATCHES_ID), TypeOfDispatch.POLICE, rs.getInt(MEDICAL_DISPATCHES_DOME_ID), rs.getDate(MEDICAL_DISPATCHES_DATE).toString(), rs.getDouble(MEDICAL_DISPATCHES_LATITUDE), rs.getDouble(MEDICAL_DISPATCHES_LONGITUDE), new Dome(rs.getInt(DOMES_ID), rs.getString(DOMES_DOMENAME), rs.getDouble(DOMES_LATITUDE), rs.getDouble(DOMES_LONGITUDE), rs.getDouble(DOMES_SURFACE))));
                            break;
                        default:
                            LOGGER.log(Level.WARNING, String.format("Unknown type of dispatch: %s", type));
                            break;
                    }
                }
                return medicalDispatches;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get medical dispatches.", ex);
            throw new RepositoryException("Could not get medical dispatches.");
        }
    }

    public List<MeteorShower> getMeteorShowers() {
        try (Connection con = getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(SQL_ALL_METEOR_SHOWERS)) {
                ResultSet rs = ps.executeQuery();
                List<MeteorShower> meteorShowers = new ArrayList<>();
                while (rs.next()) {
                    String type = rs.getString("meteor_showers.damage_type");
                    switch (type){
                        case "LOW":
                            meteorShowers.add(new MeteorShower(rs.getInt(METEOR_SHOWERS_ID), rs.getInt(METEOR_SHOWERS_DOME_ID), DamageLevel.LOW, rs.getDate(METEOR_SHOWERS_DATE).toString(), rs.getDouble(METEOR_SHOWERS_LONGITUDE), rs.getDouble(METEOR_SHOWERS_LATITUDE), new Dome(rs.getInt(DOMES_ID), rs.getString(DOMES_DOMENAME), rs.getDouble(DOMES_LATITUDE), rs.getDouble(DOMES_LONGITUDE), rs.getDouble(DOMES_SURFACE))));
                            break;
                        case "MEDIUM":
                            meteorShowers.add(new MeteorShower(rs.getInt(METEOR_SHOWERS_ID), rs.getInt(METEOR_SHOWERS_DOME_ID), DamageLevel.MEDIUM, rs.getDate(METEOR_SHOWERS_DATE).toString(), rs.getDouble(METEOR_SHOWERS_LONGITUDE), rs.getDouble(METEOR_SHOWERS_LATITUDE), new Dome(rs.getInt(DOMES_ID), rs.getString(DOMES_DOMENAME), rs.getDouble(DOMES_LATITUDE), rs.getDouble(DOMES_LONGITUDE), rs.getDouble(DOMES_SURFACE))));
                            break;
                        case "HIGH":
                            meteorShowers.add(new MeteorShower(rs.getInt(METEOR_SHOWERS_ID), rs.getInt(METEOR_SHOWERS_DOME_ID), DamageLevel.HIGH, rs.getDate(METEOR_SHOWERS_DATE).toString(), rs.getDouble(METEOR_SHOWERS_LONGITUDE), rs.getDouble(METEOR_SHOWERS_LATITUDE), new Dome(rs.getInt(DOMES_ID), rs.getString(DOMES_DOMENAME), rs.getDouble(DOMES_LATITUDE), rs.getDouble(DOMES_LONGITUDE), rs.getDouble(DOMES_SURFACE))));
                            break;
                        default:
                            LOGGER.log(Level.WARNING, String.format("Unknown damage level: %s", type));
                            break;
                    }
                }
                return meteorShowers;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get meteor showers.", ex);
            throw new RepositoryException("Could not get meteor showers.");
        }
    }

    public List<DustStorm> getDustStorms() {
        try (Connection con = getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(SQL_ALL_DUST_STORMS)) {
                ResultSet rs = ps.executeQuery();
                List<DustStorm> dustStorms = new ArrayList<>();
                while (rs.next()) {
                    String type = rs.getString("dust_storms.damage_type");
                    switch (type){
                        case "LOW":
                            dustStorms.add(new DustStorm(rs.getInt(DUST_STORMS_ID), rs.getInt(DUST_STORMS_DOME_ID), DamageLevel.LOW, rs.getDate(DUST_STORMS_DATE).toString(), rs.getDouble(DUST_STORMS_LONGITUDE), rs.getDouble(DUST_STORMS_LATITUDE), new Dome(rs.getInt(DOMES_ID), rs.getString(DOMES_DOMENAME), rs.getDouble(DOMES_LATITUDE), rs.getDouble(DOMES_LONGITUDE), rs.getDouble(DOMES_SURFACE))));
                            break;
                        case "MEDIUM":
                            dustStorms.add(new DustStorm(rs.getInt(DUST_STORMS_ID), rs.getInt(DUST_STORMS_DOME_ID), DamageLevel.MEDIUM, rs.getDate(DUST_STORMS_DATE).toString(), rs.getDouble(DUST_STORMS_LONGITUDE), rs.getDouble(DUST_STORMS_LATITUDE), new Dome(rs.getInt(DOMES_ID), rs.getString(DOMES_DOMENAME), rs.getDouble(DOMES_LATITUDE), rs.getDouble(DOMES_LONGITUDE), rs.getDouble(DOMES_SURFACE))));
                            break;
                        case "HIGH":
                            dustStorms.add(new DustStorm(rs.getInt(DUST_STORMS_ID), rs.getInt(DUST_STORMS_DOME_ID), DamageLevel.HIGH, rs.getDate(DUST_STORMS_DATE).toString(), rs.getDouble(DUST_STORMS_LONGITUDE), rs.getDouble(DUST_STORMS_LATITUDE), new Dome(rs.getInt(DOMES_ID), rs.getString(DOMES_DOMENAME), rs.getDouble(DOMES_LATITUDE), rs.getDouble(DOMES_LONGITUDE), rs.getDouble(DOMES_SURFACE))));
                            break;
                        default:
                            LOGGER.log(Level.WARNING, String.format("Unknown damage level: %s", type));
                            break;
                    }
                }
                return dustStorms;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get dust storms.", ex);
            throw new RepositoryException("Could not get dust storms.");
        }
    }

    public Appointment getAppointment(int appointmentId) {
        try (Connection con = getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(SQL_APPOINTMENT_BY_ID)) {
                ps.setInt(1, appointmentId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return new Appointment(rs.getInt(ID), rs.getDate(DATE).toString(), rs.getString(TIME), rs.getString(TOPIC), rs.getString(EMPLOYEE_NAME), rs.getString(EXPERTISE));
                }
                return null;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get appointment.", ex);
            throw new RepositoryException("Could not get appointment.");
        }
    }

    public void deleteAppointment(int appointmentId) {
        try (Connection con = getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(SQL_DELETE_APPOINTMENT)) {
                ps.setInt(1, appointmentId);
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to delete appointment.", ex);
            throw new RepositoryException("Could not delete appointment.");
        }
    }
}
