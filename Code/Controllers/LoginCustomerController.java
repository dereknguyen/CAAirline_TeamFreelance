package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import src.SQL_Database;

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
      String password = src.MD5Password.encodePassword(customerPassword.getText().trim());
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
      else if (password == null)
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
         src.Database db = SQL_Database.getInstance();
         List<String> entry = db.getCustomerInfo(username);
         if (entry == null)
         {
            usernameErr = new Label("Invalid username/password");
            usernameErr.applyCss();
            usernameErr.setId("errmsg");
            usernameErr.setTextFill(Color.web("#C32820"));
            virtualbox.getChildren().add(usernameErr);
         }
         else if (entry.get(1) != null && !entry.get(1).equals(password))
         {
            passwordErr = new Label("Invalid username/password");
            passwordErr.applyCss();
            passwordErr.setId("errmsg");
            passwordErr.setTextFill(Color.web("#C32820"));
         }
         else
         {
            //todo login successful, for testing, the database contains an account for "user" , "password"
            System.out.println("Login successful");
         }
      }
   }
}
