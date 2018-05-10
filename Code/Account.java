package com.company;

import java.util.ArrayList;
import java.util.Date;
//import java.util.HashMap;

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
        return firstName;
    }

    public Ticket getTicket(int ticketNumber) {

        for (Ticket current: this.ticket) {
            if (current.getTicketNumber() == ticketNumber) {
                return current;
            }
        }

        return null;

    }

//    public FlightStatus viewFlightStatus(int flightNum) {
//        return null;
//    }

    public boolean purchaseTicket(Date date, Trip trip) {


        return false;
    }

    public String getUserName() {
        return null;
    }

}
