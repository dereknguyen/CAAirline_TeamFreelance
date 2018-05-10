package Code;

import java.util.ArrayList;
import java.sql.Date;

public class EmployeeAccount extends Account {


    public EmployeeAccount(String firstName, String lastName, String userName, ArrayList<Ticket> ticket) {
        super(firstName, lastName, userName, ticket);
    }

    public int editFlightStatus() {
        return EmployeeControl.getInstance().editFlight();
    }

    public void scheduleFlight() {
        EmployeeControl.getInstance().scheduleFlight();
    }

    public int setPrice() {
        return EmployeeControl.getInstance().setPrice();
    }

    public void editEmployee() {
        return EmployeeControl.getInstance().editEmployee();
    }

    public void viewPrice() {
        EmployeeControl.getInstance().viewPrice();
    }

//    public int editFlightStatus(int destinationID, Date date, int status) {
//        return Database_Interface.getInstance().setStatus(destinationID, date, status);
//    }
//
//    public int scheduleFlight(int destinationID, Date date, int fullSeats, double price) {
//        return Database_Interface.getInstance().addFlight(destinationID, date, fullSeats, price);
//    }
//
//    public int setPrice(int destinationId, Date date, double price) {
//        return Database_Interface.getInstance().setPrice(destinationId, date, price);
//    }
//
//    public int editEmployee(String firstName, String lastName, String userName) {
//        return Database_Interface.getInstance().editEmployee(firstName, lastName, userName);
//    }
//
//    public int promoteToEmployee(String firstName, String lastName, String userName) {
//        Database_Interface db = Database_Interface.getInstance();
//
//        if (db.removeCustomer(userName) == 0) {
//            return db.addEmployeeAccount(userName, firstName, lastName);
//        }
//
//        return 1;
//    }


}
