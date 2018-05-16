package Code;

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

        db.addCustomerAccount("nparra", "Nick", "Parra");
        db.addCustomerAccount("nparra2", "Hi", "World");
        db.addCustomerAccount("nparra3", "Hello", "Again");

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
        //TODO db.editFlight(1, 0, d2, 5, 50.0);
        //TODO db.editCustomer("nparra", "New", "Name");
        //TODO db.editEmployee("emp2", "New", "Name");
        //TODO db.editTicket("nparra2", 0, 17, true);

        //TODO db.checkIn("nparra3", 1);

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
