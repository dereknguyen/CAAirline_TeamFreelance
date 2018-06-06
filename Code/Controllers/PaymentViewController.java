package Controllers;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import src.SQL_Database;
import src.Session;

import java.util.ArrayList;

public class PaymentViewController {

    private static final int ONE_WAY = 0;
    private static final int ROUND_TRIP = 1;

    private int tripID;
    private int returnTripID;
    private int selectedMode;
    private SQL_Database db;

    @FXML private JFXComboBox<Integer> seatSelection;
    @FXML private JFXComboBox<Integer> returnSeatSelection;
    @FXML private Label totalCost;
    @FXML private Label ErrMsg;

    @FXML
    void HandleBack() {
        totalCost.getScene().getWindow().hide();
        Utilities.present("/Views/CustomerMainView.fxml", "Main");
    }

    @FXML
    void HandlePayment() {

        Session s = Session.getInstance();
        String username = s.getUsername();

        if (this.selectedMode == ONE_WAY) {
            if (seatSelection.getSelectionModel().getSelectedItem() == null)
            {
                ErrMsg.setText("Please select a seat number");
                ErrMsg.setVisible(true);
                return;
            }
            ErrMsg.setVisible(false);
            int seat = seatSelection.getSelectionModel().getSelectedItem();

            System.out.println(username);
            System.out.println(this.tripID);
            System.out.println(seat);

            if (this.db.addTicket(username, this.tripID, seat) == 0) {
                totalCost.getScene().getWindow().hide();
            }
        }
        else if (this.selectedMode == ROUND_TRIP) {
            if (seatSelection.getSelectionModel().getSelectedItem() == null)
            {
                ErrMsg.setText("Please select an outgoing seat number");
                ErrMsg.setVisible(true);
                return;
            }
            if (returnSeatSelection.getSelectionModel().getSelectedItem() == null)
            {
                ErrMsg.setText("Please select a returning seat number");
                ErrMsg.setVisible(true);
                return;
            }
            ErrMsg.setVisible(false);
            int departSeat = seatSelection.getSelectionModel().getSelectedItem();
            int returnSeat = returnSeatSelection.getSelectionModel().getSelectedItem();

            if (this.db.addTicket(username, this.tripID, departSeat) == 0) {
                if (this.db.addTicket(username, this.returnTripID, returnSeat) == 0) {
                    totalCost.getScene().getWindow().hide();
                }
            }
        }
    }

    @FXML
    void initialize() {

        this.db = SQL_Database.getInstance();
        ArrayList<Integer> takenSeatList = db.getFullSeats(this.tripID);
        ArrayList<Integer> seatList = new ArrayList<>();

        for (int i = 0; i < 20; i++) { seatList.add(i); }

        seatList.removeAll(takenSeatList);

        for (Integer seat: seatList) {
            seatSelection.getItems().add(seat);
        }

    }

    public void setTripID(int id) {
        this.tripID = id;
    }
    public void setTripID(int startingTripID, int returnTripID) {
        this.tripID = startingTripID;
        this.returnTripID = returnTripID;
    }

    public void setSelectedMode(int mode) {
        this.selectedMode = mode;
    }
    public void hideReturnSeat() {
        returnSeatSelection.setDisable(true);
        returnSeatSelection.setVisible(false);
    }

    public void loadReturnSeat() {
        this.db = SQL_Database.getInstance();
        ArrayList<Integer> takenSeatList = db.getFullSeats(this.returnTripID);
        ArrayList<Integer> seatList = new ArrayList<>();

        for (int i = 0; i < 20; i++) { seatList.add(i); }

        seatList.removeAll(takenSeatList);

        for (Integer seat: seatList) {
            returnSeatSelection.getItems().add(seat);
        }

    }
}
