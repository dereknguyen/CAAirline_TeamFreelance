package Controllers;

import com.jfoenix.controls.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import src.*;


/*
 * AF - Available Flights Tab
 * PS - Pricing & Scheduling Tab
 * FS - Flight Status Tab
 * B  - Booking Tab
 * MR - Management Reports
 */
public class EmployeeMainViewController {

    private static final int ONE_WAY = 0;
    private static final int ROUND_TRIP = 1;

    private ObservableList<Trip> results;

    @FXML private TableView<Trip> AF_AvailableFlightsTable;
    @FXML private TableColumn<Trip, String> AF_FlightNumberCol;
    @FXML private TableColumn<Trip, String> AF_FromCol;
    @FXML private TableColumn<Trip, String> AF_ToCol;
    @FXML private TableColumn<Trip, String> AF_DepartTimeCol;
    @FXML private TableColumn<Trip, String> AF_StatusCol;
    @FXML private TableColumn<Trip, String> AF_PriceCol;

    @FXML private JFXComboBox<String> PS_OneWayFrom;
    @FXML private JFXComboBox<String> PS_OneWayTo;
    @FXML private JFXDatePicker PS_OneWayDepartDate;
    @FXML private JFXTimePicker PS_OneWayDepartTime;
    @FXML private JFXTextField PS_OneWayBasePrice;
    @FXML private Label PS_IdInfo;
    @FXML private Label PS_FromInfo;
    @FXML private Label PS_ToInfo;
    @FXML private Label PS_DateInfo;
    @FXML private Label PS_TimeInfo;
    @FXML private Label PS_PriceInfo;
    @FXML private Label PS_ErrMsg;

    @FXML private JFXTextField FS_FlightNumber;
    @FXML private TableView<Trip> FS_StatusTable;
    @FXML private TableColumn<Trip, String> FS_FromCol;
    @FXML private TableColumn<Trip, String> FS_ToCol;
    @FXML private TableColumn<Trip, String> FS_DepartTime;
    @FXML private TableColumn<Trip, String> FS_CurrentStatus;
    @FXML private JFXComboBox<String> FS_NewStatus;
    @FXML private Label FS_ErrMsg;
    private int FS_TripId;

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

    @FXML private Label MR_ReportLabel; //from
    @FXML private Label MR_DestinationLabel;
    @FXML private Label MR_DataLabel;

    @FXML private JFXComboBox<String> MR_From;
    @FXML private JFXComboBox<String> MR_To;

    @FXML
    void AF_HandleRefresh() {

        SQL_Database db = SQL_Database.getInstance();

        ObservableList<Trip> trips = FXCollections.observableArrayList(
                db.getAllTrips()
        );

        AF_FlightNumberCol.setCellValueFactory(new PropertyValueFactory<>("TripId"));
        AF_FromCol.setCellValueFactory(new PropertyValueFactory<>("FromString"));
        AF_ToCol.setCellValueFactory(new PropertyValueFactory<>("ToString"));
        AF_DepartTimeCol.setCellValueFactory(new PropertyValueFactory<>("DateString"));
        AF_StatusCol.setCellValueFactory(new PropertyValueFactory<>("StatusString"));
        AF_PriceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));

        AF_AvailableFlightsTable.setItems(trips);
    }

    @FXML
    void MR_HandleSeat() {
        String from = MR_From.getSelectionModel().getSelectedItem();
        String to = MR_To.getSelectionModel().getSelectedItem();

        SQL_Database db = SQL_Database.getInstance();

        int id = db.getFlightId(from, to);
        MR_ReportLabel.setText(from + " Data Report");
        MR_DestinationLabel.setText("To Destination: " + to);
        MR_DataLabel.setText("Average Empty Seat Percentage: " + new BigDecimal(db.calculateAvgEmpty(to) * 100).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "%");
    }

    @FXML
    void MR_HandleRevenue() {
        String from = MR_From.getSelectionModel().getSelectedItem();
        String to = MR_To.getSelectionModel().getSelectedItem();

        SQL_Database db = SQL_Database.getInstance();

        int id = db.getFlightId(from, to);
        MR_ReportLabel.setText(from + " Data Report");
        MR_DestinationLabel.setText("To Destination: " + to);
        MR_DataLabel.setText("Revenue: $" + new BigDecimal(db.getAvgRevenue(id)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
    }

    @FXML
    void B_HandlePurchaseSelected() {

        int mode = B_TripModeTabPane.getSelectionModel().getSelectedIndex();

        if (mode == ONE_WAY) {
            // Get selected TABLE ROW in Booking Tab
            int tripID = getSelectedTripID_OneWay();

            // Present Payment View. We also want to
            //  transfer over selected data to payment view controller.
            if (tripID != 0) {
                toPaymentView_OneWay(tripID);
            }
        }
        else if (mode == ROUND_TRIP){
            // GRAB STARTING TRIP
            Trip selectedTrip = B_AvailableFlightsTable.getSelectionModel().getSelectedItem();
            if (selectedTrip == null) return;

            int tripID = SQL_Database.getInstance().getSelectedTripID(
                    selectedTrip,
                    selectedTrip.getFromString(),
                    selectedTrip.getToString()
            );

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

    @FXML
    void B_HandleSearchTickets() {
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
    void FS_HandleChangeStatus() {
        int newStatus;
        String choice = FS_NewStatus.getSelectionModel().getSelectedItem();
        if (choice == null)
        {
            FS_ErrMsg.setText("Please select a new status");
            FS_ErrMsg.setVisible(true);
            return;
        }
        switch (choice) {
            case "On-time":
                newStatus = 0;
                break;
            case "Delayed":
                newStatus = 1;
                break;
            case "Cancelled":
                newStatus = 2;
                break;
            default:
                FS_ErrMsg.setText("Please select a new status");
                FS_ErrMsg.setVisible(true);
                return;
        }
        FS_ErrMsg.setVisible(false);
        SQL_Database db = SQL_Database.getInstance();
        db.setStatus(FS_TripId, newStatus);
        FS_HandleViewStatus();
    }

    @FXML
    void FS_HandleViewStatus() {
        int TripId;
        try
        {
            TripId = Integer.parseInt(FS_FlightNumber.getText());
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

        FS_ErrMsg.setVisible(false);
        FS_FromCol.setCellValueFactory(new PropertyValueFactory<>("FromString"));
        FS_ToCol.setCellValueFactory(new PropertyValueFactory<>("ToString"));
        FS_DepartTime.setCellValueFactory(new PropertyValueFactory<>("DateString"));
        FS_CurrentStatus.setCellValueFactory(new PropertyValueFactory<>("StatusString"));

        FS_StatusTable.setItems(result);
        FS_TripId = TripId;
    }

    @FXML
    void PS_HandleSetFlight() {
        String from = PS_OneWayFrom.getSelectionModel().getSelectedItem();
        String to = PS_OneWayTo.getSelectionModel().getSelectedItem();
        LocalDate date = PS_OneWayDepartDate.getValue();
        LocalTime time = PS_OneWayDepartTime.getValue();
        String basePrice = PS_OneWayBasePrice.getText();
        PS_ErrMsg.setText(addflight(from, to, date, time, basePrice));
        AF_HandleRefresh();
    }

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
        PS_OneWayFrom.getItems().addAll("San Luis Obispo", "Los Angeles", "San Francisco",
                "San Diego", "Phoenix", "Seattle", "Dallas");
        PS_OneWayTo.getItems().addAll("San Luis Obispo", "Los Angeles", "San Francisco",
                "San Diego", "Phoenix", "Seattle", "Dallas");
        MR_From.getItems().addAll("San Luis Obispo", "Los Angeles", "San Francisco",
                "San Diego", "Phoenix", "Seattle", "Dallas");
        MR_To.getItems().addAll("San Luis Obispo", "Los Angeles", "San Francisco",
                "San Diego", "Phoenix", "Seattle", "Dallas");

        FS_NewStatus.getItems().addAll("On-time", "Delayed", "Cancelled");
        FS_TripId = -1;
    }

    @FXML
    void L_HandleLogout()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/LoginView.fxml"));
        Parent root;

        try {
            root = loader.load();
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Stage stage = new Stage();
        stage.setTitle("California System");
        stage.setScene(new Scene(root));
        B_TripModeTabPane.getScene().getWindow().hide();

        Session s = Session.getInstance();
        s.setUsername(null);
        stage.show();
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

    private int getSelectedTripID_OneWay() {
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

    private String addflight(String from, String to, LocalDate date, LocalTime time, String basePrice)
    {
        double adjprice;
        if (from == null)
        {
            return "Please specify starting location";
        }
        if (to == null)
        {
            return "Please specify destination";
        }
        if (date == null)
        {
            return "Please specify departure date";
        }
        if (time == null)
        {
            return "Please specify departure time";
        }
        if (basePrice == null)
        {
            return "Please specify base price";
        }
        try
        {
            adjprice = Double.parseDouble(basePrice);
        }
        catch (NumberFormatException e)
        {
            return "Price must be a decimal";
        }
        Calendar departDate = Calendar.getInstance();
        departDate.setTime(Date.valueOf(date));
        departDate.set(Calendar.HOUR, time.getHour());
        departDate.set(Calendar.MINUTE, time.getMinute());

        SQL_Database db = SQL_Database.getInstance();
        adjprice = adjprice - ((db.calculateAvgEmpty(to)/2)*adjprice);
        int status = db.addTrip(db.getFlightId(from, to), departDate, adjprice);
        if (status == -1)
        {
            return "Flight must not be within 40 minutes of another flight";
        }
        else if (status == -2)
        {
            return "Flight already exists";
        }
        PS_IdInfo.setText("Flight Id: " + status);
        PS_FromInfo.setText("From: " + from);
        PS_ToInfo.setText("To: " + to);
        PS_DateInfo.setText("Date: " + date);
        PS_TimeInfo.setText("Time: " + time);
        PS_PriceInfo.setText("Adjusted Price: " + adjprice);
        return null;
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
        stage.show();
    }

    @FXML TableView<Employee> EM_Table;
    @FXML TableColumn<Employee, String> EM_UsernameCol;
    @FXML TableColumn<Employee, String> EM_FirstCol;
    @FXML TableColumn<Employee, String> EM_LastCol;
    @FXML JFXTextField EM_NewFirst;
    @FXML JFXTextField EM_NewLast;
    @FXML JFXTextField EM_NewUsername;
    @FXML JFXPasswordField EM_NewPassword;

    @FXML
    void EM_HandleRemove() {

    }

    @FXML
    void EM_HandleAdd() {

    }
}
