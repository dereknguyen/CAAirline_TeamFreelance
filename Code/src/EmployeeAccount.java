package src;

import java.util.ArrayList;

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

    public void setPrice() {
        EmployeeControl.getInstance().setPrice();
    }

    public void editEmployee() {
        EmployeeControl.getInstance().editEmployee();
    }

    public void viewPrice() {
        EmployeeControl.getInstance().viewPrice();
    }
}
