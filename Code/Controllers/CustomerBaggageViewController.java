package Controllers;

import com.jfoenix.controls.JFXTextField;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import src.SQL_Database;
import src.Session;
import src.Ticket;

public class CustomerBaggageViewController {

    private int tripID = 0;
    private SQL_Database db = SQL_Database.getInstance();

    @FXML private JFXTextField numCarryOn;
    @FXML private JFXTextField carryOnWeight;
    @FXML private JFXTextField numCheckIn;
    @FXML private JFXTextField checkInWeight;
    @FXML private Label ErrMsg;

    @FXML
    void HandleBack() {

        String username = Session.getInstance().getUsername();
        int seatNumber = getSeatNumber(username, this.tripID);

        if (seatNumber != -1) {
            int result = db.editTicket(username, this.tripID, seatNumber, 0, false);
            if (result != -1) {
                numCarryOn.getScene().getWindow().hide();
                Utilities.present("/Views/CustomerMainView.fxml", "Main");
            }
            else {
                System.out.println("Edit Ticket Failed");
            }
        }
        else {
            System.out.println("Edit Ticket Failed");
        }
    }

    @FXML
    void HandleConfirmCheckIn() {
        String username = Session.getInstance().getUsername();
        int seatNumber = getSeatNumber(username, this.tripID);

        int carryOn = 0, checkIn = 0, carryWeight = 0, checkWeight = 0, totalBags = 0;

        try {
            carryOn = Integer.parseInt(numCarryOn.getText().trim());
            checkIn = Integer.parseInt(numCheckIn.getText().trim());
            carryWeight = Integer.parseInt(carryOnWeight.getText().trim());
            checkWeight = Integer.parseInt(checkInWeight.getText().trim());

            totalBags = carryOn + checkIn;

            int result = db.editTicket(username, this.tripID, seatNumber, totalBags, true);

            if (result != -1) {
                ErrMsg.setVisible(false);
                numCarryOn.getScene().getWindow().hide();
                Utilities.present("/Views/CustomerMainView.fxml", "Main");
            }
        }
        catch (NumberFormatException e) {
            ErrMsg.setText("Please enter an integer");
            ErrMsg.setVisible(true);
        }
    }

    void setTripID(int id) {
        this.tripID = id;
    }

    private int getSeatNumber(String username, int tripID) {
        ArrayList<Ticket> tickets = this.db.getTicketsByUsername(username);

        for (Ticket t: tickets) {
            if (t.getTripId() == tripID) {
                return t.getSeatNumber();
            }
        }
        return -1;
    }
}
