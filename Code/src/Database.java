package src;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public interface Database
{
    /* Trip methods */
    int getTripId(int FlightId, Calendar Date);
    int getNumTrips(Calendar from, Calendar to);
    int getStatus(int TripId);
    int setStatus(int TripId, int Status);
    int addTrip(int FlightId, Calendar Date, double Price);
    int editTrip(int TripId, int FlightId, Calendar Date, double Price, int Status);
    int removeTrip(int TripId);
    double calculateAvgEmpty(String Destination);
    Calendar getDate(int TripId);
    ArrayList<Trip> getAllTrips();
    int getFlightIdFromTrip(int TripId);

    /* Flight methods */
    int getFlightId(String Source, String Destination);
    int addFlight(String Source, String Destination);
    int removeFlight(int FlightId);
    String getFlightSrc(int FlightId);
    String getFlightDest(int FlightId);

    /* Customer methods */
    List<String> getCustomerInfo(String Username);
    int addCustomerAccount(String Username, String EncryptedPassword, String FirstName, String LastName);
    int editCustomer(String Username, String EncryptedPassword, String FirstName, String LastName);
    int removeCustomer(String Username);

    /* Employee methods */
    List<String> getEmployeeInfo(String Username);
    int addEmployeeAccount(String Username, String EncryptedPassword, String FirstName, String LastName);
    int editEmployee(String Username, String EncryptedPassword, String FirstName, String LastName);
    int removeEmployee(String Username);

    /* Ticket methods */
    int addTicket(String Username, int TripId, int SeatNumber, int NumBags, boolean CheckedIn);
    int editTicket(String Username, int TripId, int SeatNumber, int NumBags, boolean CheckedIn);
    int removeTicket(String Username, int TripId, int SeatNumber);
    int checkIn(String Username, int TripId);
    int setBags(String Username, int TripId, int NumBags);
}
