package src;

import java.util.ArrayList;

public class AccountManager {
    SQL_Database db;

    public AccountManager() {
        db = SQL_Database.getInstance();
    }

    public Account CreateEmployeeAccount(String firstName, String lastName, String username, String encryptedPassword) {
        db.addEmployeeAccount(username, firstName, lastName, encryptedPassword);
        return new EmployeeAccount(firstName, lastName, username, new ArrayList<>());
    }
    public Account CreateUserAccount(String firstName, String lastName, String username, String encryptedPassword) {
        db.addCustomerAccount(username, encryptedPassword, firstName, lastName);
        return new Account(firstName, lastName, username, new ArrayList<>());
    }
    public void DeleteUserAccount(String toDelete){
        db.removeCustomer(toDelete);
    }
    public void DeleteEmployeeAccount(String toDelete) {
        db.removeEmployee(toDelete);
    }

}
