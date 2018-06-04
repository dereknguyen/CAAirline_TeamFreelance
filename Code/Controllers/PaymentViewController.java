package Controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import src.SQL_Database;

import java.util.ArrayList;

public class PaymentViewController {

    private static final int ONE_WAY = 0;
    private static final int ROUND_TRIP = 1;

    private int tripID;
    private int selectedMode;

    @FXML private JFXComboBox<Integer> seatSelection;
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
    }

    @FXML
    void HandlePayment(ActionEvent event) {
        System.out.println(this.tripID);
        // TODO: We assume all payment are a success
    }

    @FXML
    void initialize() {
        // TODO: Grab available seats from database and populate the seatSelection Combo Box
        SQL_Database db = SQL_Database.getInstance();
        ArrayList<Integer> takenSeatList = db.getFullSeats(this.tripID);

        System.out.println(takenSeatList);

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

    public void setSelectedMode(int mode) {
        this.selectedMode = mode;
    }
}
