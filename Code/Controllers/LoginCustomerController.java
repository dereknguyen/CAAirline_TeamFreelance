package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class LoginCustomerController {
   
   @FXML
   private JFXTextField customerUsername;
   
   @FXML
   private JFXPasswordField customerPassword;
   
   @FXML
   private JFXButton customerLoginButton;
   
   @FXML
   private JFXButton customerSignupButton;

   @FXML
   private VBox virtualbox;

   private Label usernameErr = null;
   private Label passwordErr = null;

   @FXML
   void HandleLoginClick(ActionEvent event) throws IOException
   {
      String username = customerUsername.getText().trim();
      String password = customerPassword.getText().trim();
      if (username.length() == 0)
      {
         virtualbox.getChildren().removeAll(passwordErr);
         passwordErr = null;
         if (usernameErr == null)
         {
            usernameErr = new Label("Please enter a username");
            usernameErr.applyCss();
            usernameErr.setId("errmsg");
            usernameErr.setTextFill(Color.web("#C32820"));
            //label.setTranslateY(-200);
            virtualbox.getChildren().add(usernameErr);
         }
      }
      else if (password.length() == 0)
      {
         virtualbox.getChildren().removeAll(usernameErr);
         usernameErr = null;
         if (passwordErr == null)
         {
            passwordErr = new Label("Please enter a password");
            passwordErr.applyCss();
            passwordErr.setId("errmsg");
            passwordErr.setTextFill(Color.web("#C32820"));
            virtualbox.getChildren().add(passwordErr);
         }
      }
      else
      {
         virtualbox.getChildren().removeAll(passwordErr, usernameErr);
         usernameErr = null;
         passwordErr = null;
         //todo database check input
      }
   }
}