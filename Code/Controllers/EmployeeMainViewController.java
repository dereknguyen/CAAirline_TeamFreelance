package Controllers;

import com.jfoenix.controls.*;

import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private TabPane mainTabPane;
    @FXML private TableView<Ticket> AF_AvailableFlightsTable;
    @FXML private TableColumn<?, ?> AF_FlightNumberCol;
    @FXML private TableColumn<?, ?> AF_FromCol;
    @FXML private TableColumn<?, ?> AF_ToCol;
    @FXML private TableColumn<?, ?> AF_DepartTimeCol;
    @FXML private TableColumn<Ticket, String> AF_NumBagsCol;
    @FXML private TableColumn<Ticket, Boolean> AF_CheckedInStatusCol;
    @FXML private TableColumn<?, ?> AF_StatusCol;

    @FXML private TabPane PS_TripModeTabPane;
    @FXML private JFXComboBox<String> PS_OneWayFrom;
    @FXML private JFXComboBox<String> PS_OneWayTo;
    @FXML private JFXDatePicker PS_OneWayDepartDate;
    @FXML private JFXTimePicker PS_OneWayDepartTime;
    @FXML private JFXTextField PS_OneWayBasePrice;
    @FXML private Label PS_ErrMsg;

    @FXML private JFXTextField FS_FlightNumber;
    @FXML private TableView<Trip> FS_StatusTable;
    @FXML private TableColumn<Trip, String> FS_FromCol;
    @FXML private TableColumn<Trip, String> FS_ToCol;
    @FXML private TableColumn<Trip, String> FS_DepartTime;
    @FXML private TableColumn<Trip, String> FS_ArrivalTime;
    @FXML private TableColumn<Trip, String> FS_CurrentStatus;
    //TODO Add FS error message to fxml
    @FXML private Label FS_ErrMsg;
    //TODO Add new status for changing status
    @FXML private JFXTextField FS_NewStatus;

    @FXML private TabPane B_TripModeTabPane;
    @FXML private JFXComboBox<String> B_OneWayFrom;
    @FXML private JFXComboBox<String> B_OneWayTo;
    @FXML private JFXDatePicker B_OneWayDepartDate;
    @FXML private Label B_ErrMsg;
    @FXML private TableView<Trip> B_AvailableFlightsTable;
    @FXML private TableColumn<Trip, String> B_FromCol;
    @FXML private TableColumn<Trip, String> B_ToCol;
    @FXML private TableColumn<Trip, String> B_DepartTimeCol;
    @FXML private TableColumn<Trip, String> B_ArrivalTimeCol;
    @FXML private TableColumn<Trip, String> B_PriceCol;

    @FXML private TableView<Report> MR_ReportTable;
    @FXML private Label MR_ReportLabel; //from
    @FXML private TableColumn<Report, String> MR_DestinationCol;
    @FXML private TableColumn<Report, String> MR_DataCol;

    @FXML private JFXComboBox<String> MR_From;
    @FXML private JFXComboBox<String> MR_To;

    @FXML
    void AF_HandleRefresh() {
        SQL_Database db = SQL_Database.getInstance();
        ObservableList<Ticket> myFlights = FXCollections.observableArrayList(
                db.getTicketsByUsername(CustomerControl.getInstance().getCustomer().getUserName())
        );

        AF_FlightNumberCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        AF_FromCol.setCellValueFactory(new PropertyValueFactory<>("FromString"));
        AF_ToCol.setCellValueFactory(new PropertyValueFactory<>("ToString"));
        AF_DepartTimeCol.setCellValueFactory(new PropertyValueFactory<>("DepartDateString"));
        AF_StatusCol.setCellValueFactory(new PropertyValueFactory<>("FlightStatus"));
        AF_NumBagsCol.setCellValueFactory(new PropertyValueFactory<>("NumberOfBags"));
        AF_CheckedInStatusCol.setCellValueFactory(new PropertyValueFactory<>("CheckedInStatus"));

        AF_AvailableFlightsTable.setItems(myFlights);
    }

    @FXML
    void MR_HandleSeat() {
        String from = MR_From.getSelectionModel().getSelectedItem();
        String to = MR_To.getSelectionModel().getSelectedItem();

        Database db = SQL_Database.getInstance();

        int id = db.getFlightId(from, to);
        double avg = ((SQL_Database) db).getAvgSeats(id);
        MR_ReportLabel.setText(from);

        Report data = new Report(from, to, new Double(avg).toString());
        ObservableList<Report> results = FXCollections.observableArrayList(data);

        MR_DataCol.setCellValueFactory(new PropertyValueFactory<>("DataString"));
        MR_DestinationCol.setCellValueFactory(new PropertyValueFactory<>("ToString"));

        MR_ReportTable.setItems(results);
    }

    @FXML
    void MR_HandleRevenue() {
        String from = MR_From.getSelectionModel().getSelectedItem();
        String to = MR_To.getSelectionModel().getSelectedItem();

        Database db = SQL_Database.getInstance();

        int id = db.getFlightId(from, to);
        double avg = ((SQL_Database) db).getAvgRevenue(id);
        MR_ReportLabel.setText(from);

        Report data = new Report(from, to, new Double(avg).toString());
        ObservableList<Report> results = FXCollections.observableArrayList(data);

        MR_DataCol.setCellValueFactory(new PropertyValueFactory<>("DataString"));
        MR_DestinationCol.setCellValueFactory(new PropertyValueFactory<>("ToString"));

        MR_ReportTable.setItems(results);
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
            // TODO: Round Trip Purchase
        }
    }

    @FXML
    void B_HandleSearchTickets() {
        B_ErrMsg.setText(searchOneWay());
    }

    @FXML
    void FS_HandleChangeStatus() {
        int TripId;
        int newStatus;
        try {
            TripId = Integer.parseInt(FS_FlightNumber.getText());
            newStatus = Integer.parseInt(FS_NewStatus.getText());
            if(TripId < 0 || newStatus < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            FS_ErrMsg.setText("Please enter a valid flight ID");
            FS_ErrMsg.setVisible(true);
            return;
        }
        SQL_Database db = SQL_Database.getInstance();
        db.setStatus(TripId, newStatus);
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
        if (t == null) return;

        FS_ErrMsg.setVisible(false);
        FS_FromCol.setCellValueFactory(new PropertyValueFactory<>("FromString"));
        FS_ToCol.setCellValueFactory(new PropertyValueFactory<>("ToString"));
        FS_DepartTime.setCellValueFactory(new PropertyValueFactory<>("DateString"));
        if (t.getRTDate() == null)
        {
            FS_ArrivalTime.setCellValueFactory(cellData -> new ReadOnlyStringWrapper("N/A"));
        }
        else
        {
            //todo this will never run
            FS_ArrivalTime.setCellValueFactory(new PropertyValueFactory<>("RTDateString"));
        }
        FS_CurrentStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));

        FS_StatusTable.setItems(result);
    }


    // TODO Set flight function
    @FXML
    void PS_HandleSetFlight() {

        Calendar c = Calendar.getInstance();

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
        PS_OneWayFrom.getItems().addAll("San Luis Obispo", "Los Angeles", "San Francisco",
                "San Diego", "Phoenix", "Seattle", "Dallas");
        PS_OneWayTo.getItems().addAll("San Luis Obispo", "Los Angeles", "San Francisco",
                "San Diego", "Phoenix", "Seattle", "Dallas");
        MR_From.getItems().addAll("San Luis Obispo", "Los Angeles", "San Francisco",
                "San Diego", "Phoenix", "Seattle", "Dallas");
        MR_To.getItems().addAll("San Luis Obispo", "Los Angeles", "San Francisco",
                "San Diego", "Phoenix", "Seattle", "Dallas");
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
            B_DepartTimeCol.setCellValueFactory(new PropertyValueFactory<>("DateString"));
            B_ArrivalTimeCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper("N/A"));
            B_PriceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));

            B_AvailableFlightsTable.setItems(results);
        }
        return null;
    }

    /*private String searchRoundTrip() {
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
            B_DepartTimeCol.setCellValueFactory(new PropertyValueFactory<>("DateString"));
            B_ArrivalTimeCol.setCellValueFactory(new PropertyValueFactory<>("RTDateString"));
            B_PriceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));

            B_AvailableFlightsTable.setItems(results);
        }

        return null;
    }*/

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

    private void toPaymentView_OneWay(int tripID) {
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
            return "Flight could not be added";
        }
        return null;
    }
}
