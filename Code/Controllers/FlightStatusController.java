package Controllers;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.jfoenix.controls.JFXTextField;
<<<<<<< HEAD
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import src.SQL_Database;

import java.io.IOException;

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
    void HandleGetStatusClick(ActionEvent event) throws IOException {
        //TODO: use flight number and figure out date
        Integer tripId = Integer.parseInt(flightNo.getText().trim());
        src.Database db = SQL_Database.getInstance();

        //TODO: fill date and time using tripId

        int flightId = ((SQL_Database) db).getFlightIdFromTrip(tripId);
        //TODO: fill src_dest using flightId

        int status = db.getStatus(tripId);
        StringProperty new_status = new SimpleStringProperty("");
        //0 = On-time, 1 = Delayed, 2 = Cancelled, Return 0 on success, -1 on error
        if (status == 0) {
            new_status = new SimpleStringProperty("ON TIME");
        }
        else if (status == 1) {
            new_status = new SimpleStringProperty("DELAYED");
        }
        else if (status == 2) {
            new_status = new SimpleStringProperty("CANCELED");
        }

        statusDisplay.textProperty().bind(new_status);
    }
}
