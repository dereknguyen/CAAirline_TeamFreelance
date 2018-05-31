package Tests;

import org.junit.Test;
import src.Database;
import src.SQL_Database;

import java.util.Calendar;

public class IntegrationTestA {
    private static Database db = SQL_Database.getInstance();

    @Test
    public void TestFlight()
    {

        int  flightID = db.addFlight("San Luis Obispo", "Seattle");

        int shouldBeSameFlightID = db.getFlightId("San Luis Obispo", "Seattle");
        if(flightID == shouldBeSameFlightID)
            System.out.println("flight ID returned by add flight is correct");
        else
            System.out.println("ERROR");
        Calendar c = Calendar.getInstance();
        c.set(2018, Calendar.MAY, 24, 15, 0);
        int tripID = db.addTrip(flightID, c,1000);
        //shouldBeSameFlightID = db.getFlightIdFromTrip(tripID);
        if(flightID == shouldBeSameFlightID)
            System.out.println("flight ID returned by add flight is correct");
        else
            System.out.println("ERROR");


    }
}
