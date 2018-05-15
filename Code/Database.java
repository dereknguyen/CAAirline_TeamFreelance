package Code;

import java.sql.*;

public interface Database
{
    /* Flight methods */
    public int getFlightId(int DestinationId, Date date);
    public int getNumFlights(Date from, Date to);
    public int getStatus(int FlightId);
    public int setStatus(int FlightId, int Status);
    public int addFlight(int DestinationId, Date date, int FullSeats, double Price);
    public int editFlight(int FlightId, int DestinationId, Date date, int FullSeats, double Price);
    public int removeFlight(int FlightId);
    public double calculateAvgEmpty(int DestinationId);

    /* Customer methods */
    public int addCustomerAccount(String Username, String FirstName, String LastName);
    public int editCustomer(String Username, String FirstName, String LastName);
    public int removeCustomer(String Username);

    /* Employee methods */
    public int addEmployeeAccount(String Username, String FirstName, String LastName);
    public int editEmployee(String Username, String FirstName, String LastName);
    public int removeEmployee(String Username);

    /* Ticket methods */
    public int addTicket(String Username, int FlightId, int SeatNumber, boolean CheckedIn);
    public int editTicket(String Username, int FlightId, int SeatNumber, boolean CheckedIn);
    public int removeTicket(String Username, int FlightId);
    public int checkIn(String Username, int FlightId);
}
