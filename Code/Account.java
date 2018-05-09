package Code;

import java.util.ArrayList;

public class Account {

    private String firstName;
    private String lastName;
    private String userName;
    private ArrayList<Ticket> ticket;

    public Account(String firstname, String lastName, String username, ArrayList<Ticket> ticket) {
        this.firstName = firstname;
        this.lastName = lastName;
        this.userName = username;
        this.ticket = ticket;
    }

    public String getName() {
        return null;
    }

    public String getAccount() {
        return null;
    }

    public Ticket getTicket(int confirmNum) {
        return null;
    }

    public FlightStatus viewFlightStatus(int flightNum) {
        return null;
    }

    public boolean purchaseTicket(Date date, Trip trip) {
        return false;
    }

    public String getUserName() {
        return null;
    }

}
