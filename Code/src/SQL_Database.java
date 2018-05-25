package src;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*

Queries:

SELECT EncryptedPassword
  FROM customers WHERE Username = "nparra";

SELECT flights.FlightId FROM flights INNER JOIN trips
 WHERE trips.FlightId = flights.FlightId AND flights.Destination = "Phoenix"
 AND trips.Date < date('now', '+2 week') AND trips.Date >= date('now');

SELECT customers.LastName, customers.FirstName FROM customers INNER JOIN tickets INNER JOIN trips
WHERE tickets.Username = customers.Username AND tickets.TripId = 0 AND tickets.CheckedIn = 'false';

SELECT tickets.SeatNumber from customers INNER JOIN tickets INNER JOIN flights INNER JOIN trips
WHERE customers.LastName = 'Parra' AND customers.FirstName = 'Nicolas' AND customers.Username = tickets.Username
AND tickets.TripId = trips.TripId AND trips.FlightId = flights.FlightId AND flights.Destination = 'Phoenix';


Tables:

customers
    Username (unique) String -- primary key
    EncryptedPassword String
    FirstName String
    LastName String

employees
    Username (unique) String -- primary key
    EncryptedPassword String
    FirstName String
    LastName String

flights
    FlightId Int -- primary key
    Source String
    Destination String

trips
    TripId int -- primary key
    FlightId int   -- (FlightId, Date) is unique
    Date (DATETIME type)
    Price double
    Status int

tickets
    Username String
    TripId int  -- (TripId, SeatNumber) primary key
    SeatNumber int
    CheckedIn boolean

 */

public class SQL_Database implements Database {
    static private SQL_Database uniqueInstance;
    private Connection conn;
    private Statement st;

    private SQL_Database(){
        try
        {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:Code/src/CAIDatabase.db");
            if (conn != null)
            {
                st = conn.createStatement();
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }

    static public SQL_Database getInstance()
    {
        if (uniqueInstance == null)
        {
            uniqueInstance = new SQL_Database();
        }
        return uniqueInstance;
    }

    /*

    Flight methods

     */

    public int getTripId(int FlightId, Calendar date)
    {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd kk:mm");
        String query = "SELECT TripId FROM trips WHERE FlightId = " + FlightId + " AND Date = '" +
                f.format(date.getTime()) + "'";
        int id = -1;
        try
        {
            ResultSet rs = st.executeQuery(query);
            // Should only ever return one entry
            rs.next();
            id = rs.getInt("TripId");
        }
        catch (SQLException e)
        {
            e.getMessage();
            return -1;
        }
        return id;
    }

    // Returns the number of trips that occurred between two dates (inclusive) or -1 on error
    // If both dates are null, returns number of trips in database
    public int getNumTrips(Calendar from, Calendar to)
    {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd kk:mm");
        String query;
        if (from == null && to == null)
        {
            query = "SELECT COUNT(*) FROM trips";
        }
        else if (from == null || to == null) return -1;
        else
        {
            query = "SELECT COUNT(*) FROM trips WHERE Date BETWEEN '" +
                    f.format(from.getTime()) + "' AND '" + f.format(to.getTime()) + "'";
        }
        int NumFlights = 0;
        try
        {
            ResultSet rs = st.executeQuery(query);
            // Should only ever return one entry
            rs.next();
            NumFlights = rs.getInt(0);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return NumFlights;
    }

    // Overload for simplicity
    public int getNumTrips()
    {
        return getNumTrips(null, null);
    }

    // Gets status of flight by ID, 0 = On-time, 1 = Delayed, 2 = Cancelled, Returns -1 on invalid ID
    public int getStatus(int TripId)
    {
        String query = "SELECT Status FROM trips WHERE TripId = " + TripId;
        int status;
        try
        {
            ResultSet rs = st.executeQuery(query);
            rs.next();
            status = rs.getInt("Status");
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return status;
    }

    // Set status of trip by ID, 0 = On-time, 1 = Delayed, 2 = Cancelled, Return 0 on success, -1 on error
    public int setStatus(int TripId, int Status)
    {
        String query = "UPDATE trips SET Status = ? WHERE TripId = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, Status);
            ps.setInt(2, TripId);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return 0;
    }

    // Attempts to add trip to database. Returns the new trip id, or -1 on error
    // Defaults status to 0 (On-time)
    public int addTrip(int FlightId, Calendar date, double Price)
    {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd kk:mm");
        String query = "INSERT INTO trips (FlightId, Date, Price, Status) values (?,?,?,0)";
        // TripId should be autoincremented and filled in by database
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, FlightId);
            ps.setString(2, f.format(date.getTime()));
            ps.setDouble(3, Price);
            ps.executeUpdate();

            String query2 = "SELECT TripId FROM trips WHERE FlightId = " + FlightId + " AND Date = '"
                    + f.format(date.getTime()) + "'";
            ResultSet rs = st.executeQuery(query2);
            rs.next();
            return rs.getInt("TripId");
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    // Defaults FullSeats to 0 and Price to 0
    public int addTrip(int FlightId, Calendar date)
    {
        return addTrip(FlightId, date, 0);
    }

    // Changes trips's destination, date, fullseats, and price. Returns 0 on success, -1 on error
    public int editTrip(int TripId, int FlightId, Calendar date, double Price, int Status)
    {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd kk:mm");
        String query = "UPDATE trips SET FlightId = ?, Date = ?, Price = ?, Status = ? WHERE TripId = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, FlightId);
            ps.setString(2, f.format(date.getTime()));
            ps.setDouble(3, Price);
            ps.setInt(4, Status);
            ps.setInt(5, TripId);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return 0;
    }

    // Attempts to remove flight from database. Returns 0 on success, 1 on error
    public int removeTrip(int TripId)
    {
        String query = "DELETE FROM trips WHERE TripId = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, TripId);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Calculates average number of empty seats using previous two weeks of flight information, returns -1 on error
    public double calculateAvgEmpty(String Destination)
    {
        int id = getFlightId("San Luis Obispo", Destination);
        String query = "SELECT NumFlights FROM NumberOfFlightsPerDestination WHERE FlightId = " + id;
        String query2 = "SELECT SUM(NumEmptySeats) FROM NumEmptySeatsPerTrip GROUP BY FlightId WHERE FlightId = " + id;
        double numtrips;
        double numempty;
        try
        {
            ResultSet rs = st.executeQuery(query);
            rs.next();
            numtrips = rs.getInt(0);
            rs = st.executeQuery(query2);
            rs.next();
            numempty = rs.getInt(0);
            if (numtrips == 0)
            {
                return 20.0;
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return numempty / numtrips;
    }

    /*

    Flight methods

     */

    // Returns flight id associated with Source and Destination cities, or -1 on error
    public int getFlightId(String Source, String Destination)
    {
        String query = "SELECT FlightId FROM flights WHERE Source = '" + Source + "' AND Destination = '" + Destination + "'";
        int id = -1;
        try
        {
            ResultSet rs = st.executeQuery(query);
            // Should only ever return one entry
            rs.next();
            id = rs.getInt("FlightId");
        }
        catch (SQLException e)
        {
            e.getMessage();
            return -1;
        }
        return id;
    }

    // Adds new flight from source to destination, returns flight ID on success, or -1 on error
    public int addFlight(String Source, String Destination)
    {
        String query = "INSERT INTO flights (Source, Destination) values (?,?)";
        // FlightId should be autoincremented and filled in by database
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, Source);
            ps.setString(2, Destination);
            ps.executeUpdate();

            String query2 = "SELECT FlightId FROM flights WHERE Source = '" + Source + "' AND Destination = '" + Destination + "'";
            ResultSet rs = st.executeQuery(query2);
            rs.next();
            return rs.getInt("FlightId");
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    // Removes flight from database, returns 0 on success, or -1 on error
    public int removeFlight(int FlightId)
    {
        String query = "DELETE FROM flights WHERE FlightId = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, FlightId);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    /*

    Customer methods

     */

    /* returns customer info associated with username or null if not found */
    public List<String> getCustomerInfo(String Username)
    {
        String query = "SELECT * FROM customers WHERE Username = '" + Username + "'";
        List<String> entry = new ArrayList<>();
        try
        {
            ResultSet rs = st.executeQuery(query);
            if (!rs.next())
            {
                return null;
            }
            entry.add(rs.getString("Username"));
            entry.add(rs.getString("EncrypedPassword"));
            entry.add(rs.getString("FirstName"));
            entry.add(rs.getString("LastName"));
            return entry;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Attempts to add customer account to database. Returns 0 on success, -1 on error.
    public int addCustomerAccount(String Username, String EncryptedPassword, String FirstName, String LastName)
    {
        String query = "INSERT INTO customers (Username, EncryptedPassword, FirstName, LastName) values (?,?,?,?)";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, Username);
            ps.setString(2, EncryptedPassword);
            ps.setString(3, FirstName);
            ps.setString(4, LastName);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return 0;
    }

    // Edits existing customer and sets values to parameters passed (cannot change username). Returns -1 on error
    public int editCustomer(String Username, String EncryptedPassword, String FirstName, String LastName)
    {
        String query = "UPDATE customers SET EncryptedPassword = ?, FirstName = ?, LastName = ? WHERE Username = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, FirstName);
            ps.setString(2, EncryptedPassword);
            ps.setString(3, LastName);
            ps.setString(4, Username);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return 0;
    }

    // Attempts to remove customer account to database. Returns 0 on success, -1 on error
    public int removeCustomer(String Username)
    {
        String query = "DELETE FROM customers WHERE Username = '" + Username + "'";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return 0;
    }

    /*

    Employee methods

     */

    // Attempts to add employee account to database. Returns 0 on success, -1 on error
    public int addEmployeeAccount(String Username, String EncryptedPassword, String FirstName, String LastName)
    {
        String query = "INSERT INTO employees (Username, FirstName, LastName) values (?,?,?,?)";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, Username);
            ps.setString(2, EncryptedPassword);
            ps.setString(3, FirstName);
            ps.setString(4, LastName);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return 0;
    }

    // Edits existing customer and sets values to parameters passed (cannot change username). Return -1 on error
    public int editEmployee(String Username, String EncryptedPassword, String FirstName, String LastName)
    {
        String query = "UPDATE employees SET EncrypedPassword = ?, FirstName = ?, LastName = ? WHERE Username = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, EncryptedPassword);
            ps.setString(2, FirstName);
            ps.setString(3, LastName);
            ps.setString(4, Username);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return 0;
    }

    // Attempts to remove employee account to database. Returns 0 on success, 1 on error
    public int removeEmployee(String Username)
    {
        String query = "DELETE FROM employees WHERE Username '" + Username + "'";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    /*

    Ticket methods

     */

    // Adds a ticket to the database. Returns 0 on success, -1 on error
    public int addTicket(String Username, int TripId, int SeatNumber, boolean CheckedIn)
    {
        String query = "INSERT INTO tickets (Username, TripId, SeatNumber, CheckedIn) values (?,?,?,?)";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, Username);
            ps.setInt(2, TripId);
            ps.setInt(3, SeatNumber);
            ps.setBoolean(4, CheckedIn);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return 0;
    }

    // Overload for simplicity
    public int addTicket(String Username, int TripId, int SeatNumber)
    {
        return addTicket(Username, TripId, SeatNumber, false);
    }

    // Edits existing customer and changes SeatNumber and CheckedIn
    public int editTicket(String Username, int TripId, int SeatNumber, boolean CheckedIn)
    {
        String query = "UPDATE tickets SET SeatNumber = ?, CheckedIn = ? WHERE Username = ? AND TripId = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, SeatNumber);
            ps.setBoolean(2, CheckedIn);
            ps.setString(3, Username);
            ps.setInt(4, TripId);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return 0;
    }

    // Attempts to remove ticket from database. Returns 0 on success, -1 on error
    public int removeTicket(String Username, int FlightId)
    {
        String query = "DELETE FROM tickets WHERE Username = ? AND FlightId = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, Username);
            ps.setInt(2, FlightId);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return 0;
    }

    // Attempts to check user in for flight. Returns 0 on success, -1 on error
    public int checkIn(String Username, int TripId)
    {
        String query = "UPDATE tickets SET CheckedIn = ? WHERE Username = ? AND TripId = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setBoolean(1, true);
            ps.setString(2, Username);
            ps.setInt(3, TripId);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return 0;
    }
}
