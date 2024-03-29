package Controllers;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import src.SQL_Database;
import src.Session;
import src.Trip;

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

            if (seatSelection.getSelectionModel().getSelectedItem() == null) {
                ErrMsg.setText("Please select a seat number");
                ErrMsg.setVisible(true);
                return;
            }

            ErrMsg.setVisible(false);
            int seat = seatSelection.getSelectionModel().getSelectedItem();


            if (this.db.addTicket(username, this.tripID, seat) == 0) {

                presentTicektView(
                        this.db.getTripInfo(this.tripID),
                        null
                );

                totalCost.getScene().getWindow().hide();
            }
        }
        else if (this.selectedMode == ROUND_TRIP) {

            if (seatSelection.getSelectionModel().getSelectedItem() == null) {
                ErrMsg.setText("Please select an outgoing seat number");
                ErrMsg.setVisible(true);
                return;
            }

            if (returnSeatSelection.getSelectionModel().getSelectedItem() == null) {
                ErrMsg.setText("Please select a returning seat number");
                ErrMsg.setVisible(true);
                return;
            }

            ErrMsg.setVisible(false);
            int departSeat = seatSelection.getSelectionModel().getSelectedItem();
            int returnSeat = returnSeatSelection.getSelectionModel().getSelectedItem();

            if (this.db.addTicket(username, this.tripID, departSeat) == 0) {
                if (this.db.addTicket(username, this.returnTripID, returnSeat) == 0) {

                    presentTicektView(
                            this.db.getTripInfo(this.tripID),
                            this.db.getTripInfo(this.returnTripID)
                    );

                    totalCost.getScene().getWindow().hide();
                }
            }
        }
    }

    private void presentTicektView(Trip trip, Trip returnTrip) {
        Stage stage = new Stage();
        FXMLLoader loader = Utilities.present(stage, "/Views/PaymentSuccess.fxml", "Your Ticket");
        PaymentSuccessViewController controller = loader.getController();
        controller.setInfo(trip, returnTrip);
        stage.show();
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

    void setTripID(int id) {
        this.tripID = id;
    }
    void setTripID(int startingTripID, int returnTripID) {
        this.tripID = startingTripID;
        this.returnTripID = returnTripID;
    }

    void setSelectedMode(int mode) {
        this.selectedMode = mode;
    }

    void hideReturnSeat() {
        returnSeatSelection.setDisable(true);
        returnSeatSelection.setVisible(false);
    }

    void loadReturnSeat() {
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
