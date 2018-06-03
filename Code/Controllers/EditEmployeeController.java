package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import src.Database;
import src.SQL_Database;

public class EditEmployeeController {

    @FXML
    private TextField employeeUsername;

    @FXML
    private TextField employeeFirstName;

    @FXML
    private TextField employeeLastName;

    @FXML
    private TextField employeePassword;

    @FXML //updates database based on given info
    void HandleUpdateClick(ActionEvent event) {
        Database db = SQL_Database.getInstance();

        String username = employeeUsername.getText().trim();
        String password = src.MD5Password.encodePassword(employeePassword.getText().trim());
        String firstName = employeeFirstName.getText().trim();
        String lastName = employeeLastName.getText().trim();

        db.editEmployee(username, password, firstName, lastName);

    }
}
