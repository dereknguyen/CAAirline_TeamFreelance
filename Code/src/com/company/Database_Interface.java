package com.company;
import com.sun.tools.doclets.internal.toolkit.util.ClassUseMapper;

import java.sql.*;

public class Database_Interface {
    private Connection conn;
    private Statement st;

    public Database_Interface(){
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

    public String getCustomerById(int id)
    {
        String query = "SELECT * FROM customers WHERE Id = " + id;
        String FirstName = "";
        String LastName = "";
        try
        {
            ResultSet rs = st.executeQuery(query);
            // Should only ever return one entry
            rs.next();
            FirstName = rs.getString("FirstName");
            LastName = rs.getString("LastName");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return FirstName + " " + LastName;
    }

    public String getEmployeeById(int id)
    {
        String query = "SELECT * FROM employees WHERE Id = " + id;
        String FirstName = "";
        String LastName = "";
        try
        {
            ResultSet rs = st.executeQuery(query);
            // Should only ever return one entry
            rs.next();
            FirstName = rs.getString("FirstName");
            LastName = rs.getString("LastName");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return FirstName + " " + LastName;
    }

    public String getFlightById(int id)
    {
        String query = "SELECT * FROM flights WHERE Id = " + id;
        int DestinationId = -1;
        String Date = "";
        try
        {
            ResultSet rs = st.executeQuery(query);
            // Should only ever return one entry
            rs.next();
            DestinationId = rs.getInt("DestinationId");
            Date = rs.getString("Date");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return DestinationId + " " + Date;
    }

    // Returns the number of flights that occurred between two dates (inclusive)
    public int getNumFlights(Date from, Date to)
    {
        String query = "SELECT COUNT(*) FROM (SELECT * FROM flights WHERE Date BETWEEN " + from + "AND" + to + ") AS count";
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

    // Attempts to add customer account to database. Returns 0 on success, 1 on error.
    //TODO fields for each customer entry
    public int addCustomerAccount(String FirstName, String LastName, int Id)
    {
        String query = "INSERT INTO customers (FirstName, LastName, Id) values (?,?,?)";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, FirstName);
            ps.setString(2, LastName);
            ps.setInt(3, Id);
            ps.execute();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Edits existing customer by Id and sets values to parameters passed
    public int editCustomer(String FirstName, String LastName, int Id)
    {
        String query = "UPDATE customers SET FirstName = ?, LastName = ? WHERE Id = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, FirstName);
            ps.setString(2, LastName);
            ps.setInt(3, Id);
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
    public int removeCustomer(int Id)
    {
        String query = "DELETE FROM customers WHERE Id = ?";
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

    // Attempts to add employee account to database. Returns 0 on success, 1 on error
    // Will replace/update existing accounts with the same name/id
    public int addEmployeeAccount(String FirstName, String LastName, int Id)
    {
        String query = "INSERT INTO employees (FirstName, LastName, Id) values (?,?,?)";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, FirstName);
            ps.setString(2, LastName);
            ps.setInt(3, Id);
            ps.execute();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Edits existing customer by Id and sets values to parameters passed
    public int editEmployee(String FirstName, String LastName, int Id)
    {
        String query = "UPDATE employees SET FirstName = ?, LastName = ? WHERE Id = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, FirstName);
            ps.setString(2, LastName);
            ps.setInt(3, Id);
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
    public int removeEmployee(int Id)
    {
        String query = "DELETE FROM employees WHERE Id = ?";
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

    // Attempts to add flight to database. Returns 0 on success, 1 on error
    public int addFlight(int Id, int DestinationId, Date date, int FullSeats)
    {
        String query = "INSERT INTO flights (Id, DestinationId, Date, FullSeats) values (?,?,?,?)";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, Id);
            ps.setInt(2, DestinationId);
            ps.setDate(3, date);
            ps.setInt(4, FullSeats);
            ps.execute();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Defaults FullSeats to 0
    public int addFlight(int Id, int DestinationId, Date date)
    {
        return addFlight(Id, DestinationId, date, 0);
    }

    // Attempts to remove flight from database. Returns 0 on success, 1 on erro
    public int removeFlight(int Id)
    {
        String query = "DELETE FROM flights WHERE Id = ?";
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

    // Returns flight id associated with DestinationId and Date, or -1 if it doesn't exist
    public int getFlightId(int DestinationId, Date date)
    {
        String query = "SELECT Id FROM flights WHERE DestinationId = ? AND Date = ?";
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
            System.out.println(e.getMessage());
            return -1;
        }
        return id;
    }

    // Calculates average number of empty seats using previous two weeks of flight information
    public double calculateAvgEmpty()
    {
        String query = "SELECT * FROM flights WHERE Date BETWEEN DATESUB(CURDATE(), INTERVAL 2 WEEK) AND CURDATE()";
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
    public int reserveSeat(int CustomerId, int FlightId, int SeatNumber)
    {
        String query = "INSERT INTO reservations (CustomerId, FlightId, SeatNumber, CheckedIn) values (?,?,?,?)";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, CustomerId);
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
    public int flightCheckIn(int CustomerId, int FlightId)
    {
        String query = "UPDATE reservations SET CheckedIn = ? WHERE CustomerId = ? AND FlightId = ?";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setBoolean(1, true);
            ps.setInt(2, CustomerId);
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
