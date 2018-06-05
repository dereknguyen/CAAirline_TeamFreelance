package Controllers;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import java.time.LocalDate;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private ObservableList<Trip> results;

    @FXML private TabPane B_TripModeTabPane;
    @FXML private TableView<Trip> B_AvailableFlightsTable;
    @FXML private JFXComboBox<String> B_OneWayFrom;
    @FXML private JFXComboBox<String> B_OneWayTo;
    @FXML private JFXDatePicker B_OneWayDepartDate;
    @FXML private JFXButton B_PurchaseFlightButton;

    @FXML private JFXComboBox<String> B_RoundTripFrom;
    @FXML private JFXComboBox<String> B_RoundTripTo;
    @FXML private JFXDatePicker B_RoundTripDepartDate;

    @FXML private TableColumn<Trip, String> B_FromCol;
    @FXML private TableColumn<Trip, String> B_ToCol;
    @FXML private TableColumn<Trip, String> B_DepartDateCol;
    @FXML private TableColumn<Trip, String> B_PriceCol;
    @FXML private Label B_ErrMsg;

    @FXML private JFXTextField CI_FlightID;
    @FXML private Label CI_ErrMsg;

    @FXML private JFXTextField FS_FlightID;
    @FXML private TableView<Trip> FS_FlightStatusTable;
    @FXML private TableColumn<Trip, String> FS_FromCol;
    @FXML private TableColumn<Trip, String> FS_ToCol;
    @FXML private TableColumn<Trip, String> FS_DepartDateCol;
    @FXML private TableColumn<Trip, String> FS_StatusCol;
    @FXML private Label FS_ErrMsg;

    @FXML private TableView<Ticket> MF_MyFlightTable;
    @FXML private TableColumn<Ticket, String> MF_FlightIDCol;
    @FXML private TableColumn<Ticket, String> MF_FromCol;
    @FXML private TableColumn<Ticket, String> MF_ToCol;
    @FXML private TableColumn<Ticket, String> MF_DepartDateCol;
    @FXML private TableColumn<Ticket, String> MF_StatusCol;
    @FXML private TableColumn<Ticket, Boolean> MF_CheckedInStatusCol;
    @FXML private TableColumn<Ticket, String> MF_NumBagsCol;


    @FXML
    void initialize() {
        B_OneWayFrom.getItems().addAll("San Luis Obispo", "Los Angeles", "San Francisco", "San Diego", "Phoenix", "Seattle", "Dallas");
        B_OneWayTo.getItems().addAll("San Luis Obispo", "Los Angeles", "San Francisco", "San Diego", "Phoenix", "Seattle", "Dallas");
        B_RoundTripFrom.getItems().addAll("San Luis Obispo", "Los Angeles", "San Francisco", "San Diego", "Phoenix", "Seattle", "Dallas");
        B_RoundTripTo.getItems().addAll("San Luis Obispo", "Los Angeles", "San Francisco", "San Diego", "Phoenix", "Seattle", "Dallas");

        B_TripModeTabPane.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.intValue() == 1) {
                        B_PurchaseFlightButton.setText("Choose 1st Flight");
                    }
                    else {
                        B_PurchaseFlightButton.setText("Purchase Flight");
                    }
                }
        );
    }

    @FXML
    void CI_HandleCheckIn() {
        SQL_Database db = SQL_Database.getInstance();
        String username = CustomerControl.getInstance().getCustomer().getUserName();
        int id;

        try {
            id = Integer.parseInt(CI_FlightID.getText().trim());
            if (db.checkIn(username, id) == 0) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/CustomerBaggageView.fxml"));
                Parent root;

                try {
                    root = loader.load();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                Stage stage = new Stage();
                stage.setTitle("Declare Baggage");
                stage.setScene(new Scene(root));

                CustomerBaggageViewController controller = loader.getController();
                controller.setTripID(id);

                CI_ErrMsg.setVisible(false);
                CI_FlightID.getScene().getWindow().hide();
                stage.show();

            }
            else {
                CI_ErrMsg.setText("No matching ticket number found");
                CI_ErrMsg.setVisible(true);
            }
        }
        catch (NumberFormatException e) {
            CI_ErrMsg.setText("Please enter an integer");
            CI_ErrMsg.setVisible(true);
        }
    }

    @FXML
    void B_HandlePurchase() {

        int mode = B_TripModeTabPane.getSelectionModel().getSelectedIndex();


        if (mode == ONE_WAY) {
            // Get selected TABLE ROW in Booking Tab
            Trip selectedTrip = B_AvailableFlightsTable.getSelectionModel().getSelectedItem();
            if (selectedTrip == null) return;

            int tripID = getSelectedTripID(selectedTrip,
                    selectedTrip.getFromString(),
                    selectedTrip.getToString());

            // Present Payment View. We also want to
            //  transfer over selected data to payment view controller.
            if (tripID != -1) {
                toPaymentView_OneWay(tripID);
            }
        }
        else if (mode == ROUND_TRIP){

            // GRAB STARTING TRIP
            Trip selectedTrip = B_AvailableFlightsTable.getSelectionModel().getSelectedItem();
            if (selectedTrip == null) return;

            int tripID = getSelectedTripID(selectedTrip, selectedTrip.getFromString(), selectedTrip.getToString());

            String from = B_RoundTripFrom.getSelectionModel().getSelectedItem();
            String to = B_RoundTripTo.getSelectionModel().getSelectedItem();

            if (tripID != -1) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ReturnFlightSelectionView.fxml"));
                Parent root;

                try {
                    root = loader.load();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                ReturnFlightSelectionViewController controller = loader.getController();
                Stage stage = new Stage();
                controller.setLocations(to, from, tripID); // Reverse the from-to
                stage.setTitle("Select Returning Trip");
                stage.setScene(new Scene(root));

                B_AvailableFlightsTable.getItems().removeAll(results);

                stage.show();
            }

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
            String from = B_OneWayFrom.getSelectionModel().getSelectedItem();
            String to = B_OneWayTo.getSelectionModel().getSelectedItem();
            LocalDate date = B_OneWayDepartDate.getValue();
            B_ErrMsg.setText(searchFlight(from, to, date));
        }
        else if (selectedIndex == 1) {
            String from = B_RoundTripFrom.getSelectionModel().getSelectedItem();
            String to = B_RoundTripTo.getSelectionModel().getSelectedItem();
            LocalDate date = B_RoundTripDepartDate.getValue();
            B_ErrMsg.setText(searchFlight(from, to, date));
        }
    }

    @FXML
    void FS_HandleGetFlightStatus()
    {
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
        results = FXCollections.observableArrayList(t);
        if (t == null) return;

        FS_ErrMsg.setVisible(false);
        FS_FromCol.setCellValueFactory(new PropertyValueFactory<>("FromString"));
        FS_ToCol.setCellValueFactory(new PropertyValueFactory<>("ToString"));
        FS_DepartDateCol.setCellValueFactory(new PropertyValueFactory<>("DateString"));
        FS_StatusCol.setCellValueFactory(new PropertyValueFactory<>("StatusString"));

        FS_FlightStatusTable.setItems(results);
    }

    @FXML
    void MF_HandleRefresh() {
        SQL_Database db = SQL_Database.getInstance();
        ObservableList<Ticket> myFlights = FXCollections.observableArrayList(
                db.getTicketsByUsername(CustomerControl.getInstance().getCustomer().getUserName())
        );

        MF_FlightIDCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        MF_FromCol.setCellValueFactory(new PropertyValueFactory<>("FromString"));
        MF_ToCol.setCellValueFactory(new PropertyValueFactory<>("ToString"));
        MF_DepartDateCol.setCellValueFactory(new PropertyValueFactory<>("DepartDateString"));
        MF_StatusCol.setCellValueFactory(new PropertyValueFactory<>("FlightStatus"));
        MF_NumBagsCol.setCellValueFactory(new PropertyValueFactory<>("NumberOfBags"));
        MF_CheckedInStatusCol.setCellValueFactory(new PropertyValueFactory<>("CheckedInStatus"));

        MF_MyFlightTable.setItems(myFlights);
    }

    @FXML
    void MF_HandleCancel() {
        SQL_Database db = SQL_Database.getInstance();
        Session s = Session.getInstance();
        Ticket selectedTicket = MF_MyFlightTable.getSelectionModel().getSelectedItem();
        if (selectedTicket == null) return;
        db.removeTicket(s.getUsername(), selectedTicket.getTripId(), selectedTicket.getSeatNumber());
        MF_HandleRefresh();
    }


    /* HELPERS */
    private String searchFlight(String from, String to, LocalDate localDate) {
        System.out.println("\nSearching:");

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
            results = FXCollections.observableArrayList(
                    db.getTripsByFlightAndDate(db.getFlightId(from, to), departDate)
            );
            /* remove cancelled trips */
            for (Trip t : results)
            {
                if (t.getStatus() == 2)
                {
                    results.remove(t);
                }
            }

            B_FromCol.setCellValueFactory(new PropertyValueFactory<>("FromString"));
            B_ToCol.setCellValueFactory(new PropertyValueFactory<>("ToString"));
            B_DepartDateCol.setCellValueFactory(new PropertyValueFactory<>("DateString"));
            B_PriceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));

            B_AvailableFlightsTable.setItems(results);
        }
        return null;
    }


    private int getSelectedTripID(Trip selectedTrip, String from, String to) {
        SQL_Database db = SQL_Database.getInstance();

        if (selectedTrip != null) {

            int flightID = db.getFlightId(from, to);

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

    private void toPaymentView_OneWay(int tripID) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/PaymentView.fxml"));
        Parent root;

        try {
            root = loader.load();
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }

        PaymentViewController controller = loader.getController();
        Stage stage = new Stage();
        controller.setTripID(tripID);
        controller.setSelectedMode(ONE_WAY);
        controller.hideReturnSeat();
        stage.setTitle("Confirm Ticket");
        stage.setScene(new Scene(root));

        B_AvailableFlightsTable.getItems().removeAll(results);
        B_AvailableFlightsTable.getScene().getWindow().hide();
        stage.show();
    }
}
