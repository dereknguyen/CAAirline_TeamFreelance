package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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

   @FXML
   private Label errMsg;

   @FXML
   void HandleLoginClick(ActionEvent event) throws IOException
   {
      String username = customerUsername.getText().trim();
      String password = src.MD5Password.encodePassword(customerPassword.getText().trim());
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

    @FXML
    void HandleToCreateAccount(ActionEvent event) {
        customerSignupButton.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/CreateAccount.fxml"));

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
