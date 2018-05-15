package Code;

import java.sql.Date;
import java.util.Calendar;

public class TextDB_Tester
{
    public static void main(String[] args)
    {
        Text_Database db = Text_Database.getInstance();
        Calendar c = Calendar.getInstance();
        Date d = new Date(c.getTime().getTime());

        db.addFlight(0, d);
        db.addTicket("nparra", db.getFlightId(0, d), 15);
        db.addCustomerAccount("nparra", "Nick", "Parra");
        db.addEmployeeAccount("emp1", "Joe", "Smith");
    }
}
