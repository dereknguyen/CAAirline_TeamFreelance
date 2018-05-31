package Tests;

import org.junit.Test;
import src.Database;
import src.SQL_Database;

import java.util.Calendar;

import static junit.framework.TestCase.assertEquals;

public class TestFlightStatus {
    private Database db = SQL_Database.getInstance();

    @Test
    public void testFlightStatusUpdate() {
        db.setStatus(5, 1);
        int status = db.getStatus(5);
        assertEquals(status, 1);
        db.setStatus(5, 2);
        status = db.getStatus(5);
        assertEquals(status, 2);
    }

    @Test
    public void addAndUpdateFlight() {
        Calendar c = Calendar.getInstance();
        int tripId = db.addTrip(1, c, 50);
        db.setStatus(tripId, 1);
        int status = db.getStatus(tripId);
        assertEquals(status, 1);

        db.removeTrip(tripId);
        status = db.getStatus(tripId);
        assertEquals(status, -1);
    }
}
