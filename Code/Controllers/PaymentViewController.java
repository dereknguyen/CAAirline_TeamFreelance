package Controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import src.CustomerControl;
import src.SQL_Database;

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
    @FXML private JFXTextField creditCardNumber;
    @FXML private JFXTextField CSV;
    @FXML private JFXDatePicker cardExpDate;
    @FXML private JFXTextField cardFullName;
    @FXML private JFXTextField cardBillingAddress;
    @FXML private JFXTextField cardCity;
    @FXML private JFXTextField cardState;
    @FXML private JFXTextField cardZip;
    @FXML private Label totalCost;

    @FXML
    void HandleBack(ActionEvent event) {
        totalCost.getScene().getWindow().hide();
        Utilities.present("/Views/CustomerMainView.fxml", "Main");
    }

    @FXML
    void HandlePayment(ActionEvent event) {

        String username = CustomerControl.getInstance().getCustomer().getUserName();



        if (this.selectedMode == ONE_WAY) {
            int seat = seatSelection.getSelectionModel().getSelectedItem();

            System.out.println(username);
            System.out.println(this.tripID);
            System.out.println(seat);

            if (this.db.addTicket(username, this.tripID, seat) == 0) {
                CustomerControl.getInstance().getCustomerFromDB(username); // Reload
                totalCost.getScene().getWindow().hide();
            }
        }
        else if (this.selectedMode == ROUND_TRIP) {
            int departSeat = seatSelection.getSelectionModel().getSelectedItem();
            int returnSeat = returnSeatSelection.getSelectionModel().getSelectedItem();

            if (this.db.addTicket(username, this.tripID, departSeat) == 0) {
                CustomerControl.getInstance().getCustomerFromDB(username); // Reload
                if (this.db.addTicket(username, this.returnTripID, returnSeat) == 0) {
                    CustomerControl.getInstance().getCustomerFromDB(username); // Reload
                    totalCost.getScene().getWindow().hide();
                }
            }
        }
    }

    @FXML
    void initialize() {

        this.db = SQL_Database.getInstance();
        ArrayList<Integer> takenSeatList = db.getFullSeats(this.tripID);
        ArrayList<Integer> seatList = new ArrayList<Integer>();

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
        ArrayList<Integer> seatList = new ArrayList<Integer>();

        for (int i = 0; i < 20; i++) { seatList.add(i); }

        seatList.removeAll(takenSeatList);

        for (Integer seat: seatList) {
            returnSeatSelection.getItems().add(seat);
        }

    }
}
