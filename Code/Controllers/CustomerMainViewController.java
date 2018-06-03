package Controllers;

import java.sql.Date;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CustomerMainViewController {

    @FXML private TabPane B_TripModeTabPane;

    @FXML private JFXComboBox<String> B_OneWayFrom;
    @FXML private JFXComboBox<String> B_OneWayTo;
    @FXML private JFXDatePicker B_OneWayDepartDate;

    @FXML private JFXComboBox<String> B_RoundTripFrom;
    @FXML private JFXComboBox<String> B_RoundTripTo;
    @FXML private JFXDatePicker B_RoundTripDepartDate;
    @FXML private JFXDatePicker B_RoundTripReturnDate;

    @FXML private TableColumn<?, ?> B_FromCol;
    @FXML private TableColumn<?, ?> B_ToCol;
    @FXML private TableColumn<?, ?> B_DepartTimeCol;
    @FXML private TableColumn<?, ?> B_ArrivalTimeCol;
    @FXML private TableColumn<?, ?> B_PriceCol;
    @FXML private Label B_ErrMsg;

    @FXML private JFXTextField CI_FlightNumber;
    @FXML private Label CI_ErrMsg;

    @FXML private JFXTextField FS_FlightNumber;
    @FXML private TableColumn<?, ?> FS_FromCol;
    @FXML private TableColumn<?, ?> FS_ToCol;
    @FXML private TableColumn<?, ?> FS_DepartTimeCol;
    @FXML private TableColumn<?, ?> FS_ArrivalTimeCol;
    @FXML private TableColumn<?, ?> FS_StatusCol;
    @FXML private Label FS_ErrMsg;

    @FXML
    void initialize() {
        B_OneWayFrom.getItems().addAll("Los Angeles", "San Francisco", "San Diego", "Arizona", "Seattle", "Dallas");
        B_OneWayTo.getItems().addAll("Los Angeles", "San Francisco", "San Diego", "Arizona", "Seattle", "Dallas");
        B_RoundTripFrom.getItems().addAll("Los Angeles", "San Francisco", "San Diego", "Arizona", "Seattle", "Dallas");
        B_RoundTripTo.getItems().addAll("Los Angeles", "San Francisco", "San Diego", "Arizona", "Seattle", "Dallas");
    }

    @FXML
    void HandleCheckIn(ActionEvent event) {
        // TODO: Pass flight ID to next view controller
        Utilities.present("/Views/CustomerBaggageView.fxml", "Baggage Declaration");
    }

    @FXML
    void HandlePurchase(ActionEvent event) {
        // TODO: Get selected Row the purchase ticket
        Utilities.present("/Views/PaymentView.fxml", "Confirm Ticket");
    }

    /*
     * Search for available tickets given the parameters:
     *  - From Location (JFXComboBox)
     *  - To Location (JFXComboBox)
     *  - Departure Date (JFXDatePicker)
     *  - Return Date (JFXDatePicker)
     *
     * Will Check B_TripModeTabPane for either:
     *  - One Way (Index 0)
     *  - Round Trip (Index 1)
     *
     * Must Verify that user have input all of the necessary information.
     *  Else, must print missing information as error message.
     *
     * Populate table with available flight information.
     */
    @FXML
    void searchForTickets() {
        int selectedIndex = B_TripModeTabPane.getSelectionModel().getSelectedIndex();

        if (selectedIndex == 0) {
            B_ErrMsg.setText(searchOneWay());
        }
        else if (selectedIndex == 1) {
            B_ErrMsg.setText(searchRoundTrip());
        }
    }

    @FXML
    void HandleGetFlightStatus(ActionEvent event) {

    }

    @FXML
    void AF_HandleRefresh() {

    }


    private String searchOneWay() {
        System.out.println("\nSearching: One Way");

        String from = B_OneWayFrom.getSelectionModel().getSelectedItem();
        String to = B_OneWayTo.getSelectionModel().getSelectedItem();
        LocalDate localD = B_OneWayDepartDate.getValue();

        if (from == null) {
            System.out.println("\tError: From location missing");
            return "Please specify location you are from.";
        }
        else if (to == null) {
            System.out.println("\tError: To location missing");
            return "Please specify location are are going to.";
        }
        else if (localD == null) {
            System.out.println("\tError: Departure date missing");
            return "Please specify departure date.";
        }
        else {
            Date departDate = Date.valueOf(localD);

            // TODO: PULL FROM DATABASE

        }

        return null;
    }

    private String searchRoundTrip() {
        System.out.println("\nSearching: Round Trip");

        String from = B_RoundTripFrom.getSelectionModel().getSelectedItem();
        String to = B_RoundTripTo.getSelectionModel().getSelectedItem();

        LocalDate departLocal = B_RoundTripDepartDate.getValue();
        LocalDate returnLocal = B_RoundTripReturnDate.getValue();

        if (from == null) {
            System.out.println("\tError: From location missing");
            return "Please specify location you are from.";
        }
        else if (to == null) {
            System.out.println("\tError: To location missing");
            return "Please specify location are are going to.";
        }
        else if (departLocal == null || returnLocal == null) {
            System.out.println("\tError: Departure date or Return date missing");
            return "Missing either depart date or return date";
        }
        else {
            Date departDate = Date.valueOf(departLocal);
            Date returnDate = Date.valueOf(returnLocal);

            // TODO: PULL FROM DATABASE
        }

        return null;
    }
}
