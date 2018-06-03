package Controllers;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ReserveSeatController {

    @FXML
    private TextField user;

    @FXML
    private TextField tripID;

    @FXML
    private TextField seatNo;

    @FXML //todo add errMsg to fxml
    private Label errMsg;

    @FXML
    void HandleSeatSelect(ActionEvent event){
        String usernm = user.getText();
        int trip = Integer.parseInt(tripID.getText());
        int seat = Integer.parseInt(seatNo.getText());

        src.Database db = src.SQL_Database.getInstance();
        if (db.addTicket(usernm, trip, seat, 0, false) == -1)
        {
            errMsg.setText("Seat Unavailable");
            errMsg.setVisible(true);
        }
        else
        {
            errMsg.setVisible(false);
            //todo return to home page?
        }
    }
}
