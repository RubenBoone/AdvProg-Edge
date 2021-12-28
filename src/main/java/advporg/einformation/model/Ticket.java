package advporg.einformation.model;

import java.util.Date;

public class Ticket {
    private int id;
    private String tourCode;
    private String firstName;
    private String lastName;
    private String email;
    private Date date;

    public Ticket() {
    }

    public Ticket(int id, String tourCode, String firstName, String lastName, String email, Date date) {
        this.id = id;
        this.tourCode = tourCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTourCode(String tourCode) {
        this.tourCode = tourCode;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getTourCode() {
        return tourCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Date getDate() {
        return date;
    }
}
