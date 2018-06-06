package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import src.CustomerControl;
import src.EmployeeControl;
import src.SQL_Database;
import src.Session;

public class LoginController {

    @FXML private TabPane loginModeTabPane;
    @FXML private JFXTextField customerUsername;
    @FXML private JFXPasswordField customerPassword;

    @FXML private JFXTextField employeeUsername;
    @FXML private JFXPasswordField employeePassword;

    @FXML private JFXButton loginButton;
    @FXML private JFXButton createAccountButton;

    @FXML private Label errMsg;

    @FXML
    void HandleLoginClick() {
        int selectedMode = loginModeTabPane.getSelectionModel().getSelectedIndex();

        String username = (selectedMode == 0) ?
                customerUsername.getText().trim() :
                employeeUsername.getText().trim();

        String password = (selectedMode == 0) ?
                src.MD5Password.encodePassword(customerPassword.getText().trim()) :
                src.MD5Password.encodePassword(employeePassword.getText().trim());

        if (username.length() == 0 && password == null) {
            errMsg.setText("Please enter credentials.");
        }
        else if (username.length() == 0) {
            errMsg.setText("Please enter a username.");
        }
        else if (password == null) {
            errMsg.setText("Please enter a password.");
        }
        else {
            if (selectedMode == 0) {
                handleCustomerLogin(username, password);
            }
            else if (selectedMode == 1) {
                handleEmployeeLogin(username, password);
            }
        }
    }

    @FXML
    void HandleToCreateAccount() {
        createAccountButton.getScene().getWindow().hide();
        Utilities.present("../Views/CreateAccount.fxml", "Create Account");
    }


    /** Present Main Tabs UI. Use only after successfully authenticate users. */
    private void showMainTab(String mode) {
        loginButton.getScene().getWindow().hide();

        Utilities.present(mode, "Main");
    }

    private void handleCustomerLogin(String username, String password) {

        src.Database db = SQL_Database.getInstance();
        List<String> entry = db.getCustomerInfo(username);
        Session s = Session.getInstance();

        if (entry == null) {
            errMsg.setText("Invalid username/password");
        }
        else if (entry.get(1) != null && !entry.get(1).equals(password)) {
            errMsg.setText("Invalid username/password");
        }
        else {
            System.out.println("Login successful");
            s.setUsername(username);
            CustomerControl.getInstance().getCustomerFromDB(username);
            showMainTab("../Views/CustomerMainView.fxml");
        }
    }

    private void handleEmployeeLogin(String username, String password) {
        src.Database db = SQL_Database.getInstance();
        List<String> entry = db.getEmployeeInfo(username);
        Session s = Session.getInstance();

        if (entry == null)
        {
            errMsg.setText("Invalid username/password");
        }
        else if (entry.get(1) != null && !entry.get(1).equals(password))
        {
            errMsg.setText("Invalid username/password");
        }
        else
        {
            System.out.println("Login successful");
            s.setUsername(username);
            EmployeeControl.getInstance().getEmployeeFromDB(username);
            showMainTab("../Views/EmployeeMainView.fxml");
        }
    }

}
