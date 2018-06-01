package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import src.SQL_Database;

import java.io.IOException;

public class CreateCustomerAccountController
{
    @FXML
    private JFXTextField newUsername;

    @FXML
    private JFXPasswordField newPassword;

    @FXML
    private JFXTextField newFirstname;

    @FXML
    private JFXTextField newLastname;

    @FXML
    private JFXButton createAccountButton;

    @FXML
    private VBox vbox;

    @FXML
    private Label errMsg;

    @FXML
    void HandleCreateClick(ActionEvent event)
    {
        String username = newUsername.getText().trim();
        String password = src.MD5Password.encodePassword(newPassword.getText().trim());
        String firstname = newFirstname.getText().trim();
        String lastname = newLastname.getText().trim();

        if (username.length() == 0)
        {
            errMsg.setText("Please enter a username");
            errMsg.setVisible(true);
        }
        else if (firstname.length() == 0)
        {
            errMsg.setText("Please enter a first name");
            errMsg.setVisible(true);
        }
        else if (lastname.length() == 0)
        {
            errMsg.setText("Please enter a last name");
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
            if (db.addCustomerAccount(username, password, firstname, lastname) == -1)
            {
                errMsg.setText("Username already in use");
                errMsg.setVisible(true);
            }
            else
            {
                errMsg.setVisible(false);
                System.out.println("New customer account created");
                //todo go to home page, etc
                gotoLogin();
            }
        }
    }

    //TODO: connect with ui
    void HandleSignInClick(ActionEvent event) {
        gotoLogin();
    }

    void gotoLogin() {
        createAccountButton.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../Views/LoginCustomer.fxml"));

        try {
            loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
}
