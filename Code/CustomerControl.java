package Code;

import java.lang.System;
import java.util.Scanner;

public class CustomerControl{
   //private Database_Interface db;
   private Dummy_Database db;
   private static CustomerControl uniqueinstance;

   private  CustomerControl(){
      //db = Database_Interface.getInstance();
      db = Dummy_Database.getInstance();
   }

   // Flight Reserve

   static public CustomerControl getInstance()
   {
      if (uniqueinstance == null)
      {
         uniqueinstance = new CustomerControl();
      }
      return uniqueinstance;
   }

   public void reserve(String username){
      FlightManager fm = new FlightManager();
      Scanner scan = new Scanner(System.in);
      Trip trip = fm.getTripFromUser();

      int flightId = db.getFlightId(trip.getDest(), trip.getDate());
      System.out.println("What seat would you like? Enter a number 1-20");
      int seatNo = scan.nextInt();
      db.reserveSeat(username, flightId, seatNo);
   }
   // Checkin
   public void checkin(String username){
      FlightManager fm = new FlightManager();
      Trip trip = fm.getTripFromUser();

      int flightId = db.getFlightId(trip.getDest(), trip.getDate());
      db.flightCheckIn(username, flightId);
   }
   // View status
   public void status(){
      FlightManager fm = new FlightManager();
      Trip trip = fm.getTripFromUser();

      System.out.println("Status = " + db.getStatus(trip.getDest(), trip.getDate()));
   }

}
