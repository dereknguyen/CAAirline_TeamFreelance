import javax.xml.soap.Text;
import java.util.Scanner;
import java.sql.*;

public class FlightManager{
   Scanner reader = new Scanner(System.in);

   public void editStatus(){
      Text_Database db = Text_Database.getInstance();
      Trip trip = getTripFromUser();
      int status;
      int flightID;
      
      System.out.println("Enter status");
      status = reader.nextInt();
      
      db.setStatus(trip.getFlightId(), status);
   }

   public Trip getTripFromUser(){
      System.out.println("Enter Integer 0 - 5 for Destination:");
      System.out.println("LA: 0, SF: 1, SD: 2, Phoenix: 3, SEA: 4, Dallas: 5");
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
      Date date = new Date(y,m,d);
      Trip trip = new Trip(date,destination);

      return trip;
   }

   public void viewStatus(){
      Text_Database db = Text_Database.getInstance();
      Trip trip = getTripFromUser();
      int status = db.getStatus(trip.getFlightId());
      System.out.println(status);
   }
   public void scheduleFlight(){
      Text_Database db = Text_Database.getInstance();
      Trip trip = getTripFromUser();
      Date date = trip.getDate();
      Date minH = new Date(date.getTime()-40*60*1000); //40 min in ms
      Date maxH = new Date(date.getTime()+40*60*1000);
      if(db.getNumFlights(minH, maxH)==0){
         System.out.println("Enter price");
         double price = reader.nextDouble();
         db.addFlight(trip.getDest(), trip.getDate(), 0, price);
         System.out.println("Success");
         db.addFlight(trip.getDest(), trip.getDate());
      }
      else
         System.out.println("Failure");
   }
}
