package Controllers;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import src.CustomerControl;
import src.SQL_Database;
import src.Ticket;

public class CustomerBaggageViewController {

    private int tripID = 0;
    SQL_Database db = SQL_Database.getInstance();

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private JFXTextField numCarryOn;
    @FXML private JFXTextField carryOnWeight;
    @FXML private JFXTextField numCheckIn;
    @FXML private JFXTextField checkInWeight;

    @FXML
    void HandleBack(ActionEvent event) {
        String username = CustomerControl.getInstance().getCustomer().getUserName();
        int seatNumber = getSeatNumber(username, this.tripID);

        if (seatNumber != -1) {
            int result = db.editTicket(username, this.tripID, seatNumber, 0, false);
            if (result != -1) {
                numCarryOn.getScene().getWindow().hide();
            }
        }
    }

    @FXML
    void HandleConfirmCheckIn(ActionEvent event) {
        String username = CustomerControl.getInstance().getCustomer().getUserName();
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
                // TODO: End Session
            }
        }
        catch (Exception e) {
            // TODO: Print number format error
        }
    }

    @FXML
    void initialize() {

    }

    public void setTripID(int id) {
        this.tripID = id;
    }

    private int getSeatNumber(String username, int tripID) {
        ArrayList<Ticket> tickets = this.db.getTicketsByUser(username);

        for (Ticket t: tickets) {
            if (t.getTripId() == tripID) {
                return t.getSeatNumber();
            }
        }
        return -1;
    }
}
