package Code;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.sql.Date;

public class EmployeeControl{
   private static EmployeeControl uniqueinstance;
   private Database_Interface db;
   private Calendar cal;
   private Scanner reader;

   private EmployeeControl()
   {
      db = Database_Interface.getInstance();
      cal = Calendar.getInstance();
      reader = new Scanner(System.in);
   }

   public static EmployeeControl getInstance()
   {
      if (uniqueinstance == null)
      {
         uniqueinstance = new EmployeeControl();
      }
      return uniqueinstance;
   }

   /*
    * Edit flight
    * Return 0 on success, 1 on failure
    */
   public int editFlight(){
      int destination, year, month, day, hour, min, action, new_hour, new_min;
      Date date;

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

      cal.set(year, month-1, day, hour, min);
      date = new Date(cal.getTime().getTime());

      if(action == 1)
         if (db.removeFlight(destination, date) == 1)
         {
            System.out.println("No matching flight found in database");
         }
      else
      {
         if (db.removeFlight(destination, date) == 1)
         {
            System.out.println("No matching flight found in database");
         }
         else
         {
            if (db.addFlight(destination, date) == 1)
            {
               System.out.println("Could not edit flight");
            }
         }
      }
      return 0;
   }

   public void scheduleFlight(){
      int destination, year, month, day, hour, min;
      Date date;

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

      cal.set(year, month-1, day, hour, min);
      date = new Date(cal.getTime().getTime());

      if (db.addFlight(destination, date) == 1)
      {
         System.out.println("Could not add flight to database");
      }
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
      //int P = flight.baseprice(destination);
      int P = 5;
      double p = P - ((avgEmpty/2)*P);

      System.out.println("Price recommendation: " + p);
   }

   public void setPrice()
   {
      System.out.println("Enter Integer 0 - 5 for Destination:");
      System.out.println("LA: 0, SF: 1, SD: 2, Phoenix: 3, SEA: 4, Dallas: 5");
      int destination = reader.nextInt();
      // TODO ask user for date and time too

      //db.setPrice(destination, date, price);
   }

   public void editEmployee()
   {
      AccountManager am = new AccountManager();
      System.out.println("Add (0) or delete (1)?");
      int action = reader.nextInt();
      System.out.println("Is this account a customer (0) or employee (1) account?");
      int level = reader.nextInt();
      System.out.println("What is your username?");
      String username = reader.next();
      if (action == 0) {
         System.out.println("What is your first name?");
         String firstname = reader.next();
         System.out.println("What is your last name?");
         String lastname = reader.next();
         if (level == 0) {
            am.CreateUserAccount(firstname, lastname, username);
         }
         else if (level == 1) {
            am.CreateEmployeeAccount(firstname, lastname, username);
         }
      }
      else if (action == 1) {
         if (level == 0) {
            am.DeleteUserAccount(username);
         }
         else if (level == 1) {
            am.DeleteEmployeeAccount(username);
         }
      }
   }
}
