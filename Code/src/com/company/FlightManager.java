package com.company;

import java.util.Scanner;

public class FlightManager{
   Scanner reader = new Scanner(System.in);

   public void editStatus(){
      Database_Interface db = Database_Interface.getInstance();
      Trip trip = getTripFromUser();
      Date date = getDateFromUser();
      int status;
      int flightID;
      
      System.out.println("Enter status");
      status = reader.nextInt();
      
      db.setStatus(date,trip, status);
   }
   public Trip getTripFromUser(){
      Trip trip = new Trip();
      System.out.println("Enter destination");
      int destination = reader.nextInt();
      trip.setDestination(destination);
      return trip;
   }
   public Date getDateFromUser(){
      Date date = new Date();
      System.out.println("Enter date");
      int d = reader.nextInt();
      date.setDate(d);
      return date;
   }
   public void viewStatus(){
      Database_Interface db = Database_Interface.getInstance();
      Trip trip = getTripFromUser();
      Date date = getDateFromUser();
      int status = db.getStatus(date, trip);
      System.out.println(status);
   }
   public void scheduleFlight(){
      Database_Interface db = Database_Interface.getInstance();
      Trip trip = getTripFromUser();
      Date date = getDateFromUser();
      db.addFlight(flightID, date, trip);
   }
}
