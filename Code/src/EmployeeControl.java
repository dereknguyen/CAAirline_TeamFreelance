package src;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.sql.Date;

public class EmployeeControl{
   private SQL_Database db;
   private static EmployeeControl uniqueinstance;

   private Account employee;

   private EmployeeControl() { db = SQL_Database.getInstance(); }

   public static EmployeeControl getInstance()
   {
      if (uniqueinstance == null)
      {
         uniqueinstance = new EmployeeControl();
      }
      return uniqueinstance;
   }

   public void getEmployeeFromDB(String username) {
      List<String> info = this.db.getEmployeeInfo(username);
      ArrayList<Ticket> userTickets = this.db.getTicketsByUsername(username);

      this.employee = new Account(info.get(2), info.get(3), info.get(0), userTickets);
   }
}
