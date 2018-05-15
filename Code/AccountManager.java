package Code;

import java.util.ArrayList;

public class AccountManager {
    SQL_Database db;

    public AccountManager() {
        db = SQL_Database.getInstance();
    }

    public Account CreateEmployeeAccount(String firstName, String lastName, String username) {
        db.addEmployeeAccount(firstName, lastName, username);
        return new EmployeeAccount(firstName, lastName, username, new ArrayList<>());
    }
    public Account CreateUserAccount(String firstName, String lastName, String username) {
        db.addCustomerAccount(firstName, lastName, username);
        return new Account(firstName, lastName, username, new ArrayList<>());
    }
    public void DeleteUserAccount(String toDelete){
        db.removeCustomer(toDelete);
    }
    public void DeleteEmployeeAccount(String toDelete) {
        db.removeEmployee(toDelete);
    }

}
