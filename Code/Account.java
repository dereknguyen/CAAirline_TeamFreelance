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

    // TODO: Need database to implement purchase ticket
    public boolean purchaseTicket(int DestinationID, Date date, Trip trip) {
        Database_Interface db = Database_Interface.getInstance();
        return false;
    }

    public int removeTicket() {
        return 0;
    }

    public int checkIn() {
        return 0;
    }

    /**
     * Get the registered username.
     * @return Username.
     */
    public String getUserName() {
        return this.userName;
    }


}
