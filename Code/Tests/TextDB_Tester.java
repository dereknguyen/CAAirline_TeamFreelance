package Tests;

import src.MD5Password;
import src.Text_Database;

import java.sql.Date;
import java.util.Calendar;

public class TextDB_Tester
{
    public static void main(String[] args)
    {
        Text_Database db = Text_Database.getInstance();
        Calendar c = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.DATE, 14);
        Date d1 = new Date(c.getTime().getTime());
        Date d2 = new Date(c2.getTime().getTime());

        db.addFlight(5, d1);
        db.addFlight(3, d2);

        db.addCustomerAccount("nparra", "abc", "Nick", "Parra");
        db.addCustomerAccount("nparra2", "def", "Hi", "World");
        db.addCustomerAccount("nparra3", "hij", "Hello", "Again");

        db.addCustomerAccount("user", MD5Password.encodePassword("password"), "Tester", "User");

        db.addEmployeeAccount("emp1", "Joe", "Smith");
        db.addEmployeeAccount("emp2", "Bryan", "Jones");

        db.addTicket("nparra", db.getFlightId(5, d1), 15);
        db.addTicket("nparra2", db.getFlightId(5, d1), 16);
        db.addTicket("nparra3", db.getFlightId(3, d2), 2);

        System.out.println(db.getFlightId(5, d1)); // should be 0
        System.out.println(db.getFlightId(3,d1)); // should be -1
        System.out.println(db.getNumFlights(null, null)); // should be 2
        System.out.println(db.getStatus(db.getFlightId(5, d1))); // should be 0
        System.out.println(db.calculateAvgEmpty(5)); // should be 20.0

        db.setStatus(0, 1);
        System.out.println(db.getStatus(0)); // should be 1

        // TODO have to manually check edits for now, possibly add more getter functions in future
        db.editFlight(1, 0, d2, 5, 50.0);
        db.editCustomer("nparra", "newpass", "New", "Name");
        db.editEmployee("emp2", "New", "Name");
        db.editTicket("nparra2", 0, 17, true);
        db.checkIn("nparra3", 1);

        db.removeFlight(0);
        db.removeFlight(1);

        db.removeCustomer("nparra2");
        db.removeCustomer("nparra");
        db.removeCustomer("nparra3");

        db.removeEmployee("emp1");
        db.removeEmployee("emp2");

        db.removeTicket("nparra3", 3);
        db.removeTicket("nparra2", 5);
        db.removeTicket("nparra", 5);
    }
}
