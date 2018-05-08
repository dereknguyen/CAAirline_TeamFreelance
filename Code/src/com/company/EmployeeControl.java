package com.company;

import java.util.Calendar;
import java.util.Scanner;
import java.sql.Date;

public class EmployeeControl{
   private Database_Interface db;
   private Calendar cal;
   private Scanner reader;

   public EmployeeControl()
   {
      db = Database_Interface.getInstance();
      cal = Calendar.getInstance();
      reader = new Scanner(System.in);
   }

   /*
    * Return 0 on success, 1 on failure
    */
   public int editFlight(){
      int destination, year, month, day, hour, min, action, new_hour, new_min;
      Date date;
      int id;

      System.out.println("Edit (0) or delete (1) a flight?");
      action = reader.nextInt();
      System.out.println("Enter Integer 0 - 5 for Destination:");
      System.out.println("LA: 0, SF: 1, SD: 2, Phoenix: 3, SEA: 4, Dallas: 5");
      destination = reader.nextInt();
      System.out.println("Year: ");
      year = reader.nextInt();
      System.out.println("Month: ");
      month = reader.nextInt();
      System.out.println("Day: ");
      day = reader.nextInt();
      System.out.println("Old Hour (Pacific Time): ");
      hour = reader.nextInt();
      System.out.println("Old Minute (Pacific Time): ");
      min = reader.nextInt();
      System.out.println("New Hour (Pacific Time): ");
      new_hour = reader.nextInt();
      System.out.println("New Minute (Pacific Time): ");
      new_min = reader.nextInt();

      cal.set(year, month, day, hour, min);
      date = new Date(cal.getTime().getTime());
      id = db.getFlightId(destination, date);

      if(action == 1)
         db.removeFlight(id);
      else{
         db.removeFlight(id);
         db.addFlight(id, destination, date);
      }
      //TODO check return values of addFlight() removeFlight()
      return 0;
   }

   public void scheduleFlight(){
      int destination, year, month, day, hour, min;
      Date date;
      int id;

      System.out.println("Enter Integer 0 - 5 for Destination:");
      System.out.println("LA: 0, SF: 1, SD: 2, Phoenix: 3, SEA: 4, Dallas: 5");
      destination = reader.nextInt();
      System.out.println("Year: ");
      year = reader.nextInt();
      System.out.println("Month: ");
      month = reader.nextInt();
      System.out.println("Day: ");
      day = reader.nextInt();
      System.out.println("Hour (Pacific Time): ");
      hour = reader.nextInt();
      System.out.println("Minute (Pacific Time): ");
      min = reader.nextInt();

      cal.set(year, month, day, hour, min);
      date = new Date(cal.getTime().getTime());
      id = db.getFlightId(destination, date);

      db.addFlight(id, destination, date);
   }

   /*
    * Uses P = P - ((X/2)*P)
    */

   public void viewPrice()
   {
      System.out.println("Enter Integer 0 - 5 for Destination:");
      System.out.println("LA: 0, SF: 1, SD: 2, Phoenix: 3, SEA: 4, Dallas: 5");
      int destination = reader.nextInt();

      double avgEmpty = db.calculateAvgEmpty(destination);
      int P = flight.baseprice(destination);
      double p = P - ((avgEmpty/2)*P);

      System.out.println("Price recommendation: " + p);
   }

   public void setPrice()
   {
      System.out.println("Enter Integer 0 - 5 for Destination:");
      System.out.println("LA: 0, SF: 1, SD: 2, Phoenix: 3, SEA: 4, Dallas: 5");
      /* ask user for date and time too */

      //TODO implement setPrice
      db.setPrice(destination, month, day, minute, hour);
   }

   public void editEmployee()
   {
      System.out.println("Add (0) or delete (1)?");
      int action = reader.nextInt();
      Account acct = new Account();
      /*...*/
   }
}
