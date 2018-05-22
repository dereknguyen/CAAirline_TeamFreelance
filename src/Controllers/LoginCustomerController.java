package src.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;

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
   void initialize() {
      
      String username = customerUsername.getText().trim();
      
      this.customerLoginButton.setOnAction(event -> {
         System.out.println("Print on");
      });
      
   }
}
