package com.company;

import java.util.Scanner;

public class EmployeeControl{
   /*
    * Return 0 on success, 1 on failure
    */
   public int editFlight(){
      Flightdatabase_interface fdb = new Flightdatabase_interface();
      Scanner reader = new Scanner(System.in);
      int destination, month, day, hour, min, action, new_hour, new_min;

      System.out.println("Edit (0) or delete (1) a flight?");
      action = reader.nextInt();
      System.out.println("Enter Integer 0 - 5 for Destination:");
      System.out.println("LA: 0, SF: 1, SD: 2, Phoenix: 3, SEA: 4, Dallas: 5");
      destination = reader.nextInt();
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

      flightdatabase_interface flight = new flightdatabase_interface();
      if(action)
         flight.removeFlight(getFlight(destination, month, day, hour, min));
      else{
         flight.removeFlight(getFlight(destination, month, day, hour, min));
         flight.setFlight(destination, month, day, new_hour, new_min);
      }
   }
   public void scheduleFlight(){
      flightdatabase_interface flight = new flightdatabase_interface();
      int destination, month, day, hour, min;

      System.out.println("Enter Integer 0 - 5 for Destination:");
      System.out.println("LA: 0, SF: 1, SD: 2, Phoenix: 3, SEA: 4, Dallas: 5");
      destination = reader.nextInt();
      System.out.println("Month: ");
      month = reader.nextInt();
      System.out.println("Day: ");
      day = reader.nextInt();
      System.out.println("Hour (Pacific Time): ");
      hour = reader.nextInt();
      System.out.println("Minute (Pacific Time): ");
      min = reader.nextInt();

      flight.setFlight(destination, month, day, hour, min);
   }

   /*
    * Uses P = P - ((X/2)*P)
    */

   public void viewPrice(){
      System.out.println("Enter Integer 0 - 5 for Destination:");
      System.out.println("LA: 0, SF: 1, SD: 2, Phoenix: 3, SEA: 4, Dallas: 5");
      int destination = reader.nextInt();
      /* ask user for current day and month */
      int day = reader.nextInt();
      int month = reader.nextInt();
      flightdatabase_interface flight = new flightdatabase_interface();


      int totalEmpty = 0;
      for(int i = day; i>day-14; i--){
         totalEmpty += flight.getEmptySeats(destination, month, day);
      }
      int X = totalEmpty / (14*20); /* 20 is total seats, 14 is number of days */
      int P = flight.baseprice(destination);
      int p = P - ((X/2)*P);
      System.out.println("Price recommendation: " + p);
   }
   public void setPrice(){
      System.out.println("Enter Integer 0 - 5 for Destination:");
      System.out.println("LA: 0, SF: 1, SD: 2, Phoenix: 3, SEA: 4, Dallas: 5");
      /* ask user for date and time too */

      flightdatabase_interface flight = new flightdatabase_interface();
      flight.setPrice(destination, month, day, minute, hour);
   }
   public void editEmployee(){
      System.out.println("Add (0) or delete (1)?");
      int action = reader.nextInt();
      Account acct = new Account();
      /*...*/
   }
}
