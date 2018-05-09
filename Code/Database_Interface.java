package Code;

import java.sql.*;

/*

Tables:

customers
    Username (unique) String
    FirstName String
    LastName String

employees
    Username (unique) String
    FirstName String
    LastName String

flights
    Id (unique) int
    DestinationId int    -- (DestinationId, Date) is unique
    Date (mysql Date type)
    FullSeats int
    Price double
    Status int

reservations
    CustomerUsername String
    FlightId  int        -- (CustomerUsername, FlightId) is unique
    SeatNumber int
    CheckedIn boolean

 */

//TODO text file implementation

public class Database_Interface {
    static private Database_Interface uniqueInstance;
    private Connection conn;
    private Statement st;

    private Database_Interface(){
        String driver = "org.gjt.mm.mysql.Driver";
        String url = "database_url";
        try
        {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, "username", "password");
            st = conn.createStatement();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }

    static public Database_Interface getInstance()
    {
        if (uniqueInstance == null)
        {
            uniqueInstance = new Database_Interface();
        }
        return uniqueInstance;
    }

    // Returns flight id associated with DestinationId and Date, or a new id if it doesn't exist
    private int getFlightId(int DestinationId, Date date)
    {
        String query = "SELECT Id FROM flights WHERE DestinationId = " + DestinationId + " AND Date = " + date;
        int id = -1;
        try
        {

            ResultSet rs = st.executeQuery(query);
            // Should only ever return one entry
            rs.next();
            id = rs.getInt("Id");
        }
        catch (Exception e)
        {
            // No matching flight found, return a new ID
            return getNumFlights(null, null);
        }
        return id;
    }

    // Returns the number of flights that occurred between two dates (inclusive)
    // If both dates are null, returns number of flights in database
    private int getNumFlights(Date from, Date to)
    {
        String query = "SELECT COUNT(*) FROM (SELECT * FROM flights " +
                "WHERE Date BETWEEN " + from + "AND" + to + ") AS count";
        if (from == null && to == null)
        {
            query = "SELECT COUNT(*) FROM (SELECT * FROM flights) AS count";
        }
        int NumFlights = 0;
        try
        {
            ResultSet rs = st.executeQuery(query);
            // Should only ever return one entry
            rs.next();
            NumFlights = rs.getInt("count");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return NumFlights;
    }

    // Overload for simplicity
    private int getNumFlights()
    {
        return getNumFlights(null, null);
    }

    // Gets status of flight by ID, 0 = On-time, 1 = Delayed, 2 = Cancelled, Returns -1 on invalid ID
    public int getStatus(int DestinationId, Date date)
    {
        int flightId = getFlightId(DestinationId, date);
        String query = "SELECT Status FROM flights WHERE Id = " + flightId;
        int status;
        try
        {
            ResultSet rs = st.executeQuery(query);
            rs.next();
            status = rs.getInt("Status");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return status;
    }

    // Attempts to add customer account to database. Returns 0 on success, 1 on error.
    public int addCustomerAccount(String Username, String FirstName, String LastName)
    {
        String query = "INSERT INTO customers (Username, FirstName, LastName) values (?,?,?)";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, Username);
            ps.setString(2, FirstName);
            ps.setString(3, LastName);
            ps.execute();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Attempts to add employee account to database. Returns 0 on success, 1 on error
    public int addEmployeeAccount(String Username, String FirstName, String LastName)
    {
        String query = "INSERT INTO employees (Username, FirstName, LastName) values (?,?,?)";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, Username);
            ps.setString(2, FirstName);
            ps.setString(3, LastName);
            ps.execute();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Edits existing customer and sets values to parameters passed (cannot change username). Returns 1 on error
    public int editCustomer(String FirstName, String LastName, String Username)
    {
        String query = "UPDATE customers SET FirstName = ?, LastName = ? WHERE Username = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, FirstName);
            ps.setString(2, LastName);
            ps.setString(3, Username);
            ps.execute();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Edits existing customer and sets values to parameters passed (cannot change username). Return 1 on error
    public int editEmployee(String FirstName, String LastName, String Username)
    {
        String query = "UPDATE employees SET FirstName = ?, LastName = ? WHERE Username = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, FirstName);
            ps.setString(2, LastName);
            ps.setString(3, Username);
            ps.execute();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Attempts to remove customer account to database. Returns 0 on success, 1 on error
    public int removeCustomer(String Username)
    {
        String query = "DELETE FROM customers WHERE Username = " + Username;
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.execute();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Attempts to remove employee account to database. Returns 0 on success, 1 on error
    public int removeEmployee(String Username)
    {
        String query = "DELETE FROM employees WHERE Username " + Username;
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.execute();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Attempts to add flight to database. Returns 0 on success, 1 on error
    // Defaults status to 0 (On-time)
    public int addFlight(int DestinationId, Date date, int FullSeats, double Price)
    {
        String query = "INSERT INTO flights (Id, DestinationId, Date, FullSeats, Price, Status) values (?,?,?,?,?,0)";
        int Id = getNumFlights();
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, Id);
            ps.setInt(2, DestinationId);
            ps.setDate(3, date);
            ps.setInt(4, FullSeats);
            ps.setDouble(5, Price);
            ps.execute();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Defaults FullSeats to 0 and Price to 0
    public int addFlight(int DestinationId, Date date)
    {
        return addFlight(DestinationId, date, 0, 0);
    }

    // Attempts to remove flight from database. Returns 0 on success, 1 on error
    public int removeFlight(int DestinationId, Date date)
    {
        String query = "DELETE FROM flights WHERE Id = ?";
        int Id = getFlightId(DestinationId, date);
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, Id);
            ps.execute();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Set status of flight by ID, 0 = On-time, 1 = Delayed, 2 = Cancelled, Return 0 on success, 1 on error
    public int setStatus(int DestinationId, Date date, int Status)
    {
        String query = "UPDATE flights SET Status = ? WHERE FlightId = ?";
        int Id = getFlightId(DestinationId, date);
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, Status);
            ps.setInt(2, Id);
            ps.execute();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Sets a flight's price. Returns 0 on success, 1 on error
    public int setPrice(int DestinationId, Date date, double Price)
    {
        String query = "UPDATE flights SET Price = ? WHERE FlightId = ?";
        int Id = getFlightId(DestinationId, date);
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setDouble(1, Price);
            ps.setInt(2, Id);
            ps.execute();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Calculates average number of empty seats using previous two weeks of flight information
    public double calculateAvgEmpty(int DestinationId)
    {
        String query = "SELECT * FROM flights WHERE Date BETWEEN DATESUB(CURDATE(), " +
                "INTERVAL 2 WEEK) AND CURDATE() AND DestinationId = " + DestinationId;
        double total = 0;
        double count;
        try
        {
            ResultSet rs = st.executeQuery(query);
            while (rs.next())
            {
                total += 20 - rs.getInt("FullSeats");
            }
            count = rs.getRow();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return 0.0;
        }
        return total / count;
    }

    // Attempts to reserve flight seat for user. Returns 0 on success, 1 on error
    public int reserveSeat(String CustomerUsername, int FlightId, int SeatNumber)
    {
        String query = "INSERT INTO reservations (CustomerUsername, FlightId, SeatNumber, CheckedIn) values (?,?,?,?)";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, CustomerUsername);
            ps.setInt(2, FlightId);
            ps.setInt(3, SeatNumber);
            ps.setBoolean(4, false);
            ps.execute();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Attempts to check user in for flight. Returns 0 on success, 1 on error
    public int flightCheckIn(String CustomerUsername, int FlightId)
    {
        String query = "UPDATE reservations SET CheckedIn = ? WHERE CustomerUsername = ? AND FlightId = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setBoolean(1, true);
            ps.setString(2, CustomerUsername);
            ps.setInt(3, FlightId);
            ps.execute();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }
}
