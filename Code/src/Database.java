package src;

import java.util.Calendar;
import java.util.Calendar;
import java.util.List;

public interface Database
{
    /* Trip methods */
    public int getTripId(int FlightId, Calendar Date);
    public int getNumTrips(Calendar from, Calendar to);
    public int getStatus(int TripId);
    public int setStatus(int TripId, int Status);
    public int addTrip(int FlightId, Calendar Date, double Price);
    public int editTrip(int TripId, int FlightId, Calendar Date, double Price, int Status);
    public int removeTrip(int TripId);
    public double calculateAvgEmpty(String Destination);

    /* Flight methods */
    public int getFlightId(String Source, String Destination);
    public int addFlight(String Source, String Destination);
    public int removeFlight(int FlightId);

    /* Customer methods */
    public List<String> getCustomerInfo(String Username);
    public int addCustomerAccount(String Username, String EncryptedPassword, String FirstName, String LastName);
    public int editCustomer(String Username, String EncryptedPassword, String FirstName, String LastName);
    public int removeCustomer(String Username);

    /* Employee methods */
    public int addEmployeeAccount(String Username, String EncryptedPassword, String FirstName, String LastName);
    public int editEmployee(String Username, String EncryptedPassword, String FirstName, String LastName);
    public int removeEmployee(String Username);

    /* Ticket methods */
    public int addTicket(String Username, int TripId, int SeatNumber, boolean CheckedIn);
    public int editTicket(String Username, int TripId, int SeatNumber, boolean CheckedIn);
    public int removeTicket(String Username, int TripId);
    public int checkIn(String Username, int TripId);
}
