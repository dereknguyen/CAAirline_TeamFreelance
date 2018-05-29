package Controllers;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ReserveSeatController {

    @FXML
    private JFXTextField user;

    @FXML
    private JFXTextField tripID;

    @FXML
    private JFXTextField seatNo;

    @FXML
    void HandleSeatSelect(ActionEvent event){
        String usernm = user.getText();
        int trip = Integer.parseInt(tripID.getText());
        int seat = Integer.parseInt(seatNo.getText());

        src.Database db = src.SQL_Database.getInstance();
        db.addTicket(usernm, trip, seat, false);
    }
}
