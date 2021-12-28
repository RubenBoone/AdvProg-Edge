package advporg.einformation.model;

public class TourMonument {
    private Tour tour;
    private Monument monument;

    public TourMonument() {
    }

    public TourMonument(Tour tour, Monument monument) {
        this.tour = tour;
        this.monument = monument;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public void setMonument(Monument monument) {
        this.monument = monument;
    }

    public Tour getTour() {
        return tour;
    }

    public Monument getMonument() {
        return monument;
    }
}
