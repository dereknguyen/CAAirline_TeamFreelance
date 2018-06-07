package Tests;

import org.junit.Assert;
import org.junit.Test;
import src.Database;
import src.SQL_Database;
import src.Trip;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        assertEquals("Check addflight:", 3, id);
        id = db.getFlightId("San Luis Obispo", "Seattle");
        assertEquals("Check getflightID:",3, id);
        assertEquals("check getFlightSrc: ", "San Luis Obispo", db.getFlightDest(3));
        assertEquals("check getFlightDest: ", "Seattle", db.getFlightDest(3));
        assertEquals("Check removeflight:",0, db.removeFlight(2));
    }

    @Test
    public void testTripMods()
    {
        Calendar c = Calendar.getInstance();
        c.set(2018, Calendar.MAY, 24, 15, 0);
        int id = db.addTrip(0, c, 25.00);
        assertEquals("addTrip: ", 1, id);
        id = db.getTripId(0, c);
        assertEquals("getTrip: ", 1, id);
        assertEquals("getStatus: ", 0, db.getStatus(1), 1);
        assertEquals("editTrip: ", 0, db.editTrip(1, 0, c, 30.00, 0));
        assertEquals("removeTrip: ", 0, db.removeTrip(1));
    }

    @Test
    public void testTicketMods()
    {
        assertEquals("add: ", 0, db.addTicket("nparra", 100, 15, 0,false));
        assertEquals("checkin: ",0, db.checkIn("nparra", 100));
        assertEquals("edit: ",0, db.editTicket("nparra", 100, 9, 0,false));
        assertEquals("remove: ",0, db.removeTicket("nparra", 100, 9));
    }

    @Test
    public void testCustomerMods()
    {
        assertEquals(0, db.addCustomerAccount("nparra2",
                "abc", "Nicolas", "Parra"));
        assertEquals(0, db.editCustomer("nparra2",
                "abc", "Nick", "Parra"));
        assertEquals(0, db.removeCustomer("nparra2"));
    }

    @Test
    public void testEmployeeMods()
    {
        int i = 0;
        db.addEmployeeAccount("nparra2", "abc", "Nicolas", "Parra");
        List<String> emp = db.getEmployeeInfo("nparra2");
        Assert.assertEquals(emp.get(i++), "nparra2");
        Assert.assertEquals(emp.get(i++), "Nicolas");
        Assert.assertEquals(emp.get(i), "Parra");
        db.removeEmployee("nparra2");

        assertEquals(0, db.addEmployeeAccount("emp1",
                "abc", "Bob", "Smith"));
        assertEquals(0, db.editEmployee("emp1",
                "newpass", "Bob", "Smith"));
        assertEquals(0, db.removeEmployee("emp1"));
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
            db.addTicket("nparra", 101, i, 0,true);
        }
        for (int i = 0; i < 10; i++)
        {
            db.addTicket("nparra", 102, i, 0,true);
        }
        assertEquals(10, db.calculateAvgEmpty("Phoenix"), 0.001);
        for (int i = 0; i < 10; i++)
        {
            db.removeTicket("nparra", 101, i);
            db.removeTicket("nparra", 102, i);
        }
        db.removeTrip(trip1);
        db.removeTrip(trip2);
        db.removeTrip(trip3);
    }

    @Test
    public void testgetNumTrips()
    {
        Calendar c = Calendar.getInstance();
        int trip1 = db.addTrip(0 , c, 25.00);
        int trip2 = db.addTrip(1, c, 20.00);
        int trip3 = db.addTrip(2, c, 30.00);

        assertEquals("getNumTrips: ", 3, db.getNumTrips(c, c));
        db.removeTrip(trip1);
        db.removeTrip(trip2);
        db.removeTrip(trip3);
    }

    @Test
    public void testGetAVGRev() {
        assertEquals("avgrev: ", 435.25, db.getAvgRevenue(0));
    }

    @Test
    public void testTripRetrieve() {
        Calendar c = Calendar.getInstance();

        int trip1 = db.addTrip(0 , c, 25.00);
        assertEquals("getFlightIdFromTrip: ", 0, db.getFlightIdFromTrip(trip1));
        ArrayList<Trip> trips = db.getTripsByFlightAndDate(0, c);
            for(int i = 0; i < trips.size(); i++){
                assertNotNull(trips.get(i));
            }
        Trip info = db.getTripInfo(trip1);
        assertEquals("getTripInfo(tripid): ", info.getTripId(), trip1);
        assertEquals("getTripInfo(flightid): ", info.getFlightId(), 0);
        assertEquals("getTripInfo: ", info.getDate(), c);
        db.removeTrip(trip1);
    }




}
