package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import src.SQL_Database;

public class CreateAccountController {

    @FXML private TabPane modeTabPane;
    @FXML private JFXTextField newUsername;
    @FXML private JFXPasswordField newPassword;
    @FXML private JFXTextField newFirstname;
    @FXML private JFXTextField newLastname;
    @FXML private JFXButton createAccountButton;
    @FXML private JFXButton loginReturnButton;
    @FXML private Label errMsg;

    @FXML
    void HandleCreateClick() {
        int mode = modeTabPane.getSelectionModel().getSelectedIndex();

        String username = newUsername.getText().trim();
        String password = src.MD5Password.encodePassword(newPassword.getText().trim());
        String firstName = newFirstname.getText().trim();
        String lastName = newLastname.getText().trim();

        if (username.length() == 0) {
            errMsg.setText("Please enter a username");
        }
        else if (firstName.length() == 0) {
            errMsg.setText("Please enter a first name");

        }
        else if (lastName.length() == 0) {
            errMsg.setText("Please enter a last name");

        }
        else if (password == null) {
            errMsg.setText("Please enter a password");

        }
        else {
            if (mode == 0) {
                handleCreateCustomer(username, password, firstName, lastName);
            }
            else if (mode == 1) {
                handleCreateEmployee(username, password, firstName, lastName);
            }
        }
    }

    private void handleCreateCustomer(String username, String password, String first, String last) {
        src.Database db = SQL_Database.getInstance();

        if (db.addCustomerAccount(username, password, first, last) == -1) {
            errMsg.setText("Username already in use");
            errMsg.setVisible(true);
        }
        else {
            createAccountButton.getScene().getWindow().hide();
            Utilities.present("../Views/CustomerMainView.fxml", "Main");
        }
    }

    private void handleCreateEmployee(String username, String password, String first, String last) {
        src.Database db = SQL_Database.getInstance();

        if (db.addEmployeeAccount(username, password, first, last) == -1) {
            errMsg.setText("Username already in use");
            errMsg.setVisible(true);
        }
        else {
            createAccountButton.getScene().getWindow().hide();
            Utilities.present("../Views/EmployeeMainView.fxml", "Main");
        }
    }

    @FXML
    private void HandleLoginReturn()
    {
        loginReturnButton.getScene().getWindow().hide();
        Utilities.present("../Views/LoginView.fxml", "California System");
    }
}
