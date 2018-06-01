package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import src.SQL_Database;

import java.io.IOException;
import java.util.List;

public class LoginEmployeeController {
    @FXML
    private JFXTextField empUsername;

    @FXML
    private JFXPasswordField empPassword;

    @FXML
    private JFXButton empLoginButton;

    @FXML
    private Label errMsg;

    @FXML
    void HandleLoginClick(ActionEvent event) throws IOException
    {
        String username = empUsername.getText().trim();
        String password = src.MD5Password.encodePassword(empPassword.getText().trim());
        if (username.length() == 0)
        {
            errMsg.setText("Please enter a username");
            errMsg.setVisible(true);
        }
        else if (password == null)
        {
            errMsg.setText("Please enter a password");
            errMsg.setVisible(true);
        }
        else
        {
            src.Database db = SQL_Database.getInstance();
            List<String> entry = db.getCustomerInfo(username);
            if (entry == null)
            {
                errMsg.setText("Invalid username/password");
                errMsg.setVisible(true);
            }
            else if (entry.get(1) != null && !entry.get(1).equals(password))
            {
                errMsg.setText("Invalid username/password");
                errMsg.setVisible(true);
            }
            else
            {
                errMsg.setVisible(false);
                System.out.println("Login successful");
                //todo login successful, for testing, the database contains an account for "user" , "password"
            }

        }
    }
}
