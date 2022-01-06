package advporg.einformation.model;

public class Tour {
    private int id;
    private String monuCode;
    private String tourCode;

    private double tourTime;
    private double entryFee;
    private double score;
    private String title;
    private String description;
    private int avgCustomer;

    public Tour() {
    }

    public Tour(int id, String monuCode, String tourCode, double tourTime, double entryFee, double score, String title, String description, int avgCustomer) {
        this.id = id;
        this.monuCode = monuCode;
        this.tourCode = tourCode;
        this.tourTime = tourTime;
        this.entryFee = entryFee;
        this.score = score;
        this.title = title;
        this.description = description;
        this.avgCustomer = avgCustomer;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMonuCode(String monuCode) {
        this.monuCode = monuCode;
    }

    public void setTourCode(String tourCode) {
        this.tourCode = tourCode;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAvgCustomers(int avgCustomer) {
        this.avgCustomer = avgCustomer;
    }

    public int getId() {
        return id;
    }

    public String getMonuCode() {
        return monuCode;
    }

    public String getTourCode() {
        return tourCode;
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

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getAvgCustomer() {
        return avgCustomer;
    }
}
