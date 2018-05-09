package Code;

import java.util.Scanner;
import java.sql.*;

public class FlightManager{
   Scanner reader = new Scanner(System.in);

   public void editStatus(){
      Database_Interface db = Database_Interface.getInstance();
      Trip trip = getTripFromUser();
      int status;
      int flightID;
      
      System.out.println("Enter status");
      status = reader.nextInt();
      
      db.setStatus(trip, status);
   }
   public Trip getTripFromUser(){
      System.out.println("Enter destination");
      int destination = reader.nextInt();
      System.out.println("Enter year");
      int y = reader.nextInt();
      System.out.println("Enter month");
      int m = reader.nextInt();
      System.out.println("Enter date");
      int d = reader.nextInt();
      System.out.println("Enter hrs");
      int h = reader.nextInt();
      System.out.println("Enter min");
      int min = reader.nextInt();
      Date date = new Date(y,m,d,h,min);
      Trip trip = new Trip(date,destination)
      return trip;
   }
   public void viewStatus(){
      Database_Interface db = Database_Interface.getInstance();
      Trip trip = getTripFromUser();
      int status = db.getStatus(trip);
      System.out.println(status);
   }
   public void scheduleFlight(){
      Database_Interface db = Database_Interface.getInstance();
      Trip trip = getTripFromUser();
      Date date = trip.date;
      Date minH = new Date(date.getTime()-40*60*1000); //40 min in ms
      Date maxH = new Date(date.getTime()+40*60*1000);
      if(db.getNumFlights(minH, maxH)==0){
         db.addFlight(trip.destination, trip.date);
      }
   }
}
