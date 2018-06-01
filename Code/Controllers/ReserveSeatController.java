package Controllers;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ReserveSeatController {

    @FXML
    private TextField user;

    @FXML
    private TextField tripID;

    @FXML
    private TextField seatNo;

    @FXML
    void HandleSeatSelect(ActionEvent event){
        String usernm = user.getText();
        int trip = Integer.parseInt(tripID.getText());
        int seat = Integer.parseInt(seatNo.getText());

        src.Database db = src.SQL_Database.getInstance();
        db.addTicket(usernm, trip, seat, 0, false);
    }
}
