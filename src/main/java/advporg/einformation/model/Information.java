package advporg.einformation.model;

public class Information {
    private int id;
    private int avgCustomers;

    private String monuCode;
    private String buildYear;

    private double tourTime;
    private double entryFee;
    private double score;

    public Information() {
    }

    public Information(int id, int avgCustomers, String monuCode, String buildYear, double tourTime, double entryFee, double score) {
        this.id = id;
        this.avgCustomers = avgCustomers;
        this.monuCode = monuCode;
        this.buildYear = buildYear;
        this.tourTime = tourTime;
        this.entryFee = entryFee;
        this.score = score;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAvgCustomers(int avgCustomers) {
        this.avgCustomers = avgCustomers;
    }

    public void setMonuCode(String monuCode) {
        this.monuCode = monuCode;
    }

    public void setBuildYear(String buildYear) {
        this.buildYear = buildYear;
    }

    public void setTourTime(double tourTime) {
        this.tourTime = tourTime;
    }

    public void setEntryFee(double entryFee) {
        this.entryFee = entryFee;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public int getAvgCustomers() {
        return avgCustomers;
    }

    public String getMonuCode() {
        return monuCode;
    }

    public String getBuildYear() {
        return buildYear;
    }

    public double getTourTime() {
        return tourTime;
    }

    public double getEntryFee() {
        return entryFee;
    }

    public double getScore() {
        return score;
    }
}
