package Controllers;

import javafx.fxml.FXML;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

public class FlightStatusController {
    @FXML
    private JFXTextField flightNo;

    @FXML
    private Label statusDisplay;


    @FXML
    void HandleStatusClick(ActionEvent event) {
        /* Get the flight number from the user */
        src.Database db = src.SQL_Database.getInstance();
        int flightNoInt = Integer.parseInt(flightNo.getText());
        int status = db.getStatus(flightNoInt);

        /* Display the status */
        String statusString;
        if (status == 0)
            statusString = "On Time";
        else if (status == 1)
            statusString = "Delayed";
        else if (status == 2)
            statusString = "Cancelled";
        else
            statusString = "ERROR";
        statusDisplay.setText(statusString);
    }



}
