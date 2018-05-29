package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class MainTabsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXComboBox<?> bookingFrom;

    @FXML
    private JFXComboBox<?> bookingTo;

    @FXML
    private JFXDatePicker bookingDepartureDate;

    @FXML
    private JFXDatePicker bookingArrivalDate;

    @FXML
    private JFXButton bookingSearchButton;

    @FXML
    private JFXButton bookingConfirmFlight;

    @FXML
    private JFXTextField checkinFlightNumber;

    @FXML
    private JFXButton checkinButton;

    @FXML
    private JFXTextField statusFlightNumber;

    @FXML
    private JFXButton statusViewButton;

    @FXML
    private TableView<?> statusTable;

    @FXML
    void checkinDidTap(ActionEvent event) {

    }

    @FXML
    void confirmFlightDidTap(ActionEvent event) {

    }

    @FXML
    void searchTicketsDidTap(ActionEvent event) {

    }

    @FXML
    void viewStatusDidTap(ActionEvent event) {

    }

    @FXML
    void initialize() {


    }
}