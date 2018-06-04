package Controllers.OldControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import src.Database;
import src.SQL_Database;
import src.Session;

import java.io.IOException;


public class CheckinController {
    @FXML
    private TextField confirmationNumber;

    @FXML
    private Button CheckInButton;

    @FXML
    private TextField carryOn;

    @FXML
    private TextField checked;

    @FXML
    private Button confirmationButton;

    @FXML
    private Label errMsg;

    @FXML
    void CheckInButtonClick(ActionEvent event) throws IOException {
        Database db = SQL_Database.getInstance();

        //this assumes confirmation number is the trip id
        Integer tripId = Integer.parseInt(confirmationNumber.getText().trim());

        Session curSession = Session.getInstance();
        String username = curSession.getUsername();

        db.checkIn(username, tripId);
    }

    @FXML
    void ConfirmationButtonClick(ActionEvent event) {
        String tempTripId = confirmationNumber.getText().trim();

        if (tempTripId.equals("")) {
            errMsg.setText("Please enter a confirmation number");
            errMsg.setVisible(true);
        }
        else {
            Integer tripId = Integer.parseInt(tempTripId);
            Database db = SQL_Database.getInstance();


            Session curSession = Session.getInstance();
            String username = curSession.getUsername();


            int numBags = 0;
            if (!carryOn.getText().trim().equals("")) {
                numBags += Integer.parseInt(carryOn.getText().trim());
            }
            if (!checked.getText().trim().equals("")) {
                numBags += Integer.parseInt(checked.getText().trim());
            }


            db.setBags(username, tripId, numBags);
        }
    }
}
