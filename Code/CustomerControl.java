package Code;

import java.lang.System;
import java.util.Scanner;

public class CustomerControl{
   private CustomerControl uniqueinstance;

   private CustomerControl(){}
   // Flight Reserve

   public CustomerControl getInstance()
   {
      if (uniqueinstance == null)
      {
         uniqueinstance = new CustomerControl();
      }
      return uniqueinstance;
   }

   public void reserve(String username){
      Scanner scan = new Scanner(System.in);
      Database_Interface db = Database_Interface.getInstance();
      Trip trip = FlightManager.getTripFromUser();
      
      int flightId = db.getFlightId(trip.getDest(), trip.getDate());
      System.out.println("What seat would you like? Enter a number 1-20");
      int seatNo = scan.nextInt();
      db.reserveSeat(username, flightId, seatNo);
   }
   // Checkin
   public void checkin(String username){
      Database_Interface db = Database_Interface.getInstance();
      Trip trip = FlightManager.getTripFromUser();

      int flightId = db.getFlightId(trip.getDest(), trip.getDate());
      db.flightCheckIn(username, flightId);
   }
   // View status
   public void status(){
      Database_Interface db = Database_Interface.getInstance();
      Trip trip = FlightManager.getTripFromUser();

      System.out.println("Status" + db.getStatus(trip.getDest(), trip.getDate()));
   }

}
