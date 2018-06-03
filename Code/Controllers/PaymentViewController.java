package Controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PaymentViewController {

    @FXML private JFXComboBox<String> seatSelection;
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

    }

    @FXML
    void initialize() {

        // TODO: Grab available seats from database

    }
}
