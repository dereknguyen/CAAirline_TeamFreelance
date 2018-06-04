package Controllers.OldControllers;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import src.SQL_Database;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class FlightStatusController {
    @FXML
    private TextField flightNo;
    @FXML
    private Button get_status;
    @FXML
    private Label statusDisplay;
    @FXML
    private Label src_dest;
    @FXML
    private Label time;
    @FXML
    private Label date;

    @FXML
    void HandleGetStatusClick(ActionEvent event) {
        Integer tripId = Integer.parseInt(flightNo.getText().trim());
        src.Database db = SQL_Database.getInstance();

        //fill date using tripId
        Calendar cal = db.getDate(tripId);
        date.setText(cal.toString());

        int flightId = db.getFlightIdFromTrip(tripId);
        //fill src_dest using flightId
        String src = db.getFlightSrc(flightId);
        String dest = db.getFlightDest(flightId);
        src_dest.setText(src + " -> " + dest);

        int status = db.getStatus(tripId);
        String new_status = "";
        //0 = On-time, 1 = Delayed, 2 = Cancelled, Return 0 on success, -1 on error
        if (status == 0) {
            new_status = "ON TIME";
        }
        else if (status == 1) {
            new_status = "DELAYED";
        }
        else if (status == 2) {
            new_status = "CANCELED";
        }
        else {
            new_status = "ERROR";
        }

        statusDisplay.setText(new_status);
    }
}