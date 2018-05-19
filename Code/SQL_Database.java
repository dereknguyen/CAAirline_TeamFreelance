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

tickets
    Username String
    FlightId  int        -- (Username, FlightId, SeatNumber) is unique
    SeatNumber int
    CheckedIn boolean

 */

public class SQL_Database implements Database {
    static private SQL_Database uniqueInstance;
    private Connection conn;
    private Statement st;

    private SQL_Database(){
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

    // Returns flight id associated with DestinationId and Date, or -1 on error
    public int getFlightId(int DestinationId, Date date)
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
        catch (SQLException e)
        {
            e.getMessage();
            return -1;
        }
        return id;
    }

    // Returns the number of flights that occurred between two dates (inclusive) or -1 on error
    // If both dates are null, returns number of flights in database
    public int getNumFlights(Date from, Date to)
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
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return NumFlights;
    }

    // Overload for simplicity
    public int getNumFlights()
    {
        return getNumFlights(null, null);
    }

    // Gets status of flight by ID, 0 = On-time, 1 = Delayed, 2 = Cancelled, Returns -1 on invalid ID
    public int getStatus(int FlightId)
    {
        String query = "SELECT Status FROM flights WHERE Id = " + FlightId;
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

    // Set status of flight by ID, 0 = On-time, 1 = Delayed, 2 = Cancelled, Return 0 on success, 1 on error
    public int setStatus(int FlightId, int Status)
    {
        String query = "UPDATE flights SET Status = ? WHERE FlightId = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, Status);
            ps.setInt(2, FlightId);
            ps.execute();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Attempts to add flight to database. Returns the new flight id, or -1 on error
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
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return Id;
    }

    // Defaults FullSeats to 0 and Price to 0
    public int addFlight(int DestinationId, Date date)
    {
        return addFlight(DestinationId, date, 0, 0);
    }

    // Changes flight's destination, date, fullseats, and price. Returns 0 on success, 1 on error
    public int editFlight(int FlightId, int DestinationId, Date date, int FullSeats, double Price)
    {
        String query = "UPDATE flights SET DestinationId = ?, Date = ?, FullSeats = ?, Price = ? WHERE FlightId = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, DestinationId);
            ps.setDate(2, date);
            ps.setInt(3, FullSeats);
            ps.setDouble(4, Price);
            ps.setInt(5, FlightId);
            ps.execute();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Attempts to remove flight from database. Returns 0 on success, 1 on error
    public int removeFlight(int FlightId)
    {
        String query = "DELETE FROM flights WHERE Id = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, FlightId);
            ps.execute();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Calculates average number of empty seats using previous two weeks of flight information, returns -1 on error
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
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return total / count;
    }


    /*

    Customer methods

     */

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
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Edits existing customer and sets values to parameters passed (cannot change username). Returns 1 on error
    public int editCustomer(String Username, String FirstName, String LastName)
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
        catch (SQLException e)
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
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    /*

    Employee methods

     */

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
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Edits existing customer and sets values to parameters passed (cannot change username). Return 1 on error
    public int editEmployee(String Username, String FirstName, String LastName)
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
        catch (SQLException e)
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

    // Adds a ticket to the database. Returns 0 on success, 1 on error
    public int addTicket(String Username, int FlightId, int SeatNumber, boolean CheckedIn)
    {
        String query = "INSERT INTO tickets (Username, FlightId, SeatNumber, CheckedIn) values (?,?,?,?)";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, Username);
            ps.setInt(2, FlightId);
            ps.setInt(3, SeatNumber);
            ps.setBoolean(4, CheckedIn);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Overload for simplicity
    public int addTicket(String Username, int FlightId, int SeatNumber)
    {
        return addTicket(Username, FlightId, SeatNumber, false);
    }

    // Edits existing customer and changes SeatNumber and CheckedIn
    public int editTicket(String Username, int FlightId, int SeatNumber, boolean CheckedIn)
    {
        String query = "UPDATE tickets SET SeatNumber = ?, CheckedIn = ? WHERE Username = ? AND FlightId = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, SeatNumber);
            ps.setBoolean(2, CheckedIn);
            ps.setString(3, Username);
            ps.setInt(4, FlightId);
            ps.execute();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Attempts to remove ticket from database. Returns 0 on success, 1 on error
    public int removeTicket(String Username, int FlightId)
    {
        String query = "DELETE FROM tickets WHERE Username = ? AND FlightId = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, Username);
            ps.setInt(2, FlightId);
            ps.execute();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Attempts to check user in for flight. Returns 0 on success, 1 on error
    public int checkIn(String Username, int FlightId)
    {
        String query = "UPDATE tickets SET CheckedIn = ? WHERE Username = ? AND FlightId = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setBoolean(1, true);
            ps.setString(2, Username);
            ps.setInt(3, FlightId);
            ps.execute();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }
}
