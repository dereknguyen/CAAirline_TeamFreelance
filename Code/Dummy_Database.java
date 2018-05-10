package Code;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.*;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/*
Temporary class that reads from a text file instead of a real database
Only supports adding new entries and reading existing ones

file format:

customers
    Username, FirstName, LastName

employees
    Username, FirstName, LastName

flights
    Id, DestinationId, Date, FullSeats, Price, Status

tickets
    Username, FlightId, SeatNumber, CheckedIn

 */

public class Dummy_Database {
    static private Dummy_Database uniqueInstance;
    private String customersfile = "customers.txt";
    private String employeesfile = "employees.txt";
    private String flightsfile = "flights.txt";
    private String ticketsfile = "tickets.txt";
    private Scanner customers;
    private Scanner employees;
    private Scanner flights;
    private Scanner tickets;
    private PrintWriter c_out;
    private PrintWriter e_out;
    private PrintWriter f_out;
    private PrintWriter t_out;

    private Dummy_Database()
    {
        try
        {
            customers = new Scanner(new FileInputStream(customersfile));
            employees = new Scanner(new FileInputStream(employeesfile));
            flights = new Scanner(new FileInputStream(flightsfile));
            tickets = new Scanner(new FileInputStream(ticketsfile));
            c_out = new PrintWriter(customersfile, "UTF-8");
            e_out = new PrintWriter(employeesfile, "UTF-8");
            f_out = new PrintWriter(flightsfile, "UTF-8");
            t_out = new PrintWriter(ticketsfile, "UTF-8");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    static public Dummy_Database getInstance()
    {
        if (uniqueInstance == null)
        {
            uniqueInstance = new Dummy_Database();
        }
        return uniqueInstance;
    }

    // Returns flight id associated with DestinationId and Date, or a new id if it doesn't exist
    public int getFlightId(int DestinationId, Date date)
    {
        List<String> entry = new ArrayList<>();
        String line;
        flights.reset();

        while (flights.hasNextLine())
        {
            line = flights.nextLine();
            entry = Arrays.asList(line.split("\\s*,\\s*"));
            if (Integer.parseInt(entry.get(1)) == DestinationId && (entry.get(2)).equals(date.toString()))
            {
                return Integer.parseInt(entry.get(0));
            }
        }
        return Integer.parseInt(entry.get(0))+1;
    }

    // Returns the number of flights that occurred between two dates (inclusive)
    // If both dates are null, returns number of flights in database
    // NOTE: Dummy version does not check dates
    private int getNumFlights(Date from, Date to)
    {
        String line;
        flights.reset();
        int count = 0;

        while (flights.hasNextLine())
        {
            line = flights.nextLine();
            count ++;
        }
        return count;
    }

    // Overload for simplicity
    private int getNumFlights()
    {
        return getNumFlights(null, null);
    }

    // Gets status of flight by ID, 0 = On-time, 1 = Delayed, 2 = Cancelled, Returns -1 on invalid ID
    public int getStatus(int DestinationId, Date date)
    {
        List<String> entry;
        String line;
        flights.reset();
        int status = -1;

        while (flights.hasNextLine())
        {
            line = flights.nextLine();
            entry = Arrays.asList(line.split("\\s*,\\s*"));
            if (Integer.parseInt(entry.get(1)) == DestinationId && entry.get(2).equals(date.toString()))
            {
                status = Integer.parseInt(entry.get(0));
            }
        }
        return status;
    }

    // Attempts to add customer account to database. Returns 0 on success, 1 on error.
    // NOTE: dummy version will not enforce unique usernames
    public int addCustomerAccount(String Username, String FirstName, String LastName)
    {
        String line = Username + ", " + FirstName + ", " + LastName;
        c_out.println(line);
        return 0;
    }

    // Attempts to add employee account to database. Returns 0 on success, 1 on error
    // NOTE: dummy version will not enforce unique usernames
    public int addEmployeeAccount(String Username, String FirstName, String LastName)
    {
        String line = Username + ", " + FirstName + ", " + LastName;
        e_out.println(line);
        return 0;
    }

    // Attempts to add ticket to database. Returns 0 on success, 1 on error
    // NOTE: dummy version will not enforce one ticket per customer per flight
    public int addTicket(String Username, int FlightId, int SeatNumber, boolean CheckedIn)
    {
        String line = Username + ", " + FlightId + ", " + SeatNumber + ", " + CheckedIn;
        t_out.println(line);
        return 0;
    }

    // Overload for simplicity
    public int addTicket(String Username, int FlightId, int SeatNumber)
    {
        return addTicket(Username, FlightId, SeatNumber, false);
    }

    // Edits existing customer and sets values to parameters passed (cannot change username). Returns 1 on error
    // NOTE: not implemented in dummy version, use add/remove instead
    public int editCustomer(String FirstName, String LastName, String Username)
    {
        return 0;
    }

    // Edits existing customer and sets values to parameters passed (cannot change username). Return 1 on error
    // NOTE: not implemented in dummy version
    public int editEmployee(String FirstName, String LastName, String Username)
    {
        return 0;
    }

    // Attempts to remove customer account to database. Returns 0 on success, 1 on error
    // NOTE: not implemented in dummy version
    public int removeCustomer(String Username)
    {
        return 0;
    }

    // Attempts to remove employee account to database. Returns 0 on success, 1 on error
    // NOTE: not implemented in dummy version
    public int removeEmployee(String Username)
    {
        return 0;
    }

    // Attempts to remove ticket from database. Returns 0 on success, 1 on error
    // NOTE: not implemented in dummy version
    public int removeTicket(String Username, int FlightId)
    {

        return 0;
    }

    // Attempts to add flight to database. Returns 0 on success, 1 on error
    // Defaults status to 0 (On-time)
    public int addFlight(int DestinationId, Date date, int FullSeats, double Price)
    {
        int id = getNumFlights();
        String line = id + ", " + DestinationId + ", " + date + ", " + FullSeats + ", " + Price + ", 0";
        c_out.println(line);
        return 0;
    }

    // Defaults FullSeats to 0 and Price to 0
    public int addFlight(int DestinationId, Date date)
    {
        return addFlight(DestinationId, date, 0, 0);
    }

    // Attempts to remove flight from database. Returns 0 on success, 1 on error
    // NOTE: not implemented in dummy version
    public int removeFlight(int DestinationId, Date date)
    {
        return 0;
    }

    // Set status of flight by ID, 0 = On-time, 1 = Delayed, 2 = Cancelled, Return 0 on success, 1 on error
    // NOTE: not implemented in dummy version
    public int setStatus(int DestinationId, Date date, int Status)
    {
        return 0;
    }

    // Sets a flight's price. Returns 0 on success, 1 on error
    // NOTE: not implemented in dummy version
    public int setPrice(int DestinationId, Date date, double Price)
    {
        return 0;
    }

    // Calculates average number of empty seats using previous two weeks of flight information
    public double calculateAvgEmpty(int DestinationId)
    {
        List<String> entry;
        String line;
        flights.reset();
        double total = 0;
        double count = 0;

        while (flights.hasNextLine())
        {
            line = flights.nextLine();
            entry = Arrays.asList(line.split("\\s*,\\s*"));
            if (Integer.parseInt(entry.get(1)) == DestinationId)
            {
                total += 20 - Integer.parseInt(entry.get(3));
                count ++;
            }
        }
        return total / count;
    }

    // Attempts to reserve flight seat for user. Returns 0 on success, 1 on error
    // NOTE: not implemented in dummy version
    public int reserveSeat(String Username, int FlightId, int SeatNumber)
    {
        return 0;
    }

    // Attempts to check user in for flight. Returns 0 on success, 1 on error
    // NOTE: not implemented in dummy version
    public int flightCheckIn(String Username, int FlightId)
    {
        return 0;
    }
}
