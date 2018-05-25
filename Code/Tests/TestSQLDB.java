package Tests;

import org.junit.Test;
import src.Database;
import src.SQL_Database;

import java.util.Calendar;
import static org.junit.Assert.assertEquals;

public class TestSQLDB
{
    private Database db = SQL_Database.getInstance();

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
        assertEquals(db.removeTrip(1), 0);
    }
}
