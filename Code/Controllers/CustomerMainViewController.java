package Controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import java.time.LocalDate;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import src.*;

public class CustomerMainViewController {

    @FXML private TabPane B_TripModeTabPane;
    @FXML private TableView<Trip> B_AvailableFlightsTable;
    @FXML private JFXComboBox<String> B_OneWayFrom;
    @FXML private JFXComboBox<String> B_OneWayTo;
    @FXML private JFXDatePicker B_OneWayDepartDate;

    @FXML private JFXComboBox<String> B_RoundTripFrom;
    @FXML private JFXComboBox<String> B_RoundTripTo;
    @FXML private JFXDatePicker B_RoundTripDepartDate;
    @FXML private JFXDatePicker B_RoundTripReturnDate;

    @FXML private TableColumn<Trip, String> B_FromCol;
    @FXML private TableColumn<Trip, String> B_ToCol;
    @FXML private TableColumn<Trip, String> B_DepartDateCol;
    @FXML private TableColumn<Trip, String> B_ReturnDateCol;
    @FXML private TableColumn<Trip, String> B_PriceCol;
    @FXML private Label B_ErrMsg;

    @FXML private JFXTextField CI_FlightID;
    @FXML private Label CI_ErrMsg;

    @FXML private JFXTextField FS_FlightID;
    @FXML private TableView<Trip> FS_FlightStatusTable;
    @FXML private TableColumn<Trip, String> FS_FromCol;
    @FXML private TableColumn<Trip, String> FS_ToCol;
    @FXML private TableColumn<Trip, String> FS_DepartDateCol;
    @FXML private TableColumn<Trip, String> FS_ReturnDateCol;
    @FXML private TableColumn<Trip, String> FS_StatusCol;
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
     * Will Check tripOptionsTabPane for either:
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

        // TODO: [2] Display the flight associated with the ID on the table in Flight Status Tab.
        int TripId;
        try
        {
            TripId = Integer.parseInt(FS_FlightID.getText());
            if (TripId < 0) throw new NumberFormatException();
        }
        catch (NumberFormatException e)
        {
            FS_ErrMsg.setText("Please enter a valid flight ID");
            FS_ErrMsg.setVisible(true);
            return;
        }
        SQL_Database db = SQL_Database.getInstance();
        Trip t = db.getTripInfo(TripId);
        ObservableList<Trip> result = FXCollections.observableArrayList(t);
        if (t == null) return;

        FS_ErrMsg.setVisible(false);
        FS_FromCol.setCellValueFactory(new PropertyValueFactory<>("FromString"));
        FS_ToCol.setCellValueFactory(new PropertyValueFactory<>("ToString"));
        FS_DepartDateCol.setCellValueFactory(new PropertyValueFactory<>("DateString"));
        if (t.getRTDate() == null)
        {
            FS_ReturnDateCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper("N/A"));
        }
        else
        {
            //todo this will never run
            FS_ReturnDateCol.setCellValueFactory(new PropertyValueFactory<>("RTDateString"));
        }
        FS_StatusCol.setCellValueFactory(new PropertyValueFactory<>("Status"));

        FS_FlightStatusTable.setItems(result);
    }

    @FXML
    void MF_HandleRefresh() {
        // TODO: Just pull what flight associates with the customer and display it in the My Flight Table.
        // TODO add checked in field, seat number, num bags to my flights page?
        SQL_Database db = SQL_Database.getInstance();
        Session s = Session.getInstance();
        ObservableList<Ticket> results = FXCollections.observableArrayList(db.getTicketsByUser(s.getUsername()));
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
            Database db = SQL_Database.getInstance();
            ObservableList<Trip> results = FXCollections.observableArrayList(
                    db.getTripsByFlightAndDate(db.getFlightId(from, to), c)
            );

            B_FromCol.setCellValueFactory(new PropertyValueFactory<>("FromString"));
            B_ToCol.setCellValueFactory(new PropertyValueFactory<>("ToString"));
            B_DepartDateCol.setCellValueFactory(new PropertyValueFactory<>("DateString"));
            B_ReturnDateCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper("N/A"));
            B_PriceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));

            B_AvailableFlightsTable.setItems(results);
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
        } else if (to == null) {
            System.out.println("\tError: To location missing");
            return "Please specify location are are going to.";
        } else if (departLocal == null || returnLocal == null) {
            System.out.println("\tError: Departure date or Return date missing");
            return "Missing either depart date or return date";
        } else {
            Date departDate = Date.valueOf(departLocal);
            Date returnDate = Date.valueOf(returnLocal);
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(departDate);
            c2.setTime(returnDate);

            Database db = SQL_Database.getInstance();
            ObservableList<Trip> results = FXCollections.observableArrayList(
                    db.getRoundTrips(db.getFlightId(from, to), c1, c2)
            );

            B_FromCol.setCellValueFactory(new PropertyValueFactory<>("FromString"));
            B_ToCol.setCellValueFactory(new PropertyValueFactory<>("ToString"));
            B_DepartDateCol.setCellValueFactory(new PropertyValueFactory<>("DateString"));
            B_ReturnDateCol.setCellValueFactory(new PropertyValueFactory<>("RTDateString"));
            B_PriceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));

            B_AvailableFlightsTable.setItems(results);
        }

        return null;
    }
}
