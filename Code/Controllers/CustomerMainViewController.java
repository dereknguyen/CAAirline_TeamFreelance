package Controllers;

import java.sql.Date;
import java.util.Calendar;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import java.time.LocalDate;

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

    @FXML private JFXButton AsEmployeeButton;

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
    @FXML private TableColumn<Ticket, String> MF_SeatNumCol;

    @FXML
    void initialize() {
        B_OneWayFrom.getItems().addAll("San Luis Obispo", "Los Angeles", "San Francisco",
                "San Diego", "Phoenix", "Seattle", "Dallas");
        B_OneWayTo.getItems().addAll("San Luis Obispo", "Los Angeles", "San Francisco",
                "San Diego", "Phoenix", "Seattle", "Dallas");
        B_RoundTripFrom.getItems().addAll("San Luis Obispo", "Los Angeles", "San Francisco",
                "San Diego", "Phoenix", "Seattle", "Dallas");
        B_RoundTripTo.getItems().addAll("San Luis Obispo", "Los Angeles", "San Francisco",
                "San Diego", "Phoenix", "Seattle", "Dallas");

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

        SQL_Database db = SQL_Database.getInstance();
        if (db.getEmployeeInfo(Session.getInstance().getUsername()) == null)
        {
            AsEmployeeButton.setVisible(false);
            AsEmployeeButton.setDisable(true);
        }
    }

    @FXML
    public void HandleToEmployee()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/EmployeeMainView.fxml"));
        Parent root;

        try {
            root = loader.load();
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Stage stage = new Stage();
        stage.setTitle("Main");
        stage.setScene(new Scene(root));
        B_TripModeTabPane.getScene().getWindow().hide();

        stage.show();
    }

    /*******************************************************************************************************************
     *
     *  BOOKING TAB
     *
     ******************************************************************************************************************/

    @FXML
    void B_HandlePurchase() {

        SQL_Database db = SQL_Database.getInstance();

        int mode = B_TripModeTabPane.getSelectionModel().getSelectedIndex();

        if (mode == ONE_WAY) {

            Trip selectedTrip = B_AvailableFlightsTable.getSelectionModel().getSelectedItem();
            if (selectedTrip == null) return;

            int tripID = db.getSelectedTripID(selectedTrip, selectedTrip.getFromString(), selectedTrip.getToString());

            if (tripID != -1) {

                Stage stage = new Stage();
                FXMLLoader loader = Utilities.present(stage,
                        "/Views/PaymentView.fxml",
                        "Confirm Ticket");

                PaymentViewController controller = loader.getController();
                controller.setTripID(tripID);
                controller.setSelectedMode(ONE_WAY);
                controller.hideReturnSeat();
                B_AvailableFlightsTable.getScene().getWindow().hide();
                stage.show();
            }
        }
        else if (mode == ROUND_TRIP) {

            Trip selectedTrip = B_AvailableFlightsTable.getSelectionModel().getSelectedItem();
            if (selectedTrip == null) return;

            int tripID = db.getSelectedTripID(selectedTrip, selectedTrip.getFromString(), selectedTrip.getToString());

            String from = B_RoundTripFrom.getSelectionModel().getSelectedItem();
            String to = B_RoundTripTo.getSelectionModel().getSelectedItem();

            if (tripID != -1) {

                Stage stage = new Stage();
                FXMLLoader loader = Utilities.present(stage,
                        "/Views/ReturnFlightSelectionView.fxml",
                        "Select Returning Trip");

                ReturnFlightSelectionViewController controller = loader.getController();
                controller.setLocations(to, from, tripID);
                B_AvailableFlightsTable.getScene().getWindow().hide();
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
        if (selectedIndex == ONE_WAY) {
            String from = B_OneWayFrom.getSelectionModel().getSelectedItem();
            String to = B_OneWayTo.getSelectionModel().getSelectedItem();
            LocalDate date = B_OneWayDepartDate.getValue();
            B_ErrMsg.setText(searchFlight(from, to, date));
        }
        else if (selectedIndex == ROUND_TRIP) {
            String from = B_RoundTripFrom.getSelectionModel().getSelectedItem();
            String to = B_RoundTripTo.getSelectionModel().getSelectedItem();
            LocalDate date = B_RoundTripDepartDate.getValue();
            B_ErrMsg.setText(searchFlight(from, to, date));
        }
    }

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
            results.removeIf(t -> t.getStatus() == 2);

            B_FromCol.setCellValueFactory(new PropertyValueFactory<>("FromString"));
            B_ToCol.setCellValueFactory(new PropertyValueFactory<>("ToString"));
            B_DepartDateCol.setCellValueFactory(new PropertyValueFactory<>("DateString"));
            B_PriceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));

            B_AvailableFlightsTable.setItems(results);
        }
        return null;
    }

    /*******************************************************************************************************************
     *
     *  CHECK IN TAB
     *
     ******************************************************************************************************************/

    @FXML
    void CI_HandleCheckIn() {
        SQL_Database db = SQL_Database.getInstance();
        String username = Session.getInstance().getUsername();
        int id;

        try {
            id = Integer.parseInt(CI_FlightID.getText().trim());
            if (db.checkIn(username, id) == 0) {

                Stage stage = new Stage();
                FXMLLoader loader = Utilities.present(stage,
                        "/Views/CustomerBaggageView.fxml",
                        "Declare Baggage");
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


    /*******************************************************************************************************************
     *
     *  FLIGHT STATUS TAB
     *
     ******************************************************************************************************************/

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

    /*******************************************************************************************************************
     *
     *  MY FLIGHT STATUS TAB
     *
     ******************************************************************************************************************/

    @FXML
    void MF_HandleRefresh() {
        SQL_Database db = SQL_Database.getInstance();
        ObservableList<Ticket> myFlights = FXCollections.observableArrayList(
                db.getTicketsByUsername(Session.getInstance().getUsername())
        );

        MF_FlightIDCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        MF_FromCol.setCellValueFactory(new PropertyValueFactory<>("FromString"));
        MF_ToCol.setCellValueFactory(new PropertyValueFactory<>("ToString"));
        MF_DepartDateCol.setCellValueFactory(new PropertyValueFactory<>("DepartDateString"));
        MF_StatusCol.setCellValueFactory(new PropertyValueFactory<>("FlightStatus"));
        MF_NumBagsCol.setCellValueFactory(new PropertyValueFactory<>("NumberOfBags"));
        MF_CheckedInStatusCol.setCellValueFactory(new PropertyValueFactory<>("CheckedInStatus"));
        MF_SeatNumCol.setCellValueFactory(new PropertyValueFactory<>("SeatNum"));

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


    /*******************************************************************************************************************
     *
     *  LOG OUT
     *
     ******************************************************************************************************************/

    @FXML
    void L_HandleLogout() {
        Stage stage = new Stage();
        Utilities.present(stage, "/Views/LoginView.fxml", "California System");

        B_TripModeTabPane.getScene().getWindow().hide();
        Session.getInstance().setUsername(null);
        stage.show();
    }

}
