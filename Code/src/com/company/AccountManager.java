package com.company;

import java.util.ArrayList;

public class AccountManager {
    Database_Interface db;

    public AccountManager() {
        db = Database_Interface.getInstance();
    }

    public Account CreateEmployeeAccount(String firstName, String lastName, String username) {
        db.addEmployeeAccount(firstName, lastName, username);
        return new EmployeeAccount(firstName, lastName, username, new ArrayList<>());
    }
    public Account CreateUserAccount(String firstName, String lastName, String username) {
        db.addCustomerAccount(firstName, lastName, username);
        return new Account(firstName, lastName, username, new ArrayList<>());
    }
    public void DeleteAccount(Account toDelete){
        if (toDelete instanceof EmployeeAccount) {
            db.deleteEmployee(toDelete.getUserName());
        }
        else {
            db.deleteCustomer(toDelete.getUserName());
        }
    }

}
