package Code;

import java.lang.System;
import java.util.Scanner;

public class CustomerControl{
   private Database_Interface db;
   public CustomerControl(){
      db = Database_Interface.getInstance();
   }
   // Flight Reserve
   public void reserve(String username){
      FlightManager fm = new FlightManager();
      Scanner scan = new Scanner(System.in);
      Database_Interface db = Database_Interface.getInstance();
      Trip trip = fm.getTripFromUser();
      
      //int flightId = db.getFlightId(trip.getDest(), trip.getDate());
      int flightId = 0;
      System.out.println("What seat would you like? Enter a number 1-20");
      int seatNo = scan.nextInt();
      db.reserveSeat(username, flightId, seatNo);
   }
   // Checkin
   public void checkin(String username){
      FlightManager fm = new FlightManager();
      Database_Interface db = Database_Interface.getInstance();
      Trip trip = fm.getTripFromUser();

      //int flightId = db.getFlightId(trip.getDest(), trip.getDate());
      int flightId = 0;
      db.flightCheckIn(username, flightId);
   }
   // View status
   public void status(){
      FlightManager fm = new FlightManager();
      Database_Interface db = Database_Interface.getInstance();
      Trip trip = fm.getTripFromUser();

      System.out.println("Status" + db.getStatus(trip.getDest(), trip.getDate()));
   }

}
