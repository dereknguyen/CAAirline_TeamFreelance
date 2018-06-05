package src;

import java.lang.System;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

public class CustomerControl{
   private SQL_Database db;
   private static CustomerControl uniqueinstance;

   private Account customer;

   private  CustomerControl() {
      db = SQL_Database.getInstance();
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

   public void getCustomerFromDB(String username) {
       List<String> info = this.db.getCustomerInfo(username);
       ArrayList<Ticket> userTickets = this.db.getTicketsByUsername(username);

       this.customer = new Account(info.get(2), info.get(3), info.get(0), userTickets);
   }

   public Account getCustomer() {
       return customer;
   }

   public void reserve(String username){
      FlightManager fm = new FlightManager();
      Scanner scan = new Scanner(System.in);
      //Trip trip = fm.getTripFromUser();
      //Trip trip = new Trip(0, 0, Calendar.getInstance(), 0, 0);

      //int tripId = trip.getTripId();
      System.out.println("What seat would you like? Enter a number 1-20");
      int seatNo = scan.nextInt();
      //db.addTicket(username, tripId, seatNo);
   }

   // Checkin
   public void checkin(String username){
      FlightManager fm = new FlightManager();
      //Trip trip = new Trip(0, 0, Calendar.getInstance(), 0, 0);

      //int flightId = trip.getFlightId();
      //db.checkIn(username, flightId);
   }

   // View status
   public void status(){
      FlightManager fm = new FlightManager();
      //Trip trip = new Trip(0, 0, Calendar.getInstance(), 0, 0);

      //System.out.println("Status = " + db.getStatus(trip.getFlightId()));
   }

}
