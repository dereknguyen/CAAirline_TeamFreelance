package src;

import java.io.*;
import java.sql.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.StringJoiner;


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

public class Text_Database implements Database {
    static private Text_Database uniqueInstance;
    /*private BufferedReader customers_reader;
    private BufferedReader employees_reader;
    private BufferedReader flights_reader;
    private BufferedReader tickets_reader;

    private Text_Database() {
        try {
            customers_reader = new BufferedReader(new FileReader("customers.txt"));
            employees_reader = new BufferedReader(new FileReader("employees.txt"));
            flights_reader = new BufferedReader(new FileReader("flights.txt"));
            tickets_reader = new BufferedReader(new FileReader("tickets.txt"));

            // NOTE: unpredictable result with text files over 2^16 chars (65536)
            customers_reader.mark(65536);
            employees_reader.mark(65536);
            flights_reader.mark(65536);
            tickets_reader.mark(65536);
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }*/

    static public Text_Database getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Text_Database();
        }
        return uniqueInstance;
    }

    /*

    Flight methods

    */

    // Returns flight id associated with DestinationId and Date, or -1 on error
    public int getFlightId(int DestinationId, Date date)
    {
        String line;
        int id = -1;
        try
        {
            BufferedReader flights_reader = new BufferedReader(new FileReader("src/flights.txt"));
            while ((line = flights_reader.readLine()) != null)
            {
                List<String> entry = Arrays.asList(line.split("\\s*,\\s*"));
                if (Integer.parseInt(entry.get(1)) == DestinationId && (entry.get(2).equals(date.toString())))
                {
                    id = Integer.parseInt(entry.get(0));
                }
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return id;
    }

    // Returns the number of flights that occurred between two dates (inclusive) or -1 on error
    // If both dates are null, returns number of flights in database
    public int getNumFlights(Date from, Date to)
    {
        String line;
        int count = 0;
        try
        {
            BufferedReader flights_reader = new BufferedReader(new FileReader("src/flights.txt"));
            while ((line = flights_reader.readLine()) != null)
            {
                List <String> entry = Arrays.asList(line.split("\\s*,\\s*"));
                if (from == null && to == null)
                {
                    count ++;
                }
                else if (entry.get(2).compareTo(from.toString()) >= 0 && entry.get(2).compareTo(to.toString()) <= 0)
                {
                    count++;
                }

            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return count;
    }

    // Overload for simplicity
    public int getNumFlights()
    {
        return getNumFlights(null, null);
    }

    // Gets status of flight by ID, 0 = On-time, 1 = Delayed, 2 = Cancelled, Returns -1 on invalid ID
    public int getStatus(int FlightId)
    {
        String line;
        int status = -1;
        try
        {
            BufferedReader flights_reader = new BufferedReader(new FileReader("src/flights.txt"));
            while ((line = flights_reader.readLine()) != null)
            {
                List <String> entry = Arrays.asList(line.split("\\s*,\\s*"));
                if (Integer.parseInt(entry.get(0)) == FlightId)
                {
                    status = Integer.parseInt(entry.get(5));
                }
            }
            flights_reader.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return status;
    }

    // Set status of flight by ID, 0 = On-time, 1 = Delayed, 2 = Cancelled, Return 0 on success, 1 on error
    public int setStatus(int FlightId, int Status)
    {
        String line;
        String newline;
        String output = "";
        try
        {
            BufferedReader flights_reader = new BufferedReader(new FileReader("src/flights.txt"));
            while ((line = flights_reader.readLine()) != null)
            {
                List <String> entry = Arrays.asList(line.split("\\s*, \\s*"));
                if (Integer.parseInt(entry.get(0)) == FlightId)
                {
                    StringJoiner joiner = new StringJoiner(", ");
                    joiner.add(entry.get(0)).add(entry.get(1)).add(entry.get(2))
                            .add(entry.get(3)).add(entry.get(4)).add(Integer.toString(Status));
                    newline = joiner.toString() + System.lineSeparator();

                    output += newline;
                }
                else
                {
                    output += line + System.lineSeparator();
                }
            }
            FileWriter writer = new FileWriter("src/flights.txt");
            writer.write(output);
            writer.close();
            flights_reader.close();
        }
        catch (IOException e)
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
        String line;
        int id;
        try
        {
            BufferedReader flights_reader = new BufferedReader(new FileReader("src/flights.txt"));
            FileWriter writer = new FileWriter("src/flights.txt", true);
            while ((line = flights_reader.readLine()) != null);
            StringJoiner joiner = new StringJoiner(", ");
            id = getNumFlights();
            joiner.add(Integer.toString(id)).add(Integer.toString(DestinationId))
                    .add(date.toString()).add(Integer.toString(FullSeats)).add(Double.toString(Price)).add("0");
            String newline = joiner.toString() + System.lineSeparator();

            writer.write(newline);
            writer.close();
            flights_reader.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return id;
    }

    // Defaults FullSeats to 0 and Price to 0
    public int addFlight(int DestinationId, Date date)
    {
        return addFlight(DestinationId, date, 0, 0);
    }

    // Changes flight's destination, date, fullseats, and price. Returns 0 on success, 1 on error
    public int editFlight(int FlightId, int DestinationId, Date date, int FullSeats, double Price)
    {
        String line;
        String newline;
        String output = "";
        try
        {
            BufferedReader flights_reader = new BufferedReader(new FileReader("src/flights.txt"));
            while ((line = flights_reader.readLine()) != null)
            {
                List <String> entry = Arrays.asList(line.split("\\s*, \\s*"));
                if (Integer.parseInt(entry.get(0)) == FlightId)
                {
                    StringJoiner joiner = new StringJoiner(", ");
                    joiner.add(entry.get(0)).add(Integer.toString(DestinationId)).add(date.toString())
                            .add(Integer.toString(FullSeats)).add(Double.toString(Price))
                            .add(entry.get(5));
                    newline = joiner.toString() + System.lineSeparator();

                    output += newline;
                }
                else
                {
                    output += line + System.lineSeparator();
                }
            }
            FileWriter writer = new FileWriter("src/flights.txt");
            writer.write(output);
            writer.close();
            flights_reader.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Attempts to remove flight from database. Returns 0 on success, 1 on error
    public int removeFlight(int FlightId)
    {
        String line;
        String output = "";
        try
        {
            BufferedReader flights_reader = new BufferedReader(new FileReader("src/flights.txt"));
            while ((line = flights_reader.readLine()) != null)
            {
                List <String> entry = Arrays.asList(line.split("\\s*, \\s*"));
                if (Integer.parseInt(entry.get(0)) != FlightId)
                {
                    output += line + System.lineSeparator();
                }
            }
            FileWriter writer = new FileWriter("src/flights.txt");
            writer.write(output);
            writer.close();
            flights_reader.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Calculates average number of empty seats using previous two weeks of flight information, returns -1 on error
    public double calculateAvgEmpty(int DestinationId)
    {
        double total = 0;
        double count = 0;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.DATE, -14);
        Date d1 = new Date(c1.getTime().getTime());
        Date d2 = new Date(c2.getTime().getTime());
        String line;
        try
        {
            BufferedReader flights_reader = new BufferedReader(new FileReader("src/flights.txt"));
            while ((line = flights_reader.readLine()) != null)
            {
                List <String> entry = Arrays.asList(line.split("\\s*, \\s*"));
                if (Integer.parseInt(entry.get(1)) == DestinationId && entry.get(2).compareTo(d1.toString()) <= 0
                        && entry.get(2).compareTo(d2.toString()) >= 0)
                {
                    total += 20 - Integer.parseInt(entry.get(3));
                    count ++;
                }
            }
            flights_reader.close();
        }
        catch (IOException e)
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
        try
        {
            BufferedReader customers_reader = new BufferedReader(new FileReader("src/customers.txt"));
            FileWriter writer = new FileWriter("src/customers.txt", true);
            while (customers_reader.readLine() != null);
            StringJoiner joiner = new StringJoiner(", ");
            joiner.add(Username).add(FirstName).add(LastName);
            String newline = joiner.toString() + System.lineSeparator();

            writer.write(newline);
            writer.close();
            customers_reader.close();;
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return 0;
    }

    // Edits existing customer and sets values to parameters passed (cannot change username). Returns 1 on error
    public int editCustomer(String Username, String FirstName, String LastName)
    {
        String line;
        String newline;
        String output = "";
        try
        {
            BufferedReader customers_reader = new BufferedReader(new FileReader("src/customers.txt"));
            while ((line = customers_reader.readLine()) != null)
            {
                List <String> entry = Arrays.asList(line.split("\\s*, \\s*"));
                if (entry.get(0).equals(Username))
                {
                    StringJoiner joiner = new StringJoiner(", ");
                    joiner.add(Username).add(FirstName).add(LastName);
                    newline = joiner.toString() + System.lineSeparator();

                    output += newline;
                }
                else
                {
                    output += line + System.lineSeparator();
                }
            }
            FileWriter writer = new FileWriter("src/customers.txt");
            writer.write(output);
            writer.close();
            customers_reader.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Attempts to remove customer account to database. Returns 0 on success, 1 on error
    public int removeCustomer(String Username)
    {
        String line;
        String output = "";
        try
        {
            BufferedReader customers_reader = new BufferedReader(new FileReader("src/customers.txt"));
            while ((line = customers_reader.readLine()) != null)
            {
                List <String> entry = Arrays.asList(line.split("\\s*, \\s*"));
                if (!entry.get(0).equals(Username))
                {
                    output += line + System.lineSeparator();
                }
            }
            FileWriter writer = new FileWriter("src/customers.txt");
            writer.write(output);
            writer.close();
            customers_reader.close();
        }
        catch (IOException e)
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
        String line;
        try
        {
            BufferedReader employees_reader = new BufferedReader(new FileReader("src/employees.txt"));
            FileWriter writer = new FileWriter("src/employees.txt", true);
            while ((line = employees_reader.readLine()) != null);
            StringJoiner joiner = new StringJoiner(", ");
            joiner.add(Username).add(FirstName).add(LastName);
            String newline = joiner.toString() + System.lineSeparator();

            writer.write(newline);
            writer.close();
            employees_reader.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
        return 0;
    }

    // Edits existing customer and sets values to parameters passed (cannot change username). Return 1 on error
    public int editEmployee(String Username, String FirstName, String LastName)
    {
        String line;
        String newline;
        String output = "";
        try
        {
            BufferedReader employees_reader = new BufferedReader(new FileReader("src/employees.txt"));
            while ((line = employees_reader.readLine()) != null)
            {
                List <String> entry = Arrays.asList(line.split("\\s*, \\s*"));
                if (entry.get(0).equals(Username))
                {
                    StringJoiner joiner = new StringJoiner(", ");
                    joiner.add(Username).add(FirstName).add(LastName);
                    newline = joiner.toString() + System.lineSeparator();

                    output += newline;
                }
                else
                {
                    output += line + System.lineSeparator();
                }
            }
            FileWriter writer = new FileWriter("src/employees.txt");
            writer.write(output);
            writer.close();
            employees_reader.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Attempts to remove employee account to database. Returns 0 on success, 1 on error
    public int removeEmployee(String Username)
    {
        String line;
        String output = "";
        try
        {
            BufferedReader employees_reader = new BufferedReader(new FileReader("src/employees.txt"));
            while ((line = employees_reader.readLine()) != null)
            {
                List <String> entry = Arrays.asList(line.split("\\s*, \\s*"));
                if (!entry.get(0).equals(Username))
                {
                    output += line + System.lineSeparator();
                }
            }

            FileWriter writer = new FileWriter("src/employees.txt");
            writer.write(output);
            writer.close();
            employees_reader.close();
        }
        catch (IOException e)
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
        String line;
        try
        {
            BufferedReader tickets_reader = new BufferedReader(new FileReader("src/tickets.txt"));
            FileWriter writer = new FileWriter("src/tickets.txt", true);
            while ((line = tickets_reader.readLine()) != null);
            StringJoiner joiner = new StringJoiner(", ");
            joiner.add(Username).add(Integer.toString(FlightId))
                    .add(Integer.toString(SeatNumber)).add(Boolean.toString(CheckedIn));
            String newline = joiner.toString() + System.lineSeparator();

            writer.write(newline);
            writer.close();
            tickets_reader.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return -1;
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
        String line;
        String newline;
        String output = "";
        try
        {
            BufferedReader tickets_reader = new BufferedReader(new FileReader("src/tickets.txt"));
            while ((line = tickets_reader.readLine()) != null)
            {
                List <String> entry = Arrays.asList(line.split("\\s*, \\s*"));
                if (entry.get(0).equals(Username) && Integer.parseInt(entry.get(1)) == FlightId)
                {
                    StringJoiner joiner = new StringJoiner(", ");
                    joiner.add(Username).add(Integer.toString(FlightId))
                            .add(Integer.toString(SeatNumber)).add(Boolean.toString(CheckedIn));
                    newline = joiner.toString() + System.lineSeparator();

                    output += newline;
                }
                else
                {
                    output += line + System.lineSeparator();
                }
            }
            FileWriter writer = new FileWriter("src/tickets.txt");
            writer.write(output);
            writer.close();
            tickets_reader.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Attempts to remove ticket from database. Returns 0 on success, 1 on error
    public int removeTicket(String Username, int FlightId)
    {
        String line;
        String output = "";
        try
        {
            BufferedReader tickets_reader = new BufferedReader(new FileReader("src/tickets.txt"));
            while ((line = tickets_reader.readLine()) != null)
            {
                List <String> entry = Arrays.asList(line.split("\\s*, \\s*"));
                if (!entry.get(0).equals(Username))
                {
                    output += line + System.lineSeparator();
                }
            }
            FileWriter writer = new FileWriter("src/tickets.txt");
            writer.write(output);
            writer.close();
            tickets_reader.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }

    // Attempts to check user in for flight. Returns 0 on success, 1 on error
    public int checkIn(String Username, int FlightId)
    {
        String line;
        String newline;
        String output = "";
        try
        {
            BufferedReader tickets_reader = new BufferedReader(new FileReader("src/tickets.txt"));
            while ((line = tickets_reader.readLine()) != null)
            {
                List <String> entry = Arrays.asList(line.split("\\s*, \\s*"));
                if (entry.get(0).equals(Username) && Integer.parseInt(entry.get(1)) == FlightId)
                {
                    StringJoiner joiner = new StringJoiner(", ");
                    joiner.add(Username).add(Integer.toString(FlightId))
                            .add(entry.get(2)).add(Boolean.toString(true));
                    newline = joiner.toString() + System.lineSeparator();

                    output += newline;
                }
                else
                {
                    output += line + System.lineSeparator();
                }
            }
            FileWriter writer = new FileWriter("src/tickets.txt");
            writer.write(output);
            writer.close();
            tickets_reader.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return 1;
        }
        return 0;
    }
}