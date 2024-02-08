package be.howest.ti.mars.logic.domain;

public class Company {
    private final int id;
    private final String name;
    private final String section;
    private final int adEffectiveness;
    private final int userId;

    public Company(int id, String name, String section, int adEffectiveness, int userId) {
        this.id = id;
        this.name = name;
        this.section = section;
        this.adEffectiveness = adEffectiveness;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSection() {
        return section;
    }

    public int getAdEffectiveness() {
        return adEffectiveness;
    }

    public int getUserId() {
        return userId;
    }
}
