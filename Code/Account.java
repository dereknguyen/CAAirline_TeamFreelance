package Code;

import java.util.ArrayList;
import java.util.Date;

public class Account {

    private String firstName;
    private String lastName;
    private String userName;
    private ArrayList<Ticket> ticket;

    public Account(String firstName, String lastName, String username, ArrayList<Ticket> ticket) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = username;
        this.ticket = ticket;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    /**
     * Get account registered full name by concatenating first name and last name.
     * @return Full Name.
     */
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    /**
     * Get the flight ticket given the associated ticket number of that ticket.
     * @param ticketNumber The assigned ticket number.
     * @return The ticket with the given ticket number.
     */
    public Ticket getTicket(int ticketNumber) {

        for (Ticket current: this.ticket) {
            if (current.getTicketNumber() == ticketNumber) {
                return current;
            }
        }

        return null;
    }

    public boolean purchaseTicket(int DestinationID, Date date, Trip trip) {
        Text_Database db = Text_Database.getInstance();
        return false;
    }

    public void getFlightStatus() {
        CustomerControl.getInstance().status();
    }

    public void reserve() {
        CustomerControl.getInstance().reserve(this.userName);
    }

    public void checkIn() {
        CustomerControl.getInstance().checkin(this.userName);
    }

    /**
     * Get the registered username.
     * @return Username.
     */
    public String getUserName() {
        return this.userName;
    }


}
