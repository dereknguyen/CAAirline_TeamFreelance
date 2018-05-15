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


        // overwrites first entry
        db.addFlight(5, d1);
        db.addFlight(3, d2);

        db.addTicket("nparra", db.getFlightId(5, d1), 15);
        db.addTicket("nparra2", db.getFlightId(5, d1), 16);
        db.addTicket("nparra3", db.getFlightId(3, d2), 2);

        db.addEmployeeAccount("emp1", "Joe", "Smith");
        db.addEmployeeAccount("emp2", "Bryan", "Jones");

        db.addCustomerAccount("nparra", "Nick", "Parra");
        db.addCustomerAccount("nparra2", "Hi", "World");
        db.addCustomerAccount("nparra3", "Hello", "Again");

        // Correctly returns valid flight ID and -1 for nonexistent
        //System.out.println(db.getFlightId(5, d));
        //System.out.println(db.getFlightId(4,d));

        // Currently removes all lines from file, not working
        /*db.removeEmployee("emp1");
        db.removeCustomer("nparra");
        db.removeTicket("nparra", db.getFlightId(5, d));
        db.removeFlight(db.getFlightId(5, d));*/
    }
}
