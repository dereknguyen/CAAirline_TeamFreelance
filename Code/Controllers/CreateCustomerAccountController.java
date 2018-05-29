package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

import java.awt.event.ActionEvent;
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
    void HandleCreateClick(ActionEvent event) throws IOException
    {
        String username = newUsername.getText().trim();
        String password = src.MD5Password.encodePassword(newPassword.getText().trim());
        String firstname = newFirstname.getText().trim();
        String lastname = newLastname.getText().trim();


    }
}
