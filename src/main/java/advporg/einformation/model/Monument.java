package advporg.einformation.model;

public class Monument {
    private int id;
    private String name;
    private String location;
    private String monuCode;
    private String buildYear;

    public Monument() {
    }

    public Monument(int id, String name, String location, String monuCode, String buildYear) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.monuCode = monuCode;
        this.buildYear = buildYear;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMonuCode(String monuCode) {
        this.monuCode = monuCode;
    }

    public void setBuildYear(String buildYear) {
        this.buildYear = buildYear;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getMonuCode() {
        return monuCode;
    }

    public String getBuildYear() {
        return buildYear;
    }
}
