package Controllers;

import java.sql.Date;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import src.Database;
import src.SQL_Database;
import src.Trip;

public class CustomerMainViewController {

    @FXML private TabPane B_TripModeTabPane;
    @FXML private TableView<?> B_AvailableFlightsTable;
    @FXML private JFXComboBox<String> B_OneWayFrom;
    @FXML private JFXComboBox<String> B_OneWayTo;
    @FXML private JFXDatePicker B_OneWayDepartDate;

    @FXML private JFXComboBox<String> B_RoundTripFrom;
    @FXML private JFXComboBox<String> B_RoundTripTo;
    @FXML private JFXDatePicker B_RoundTripDepartDate;
    @FXML private JFXDatePicker B_RoundTripReturnDate;

    @FXML private TableColumn<?, ?> B_FromCol;
    @FXML private TableColumn<?, ?> B_ToCol;
    @FXML private TableColumn<?, ?> B_DepartDateCol;
    @FXML private TableColumn<?, ?> B_ReturnDateCol;
    @FXML private TableColumn<?, ?> B_PriceCol;
    @FXML private Label B_ErrMsg;

    @FXML private JFXTextField CI_FlightID;
    @FXML private Label CI_ErrMsg;

    @FXML private JFXTextField FS_FlightID;
    @FXML private TableView<?> FS_FlightStatusTable;
    @FXML private TableColumn<?, ?> FS_FromCol;
    @FXML private TableColumn<?, ?> FS_ToCol;
    @FXML private TableColumn<?, ?> FS_DepartDateCol;
    @FXML private TableColumn<?, ?> FS_ReturnDateCol;
    @FXML private TableColumn<?, ?> FS_StatusCol;
    @FXML private Label FS_ErrMsg;

    @FXML private TableView<?> MF_MyFlightTable;
    @FXML private TableColumn<?, ?> MF_FlightIDCol;
    @FXML private TableColumn<?, ?> MF_FromCol;
    @FXML private TableColumn<?, ?> MF_ToCol;
    @FXML private TableColumn<?, ?> MF_DepartDateCol;
    @FXML private TableColumn<?, ?> MF_ReturnDateCol;
    @FXML private TableColumn<?, ?> MF_StatusCol;


    @FXML
    void initialize() {
        B_OneWayFrom.getItems().addAll("San Luis Obispo", "Los Angeles", "San Francisco", "San Diego", "Phoenix", "Seattle", "Dallas");
        B_OneWayTo.getItems().addAll("San Luis Obispo", "Los Angeles", "San Francisco", "San Diego", "Phoenix", "Seattle", "Dallas");
        B_RoundTripFrom.getItems().addAll("San Luis Obispo", "Los Angeles", "San Francisco", "San Diego", "Phoenix", "Seattle", "Dallas");
        B_RoundTripTo.getItems().addAll("San Luis Obispo", "Los Angeles", "San Francisco", "San Diego", "Phoenix", "Seattle", "Dallas");
    }

    @FXML
    void CI_HandleCheckIn(ActionEvent event) {
        // TODO: [1] Get Flight ID

        // TODO: [2] Pass flight ID to baggage view controller
        /* Will perform to Baggage in conjunction with [2] */
        Utilities.present("/Views/CustomerBaggageView.fxml", "Baggage Declaration");
    }

    @FXML
    void B_HandlePurchase(ActionEvent event) {
        // TODO: [1] Get selected TABLE ROW in Booking Tab

        // TODO: [2] Present Payment View. We also want to transfer over selected data to payment view controller.
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
    void B_HandleSearch() {
        int selectedIndex = B_TripModeTabPane.getSelectionModel().getSelectedIndex();

        if (selectedIndex == 0) {
            B_ErrMsg.setText(searchOneWay());
        }
        else if (selectedIndex == 1) {
            B_ErrMsg.setText(searchRoundTrip());
        }
    }

    @FXML
    void FS_HandleGetFlightStatus(ActionEvent event) {
        // TODO: [1] Grab the flight ID from text field

        // TODO: [2] Display the flight associate with the ID on the table in Flight Status Tab.
    }

    @FXML
    void MF_HandleRefresh() {
        // TODO: Just pull what flight associate with the customer and display it in the My Flight Table.
    }


    /* HELPERS */

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
            Calendar c = Calendar.getInstance();
            Date departDate = Date.valueOf(localD);
            c.setTime(departDate);

            /* TODO: PULL FROM DATABASE
             *
             * Currently, Nick have it set up so that it will print to console.
             * What we want is to populate the table with the queried data.
             */
            Database db = SQL_Database.getInstance();
            ArrayList<Trip> results = db.getTripsByFlightAndDate(db.getFlightId(from, to), c);
            for (Trip t : results)
            {
                System.out.println(t.getTripId() + " " + t.getFlightId() + " "
                        + t.getDate().getTime() + " " + t.getPrice() + " " + t.getStatus());
            }

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
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(departDate);
            c2.setTime(returnDate);

            /* TODO: PULL FROM DATABASE
             *
             * Same thing as searchOneWay
             */

        }

        return null;
    }
}
