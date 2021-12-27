package advporg.einformation.model;

public class MonumentInformation {
    private Monument monument;
    private Information information;

    public MonumentInformation() {
    }

    public MonumentInformation(Monument monument, Information information) {
        this.monument = monument;
        this.information = information;
    }

    public Monument getMonument() {
        return monument;
    }

    public Information getInformation() {
        return information;
    }

    public void setMonument(Monument monument) {
        this.monument = monument;
    }

    public void setInformation(Information information) {
        this.information = information;
    }
}
