package Code;

import java.util.ArrayList;
import java.sql.Date;

public class EmployeeAccount extends Account {

    private EmployeeControl controller;

    public EmployeeAccount(String firstName, String lastName, String userName, ArrayList<Ticket> ticket) {
        super(firstName, lastName, userName, ticket);

        this.controller = new EmployeeControl();
    }

    public void editFlightStatus() {
        controller.editFlight();
    }

    public void scheduleFlight() {
        controller.scheduleFlight();
    }

    public void setPrice() {
        controller.setPrice();
    }

    public void editEmployee() {
        controller.editEmployee();
    }

    public void viewPrice() {
        controller.viewPrice();
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
