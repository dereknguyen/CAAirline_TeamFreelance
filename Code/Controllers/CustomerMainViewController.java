package Controllers;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import java.time.LocalDate;
import java.util.Locale;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import src.*;

public class CustomerMainViewController {

    private static final int ONE_WAY = 0;
    private static final int ROUND_TRIP = 1;

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
        // Get selected TABLE ROW in Booking Tab
        int tripID = getSelectedTripID();

        // Present Payment View. We also want to
        //  transfer over selected data to payment view controller.
        if (tripID != 0) {
            toPaymentView(tripID);
        }
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

        // TODO: [2] Display the flight associate with the ID on the table in Flight Status Tab.
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
        LocalDate localDate = B_OneWayDepartDate.getValue();

        if (from == null) {
            System.out.println("\tError: From location missing");
            return "Please specify location you are from.";
        }
        else if (to == null) {
            System.out.println("\tError: To location missing");
            return "Please specify location are are going to.";
        }
        else if (localDate == null) {
            System.out.println("\tError: Departure date missing");
            return "Please specify departure date.";
        }
        else {
            Calendar departDate = Calendar.getInstance();
            departDate.setTime(Date.valueOf(localDate));

            Database db = SQL_Database.getInstance();
            ObservableList<Trip> results = FXCollections.observableArrayList(
                    db.getTripsByFlightAndDate(db.getFlightId(from, to), departDate)
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

            Calendar departDate = Calendar.getInstance();
            Calendar returnDate = Calendar.getInstance();

            departDate.setTime(Date.valueOf(departLocal));
            returnDate.setTime(Date.valueOf(returnLocal));

            Database db = SQL_Database.getInstance();
            ObservableList<Trip> results = FXCollections.observableArrayList(
                    db.getRoundTrips(db.getFlightId(from, to), departDate, returnDate)
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

    private int getSelectedTripID() {
        SQL_Database db = SQL_Database.getInstance();
        Trip selectedTrip = B_AvailableFlightsTable.getSelectionModel().getSelectedItem();

        if (selectedTrip != null) {
            int flightID = db.getFlightId(selectedTrip.getFromString(), selectedTrip.getToString());

            Calendar date = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM HH:mm:ss z yyyy", Locale.ENGLISH);

            try {
                date.setTime(sdf.parse(selectedTrip.getDateString()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return db.getTripId(flightID, date);
        }

        return 0;
    }

    private void toPaymentView(int tripID) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/PaymentView.fxml"));

        Parent root = null;

        try {
            root = loader.load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        PaymentViewController controller = loader.getController();
        Stage stage = new Stage();
        controller.setTripID(tripID);
        controller.setSelectedMode(ONE_WAY);

        stage.setTitle("Confirm Ticket");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
