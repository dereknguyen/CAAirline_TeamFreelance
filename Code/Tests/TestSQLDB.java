package Tests;

import org.junit.Test;
import src.Database;
import src.SQL_Database;
import src.Trip;
import java.util.ArrayList;

import java.util.Calendar;
import static org.junit.Assert.*;

public class TestSQLDB
{
    private Database db = SQL_Database.getInstance();

    @Test
    public void testGetAllFlights(){
        ArrayList<Trip> trips = db.getAllTrips();
        for(int i = 0; i < trips.size(); i++){
            assertNotNull(trips.get(i));
        }

    }
    @Test
    public void testFlightMods()
    {
        int id = db.addFlight("San Luis Obispo", "Seattle");
        assertEquals(id, 2);
        id = db.getFlightId("San Luis Obispo", "Seattle");
        assertEquals(id, 2);
        assertEquals(db.removeFlight(2), 0);
    }

    @Test
    public void testTripMods()
    {
        Calendar c = Calendar.getInstance();
        c.set(2018, Calendar.MAY, 24, 15, 0);
        int id = db.addTrip(0, c, 25.00);
        assertEquals(id, 1);
        id = db.getTripId(0, c);
        assertEquals(id, 1);
        assertEquals(db.setStatus(1, 1), 0);
        assertEquals(db.getStatus(1), 1);
        assertEquals(db.editTrip(1, 0, c, 30.00, 0), 0);
        assertEquals(db.removeTrip(1), 0);
    }

    @Test
    public void testTicketMods()
    {
        assertEquals(db.addTicket("nparra", 0, 15, false), 0);
        assertEquals(db.checkIn("nparra", 0), 0);
        assertEquals(db.editTicket("nparra", 0, 9, false), 0);
        assertEquals(db.removeTicket("nparra", 0, 9), 0);
    }

    @Test
    public void testCustomerMods()
    {
        assertEquals(db.addCustomerAccount("nparra2",
                "abc", "Nicolas", "Parra"), 0);
        assertEquals(db.editCustomer("nparra2",
                "abc", "Nick", "Parra"), 0);
        assertEquals(db.removeCustomer("nparra2"), 0);
    }

    @Test
    public void testEmployeeMods()
    {
        assertEquals(db.addEmployeeAccount("emp1",
                "abc", "Bob", "Smith"), 0);
        assertEquals(db.editEmployee("emp1",
                "newpass", "Bob", "Smith"), 0);
        assertEquals(db.removeEmployee("emp1"), 0);
    }

    @Test
    public void testAvgEmpty()
    {
        //Note: test will fail if existing entries for flight with destination phoenix
        Calendar c = Calendar.getInstance();
        int trip1 = db.addTrip(0 , c, 25.00);
        c.add(Calendar.DAY_OF_WEEK, -1);
        int trip2 = db.addTrip(0, c, 20.00);
        // future flights should not be counted in calculation
        c.add(Calendar.DAY_OF_WEEK, 7);
        int trip3 = db.addTrip(0, c, 30.00);
        for (int i = 0; i < 10; i++)
        {
            db.addTicket("nparra", 1, i, true);
        }
        for (int i = 0; i < 10; i++)
        {
            db.addTicket("nparra", 2, i, true);
        }
        assertEquals(db.calculateAvgEmpty("Phoenix"), 10, 0.001);
        for (int i = 0; i < 10; i++)
        {
            db.removeTicket("nparra", 1, i);
            db.removeTicket("nparra", 2, i);
        }
        db.removeTrip(trip1);
        db.removeTrip(trip2);
        db.removeTrip(trip3);
    }
}
