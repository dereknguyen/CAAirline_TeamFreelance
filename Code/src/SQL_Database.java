package src;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/*

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
    NumBags int
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
            conn = DriverManager.getConnection("jdbc:sqlite:src/CAIDatabase.db");
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
        if (FlightId < 0) return -1;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd kk:mm");
        String query = "SELECT TripId FROM trips WHERE FlightId = " + FlightId + " AND Date = '" +
                f.format(date.getTime()) + "'";
        int id;
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
        int NumFlights;
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

    // Returns avg revenue for flightId based on past two weeks
    public double getAvgRevenue(int FlightId) {
        String query = "SELECT Rev FROM AvgRevPerDest WHERE FlightId = " + FlightId;
        try
        {
            ResultSet rs = st.executeQuery(query);
            rs.next();
            return rs.getDouble(1);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    // Gets status of flight by ID, 0 = On-time, 1 = Delayed, 2 = Cancelled, Returns -1 on invalid ID
    public int getStatus(int TripId)
    {
        if (TripId < 0) return -1;
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
        if (TripId < 0 || (Status != 0 && Status != 1 && Status != 2)) return -1;
        String query = "UPDATE trips SET Status = ? WHERE TripId = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, Status);
            ps.setInt(2, TripId);
            if (ps.executeUpdate() == 0) return -1;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return 0;
    }

    // Attempts to add trip to database. Returns the new trip id, or -1 on time conflict, or -2 on other error
    // Defaults status to 0 (On-time)
    public int addTrip(int FlightId, Calendar date, double Price)
    {
        if (FlightId < 0 || Price < 0) return -1;
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
            if (e.getMessage().contains("Time Constraint"))
            {
                return -1;
            }
            else
            {
                return -2;
            }
        }
    }

    // Changes trips's destination, date, fullseats, and price. Returns 0 on success, -1 on error
    public int editTrip(int TripId, int FlightId, Calendar date, double Price, int Status)
    {
        if (TripId < 0 || FlightId < 0 || Price < 0) return -1;
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
            if (ps.executeUpdate() == 0) return -1;
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
        if (TripId < 0) return -1;
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

    // Calculates average percentage of empty seats using previous two weeks of flight information, returns -1 on error
    public double calculateAvgEmpty(String Destination)
    {
        int id = getFlightId("San Luis Obispo", Destination);
        String query = "SELECT NumFlights FROM NumberOfTripsPerDestination WHERE FlightId = " + id;
        String query2 = "SELECT SUM(NumEmptySeats) FROM NumEmptySeatsPerTrip GROUP BY FlightId HAVING FlightId = " + id;
        double numtrips;
        double numempty;
        try
        {
            ResultSet rs = st.executeQuery(query);
            rs.next();
            numtrips = rs.getInt(1);
            rs = st.executeQuery(query2);
            if (!rs.next())
            {
                return 1.0;
            }
            numempty = rs.getInt(1);
            if (numtrips == 0)
            {
                return 1.0;
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return (numempty/20) / numtrips;
    }

    /* return departure date of a trip, or null for nonexistant tripID */
    public Calendar getDate(int TripId)
    {
        if (TripId < 0) return null;
        String query = "SELECT Date FROM trips WHERE TripId = " + TripId;
        Date date;
        Calendar c = Calendar.getInstance();
        try
        {
            ResultSet rs = st.executeQuery(query);
            rs.next();
            date = rs.getDate("Date");
            c.setTime(date);
            return c;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /* returns an array of all trips in the database, or null on error */
    public ArrayList<Trip> getAllTrips()
    {
        String query = "SELECT * FROM trips";
        ArrayList<Trip> out = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm");
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                Calendar c = Calendar.getInstance();
                c.setTime(sdf.parse(rs.getString("Date")));
                out.add(new Trip(rs.getInt("TripId"),
                        rs.getInt("FlightId"),
                        c,
                        rs.getDouble("Price"),
                        rs.getInt("Status")));
            }
            return out;

        }
        catch (SQLException | ParseException e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /* Returns flight id associated with trip, or -1 on error */
    public int getFlightIdFromTrip(int TripId)
    {
        String query = "SELECT FlightId FROM trips WHERE TripId = " + TripId;
        try
        {
            ResultSet rs = st.executeQuery(query);
            // Should only ever return one entry
            rs.next();
            return rs.getInt("FlightId");
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public ArrayList<Trip> getTripsByFlightAndDate(int FlightId, Calendar date)
    {
        String query = "SELECT * FROM trips WHERE FlightId = ? AND DATE(Date) = ?";
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, FlightId);
            ps.setString(2, sdf.format(date.getTime()));
            ResultSet rs = ps.executeQuery();
            ArrayList<Trip> output = new ArrayList<>();
            while (rs.next())
            {
                Calendar c = Calendar.getInstance();
                c.setTime(sdf2.parse(rs.getString("Date")));
                output.add(new Trip(
                        rs.getInt("TripId"),
                        rs.getInt("FlightId"),
                        c,
                        rs.getDouble("Price"),
                        rs.getInt("Status")));
            }
            return output;
        }
        catch (SQLException | ParseException e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }


    public ArrayList<Trip> getRoundTrips(int FlightId, Calendar departDate, Calendar returnDate)
    {
        String Source = getFlightSrc(FlightId);
        String Destination = getFlightDest(FlightId);
        int reverseId = getFlightId(Destination, Source);
        String query = "SELECT t1.TripId, t1.FlightId, t1.Date AS Depart, t2.Date AS Return, " +
                "t1.Price AS P1, t2.Price AS P2, t1.Status FROM trips t1 INNER JOIN trips t2 " +
                "WHERE t1.FlightId = ? AND t2.FlightId = ? AND DATE(t1.Date) = ? AND DATE(t2.Date) = ?";
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, FlightId);
            ps.setInt(2, reverseId);
            ps.setString(3, sdf.format(departDate.getTime()));
            ps.setString(4, sdf.format(returnDate.getTime()));
            ResultSet rs = ps.executeQuery();
            ArrayList<Trip> output = new ArrayList<>();
            while (rs.next())
            {
                Calendar c1 = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                c1.setTime(sdf2.parse(rs.getString("Depart")));
                c2.setTime(sdf2.parse(rs.getString("Return")));
                output.add(new Trip(rs.getInt("TripId"),
                        rs.getInt("FlightId"),
                        c1,
                        c2,
                        rs.getDouble("P1") + rs.getDouble("P2"),
                        rs.getInt("Status")));
            }
            return output;
        }
        catch (SQLException | ParseException e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Trip getTripInfo(int TripId)
    {
        String query = "SELECT * FROM trips WHERE TripId = " + TripId;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            rs.next();
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(rs.getString("Date")));
            return new Trip(rs.getInt("TripId"), rs.getInt("FlightId"), c,
                    rs.getDouble("Price"), rs.getInt("Status"));
        }
        catch (SQLException | ParseException e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /*

    Flight methods

     */

    // Returns flight id associated with Source and Destination cities, or -1 on error
    public int getFlightId(String Source, String Destination)
    {
        String query = "SELECT FlightId FROM flights WHERE Source = '" + Source + "' AND Destination = '" + Destination + "'";
        int id;
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
        if (FlightId < 0) return -1;
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

    /* return the Flight's Source, or null for nonexistant flightId */
    public String getFlightSrc(int FlightId) {
        if (FlightId < 0) return null;

        String query = "SELECT Source FROM flights WHERE FlightId = " + FlightId;
        try
        {
            ResultSet rs = st.executeQuery(query);
            // Should only ever return one entry
            rs.next();
            return rs.getString("Source");
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /* returns the Flight's Destination, or null for nonexistant flightId */
    public String getFlightDest(int FlightId) {
        if (FlightId < 0) return null;

        String query = "SELECT Destination FROM flights WHERE FlightId = "  + FlightId;
        try
        {
            ResultSet rs = st.executeQuery(query);
            // Should only ever return one entry
            rs.next();
            return rs.getString("Destination");
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return null;
        }

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
            entry.add(rs.getString("EncryptedPassword"));
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
            if (ps.executeUpdate() == 0) return -1;
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

    /* Gets employee information from database, returns null if not found */
    public List<String> getEmployeeInfo(String Username)
    {
        String query = "SELECT * FROM employees WHERE Username = ?";
        List<String> entry = new ArrayList<>();
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, Username);
            ResultSet rs = ps.executeQuery();
            if (!rs.next())
            {
                return null;
            }
            entry.add(rs.getString("Username"));
            entry.add(rs.getString("EncryptedPassword"));
            entry.add(rs.getString("FirstName"));
            entry.add(rs.getString("LastName"));
            return entry;
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Attempts to add employee account to database. Returns 0 on success, -1 on error
    public int addEmployeeAccount(String Username, String EncryptedPassword, String FirstName, String LastName)
    {
        String query = "INSERT INTO employees (Username, EncryptedPassword, FirstName, LastName) values (?,?,?,?)";
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
        String query = "UPDATE employees SET EncryptedPassword = ?, FirstName = ?, LastName = ? WHERE Username = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, EncryptedPassword);
            ps.setString(2, FirstName);
            ps.setString(3, LastName);
            ps.setString(4, Username);
            if (ps.executeUpdate() == 0) return -1;
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
        String query = "DELETE FROM employees WHERE Username = '" + Username + "'";
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

    public ArrayList<Employee> getAllEmployees(String currentEmployee)
    {
        String query = "SELECT * FROM employees";
        ArrayList<Employee> output = new ArrayList<>();
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                Employee e = new Employee(rs.getString("Username"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"));

                if (!e.getUsername().equals(currentEmployee)) {
                    output.add(e);
                }

            }
            return output;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /*

    Ticket methods

     */

    // Adds a ticket to the database. Returns 0 on success, -1 on error
    public int addTicket(String Username, int TripId, int SeatNumber, int NumBags, boolean CheckedIn)
    {
        if (TripId < 0 || SeatNumber < 0 || SeatNumber > 19) return -1;
        String query = "INSERT INTO tickets (Username, TripId, SeatNumber, NumBags, CheckedIn) values (?,?,?,?,?)";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, Username);
            ps.setInt(2, TripId);
            ps.setInt(3, SeatNumber);
            ps.setInt(4, NumBags);
            ps.setBoolean(5, CheckedIn);
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
        return addTicket(Username, TripId, SeatNumber, 0, false);
    }

    // Edits existing customer and changes SeatNumber and CheckedIn, returns -1 on error
    public int editTicket(String Username, int TripId, int SeatNumber, int NumBags, boolean CheckedIn)
    {
        if (TripId < 0 || SeatNumber > 19 || SeatNumber < 0) return -1;
        String query = "UPDATE tickets SET SeatNumber = ?, NumBags = ?, CheckedIn = ? WHERE Username = ? AND TripId = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, SeatNumber);
            ps.setInt(2, NumBags);
            ps.setBoolean(3, CheckedIn);
            ps.setString(4, Username);
            ps.setInt(5, TripId);
            if (ps.executeUpdate() == 0) return -1;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return 0;
    }

    // Attempts to remove ticket from database. Returns 0 on success, -1 on error
    public int removeTicket(String Username, int TripId, int SeatNumber)
    {
        if (TripId < 0) return -1;
        String query = "DELETE FROM tickets WHERE Username = ? AND TripId = ? AND SeatNumber = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, Username);
            ps.setInt(2, TripId);
            ps.setInt(3, SeatNumber);
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
        if (TripId < 0) return -1;
        String query = "UPDATE tickets SET CheckedIn = ? WHERE Username = ? AND TripId = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setBoolean(1, true);
            ps.setString(2, Username);
            ps.setInt(3, TripId);
            if (ps.executeUpdate() == 0) return -1;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return 0;
    }

    // Set number of bags for customer's ticket. Returns 0 on success, -1 on error
    public int setBags(String Username, int TripId, int NumBags)
    {
        if (TripId < 0) return -1;
        String query = "UPDATE tickets SET NumBags = ? WHERE Username = ? AND TripId = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, NumBags);
            ps.setString(2, Username);
            ps.setInt(3, TripId);
            if (ps.executeUpdate() == 0) return -1;

        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return 0;
    }

    public ArrayList<Integer> getFullSeats(int TripId)
    {
        if (TripId < 0) return null;
        String query = "SELECT SeatNumber FROM tickets WHERE TripId = " + TripId;
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<Integer> output = new ArrayList<>();
            while (rs.next())
            {
                output.add(rs.getInt(1));
            }
            return output;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public ArrayList<Ticket> getTicketsByUsername(String Username) {

        String query = "SELECT * FROM tickets WHERE Username = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, Username);

            ResultSet rs = ps.executeQuery();
            ArrayList<Ticket> output = new ArrayList<>();

            while (rs.next()) {
                Ticket temp = new Ticket(
                        rs.getInt("TripID"),
                        rs.getInt("SeatNumber"),
                        rs.getInt("NumBags"),
                        rs.getBoolean("CheckedIn")
                );

                temp.completeInfo(temp.getTripId());

                output.add(temp);
            }

            return output;

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public int getSelectedTripID(Trip selectedTrip, String from, String to) {

        if (selectedTrip != null) {

            int flightID = uniqueInstance.getFlightId(from, to);

            Calendar date = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM HH:mm:ss z yyyy", Locale.ENGLISH);

            try {
                date.setTime(sdf.parse(selectedTrip.getDateString()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return uniqueInstance.getTripId(flightID, date);
        }

        return 0;
    }

}
