package be.howest.ti.mars.logic.domain;

public class User {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String homeAddress;
    private final String premium;

    public User(int id, String firstName, String lastName, String homeAddress, String premium) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.homeAddress = homeAddress;
        this.premium = premium;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public String getPremium() {
        return premium;
    }
}
