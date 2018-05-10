package Code;

import java.util.Scanner;

public class CustomerControl(){
   // Flight Reserve
   public void reserve(){
      Database_Interface db = new Database_Interface();
      String username = //prompt customer for username
      Trip trip = FlightManager.getTripFromUser();
      
      int flightId = getFlightId(trip.getDest(), trip.getDate());
      int seatNo = // prompt for seat number
      db.reserveSeat(username, flightId, seatNo);
   }
   // Checkin
   public void checkin(){
      Database_Interface db = new Database_Interface();
      String username = //prompt for username
      Trip trip = FlightManager.getTripFromUser();

      int flightId = getFlightId(trip.getDest(), trip.getDate();
      db.flightCheckIn(username, flightId);
   }
   // View status
   public void status(){
      Database_Interface db = new Database_Interface();
      Trip trip = FlightManager.getTripFromUser();

      System.out.println("Status" + db.getStatus(trip.getDest(), trip.getDate()));
   }

}
