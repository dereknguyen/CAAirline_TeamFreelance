package Controllers;

import com.jfoenix.controls.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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

        MR_ReportLabel.setText(from + " Data Report");
        MR_DestinationLabel.setText("To Destination: " + to);
        MR_DataLabel.setText("Average Empty Seat Percentage: " + new BigDecimal(db.calculateAvgEmpty(to)
                * 100).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "%");
    }

    @FXML
    void MR_HandleRevenue() {
        String from = MR_From.getSelectionModel().getSelectedItem();
        String to = MR_To.getSelectionModel().getSelectedItem();

        SQL_Database db = SQL_Database.getInstance();

        int id = db.getFlightId(from, to);
        MR_ReportLabel.setText(from + " Data Report");
        MR_DestinationLabel.setText("To Destination: " + to);
        MR_DataLabel.setText("Revenue: $" + new BigDecimal(db.getAvgRevenue(id)).setScale(2,
                BigDecimal.ROUND_HALF_UP).toString());
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

        refreshEmployeeList();
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
        AF_AvailableFlightsTable.getScene().getWindow().hide();

        Session s = Session.getInstance();
        s.setUsername(null);
        stage.show();
    }

    @FXML
    private void HandleToCustomer()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/CustomerMainView.fxml"));
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
        AF_AvailableFlightsTable.getScene().getWindow().hide();

        stage.show();
    }

    /* HELPERS */
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

    @FXML TableView<Employee> EM_Table;
    @FXML TableColumn<Employee, String> EM_UsernameCol;
    @FXML TableColumn<Employee, String> EM_FirstCol;
    @FXML TableColumn<Employee, String> EM_LastCol;
    @FXML JFXTextField EM_NewFirst;
    @FXML JFXTextField EM_NewLast;
    @FXML JFXTextField EM_NewUsername;
    @FXML JFXPasswordField EM_NewPassword;
    @FXML Label EM_ErrMsg;
    @FXML Label EM_Msg;

    @FXML
    void EM_HandleRemove() {
        Employee selectedEmployee = EM_Table.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) return;

        if (SQL_Database.getInstance().removeEmployee(selectedEmployee.getUsername()) == 0) {
            refreshEmployeeList();
        }
        else {
            EM_ErrMsg.setText("Unable to remove employee");
            EM_ErrMsg.setVisible(true);
        }
    }

    @FXML
    void EM_HandleAdd() {
        String username = EM_NewUsername.getText().trim();
        String password = src.MD5Password.encodePassword(EM_NewPassword.getText().trim());
        String firstName = EM_NewFirst.getText().trim();
        String lastName = EM_NewLast.getText().trim();

        if (username.length() == 0) {
            EM_ErrMsg.setText("Please enter a username");
            EM_Msg.setVisible(false);
        }
        else if (firstName.length() == 0) {
            EM_ErrMsg.setText("Please enter a first name");
            EM_Msg.setVisible(false);
        }
        else if (lastName.length() == 0) {
            EM_ErrMsg.setText("Please enter a last name");
            EM_Msg.setVisible(false);

        }
        else if (password == null) {
            EM_ErrMsg.setText("Please enter a password");
            EM_Msg.setVisible(false);
        }
        else {
            int result = SQL_Database.getInstance().addEmployeeAccount(username, password, firstName, lastName);

            if (result == -1) {
                EM_ErrMsg.setText("Add new employee Error");
                EM_Msg.setVisible(false);
            }
            else {
                refreshEmployeeList();
                EM_NewUsername.clear();
                EM_NewFirst.clear();
                EM_NewLast.clear();
                EM_NewPassword.clear();
                EM_ErrMsg.setVisible(false);

                EM_Msg.setText("Welcome to the family, " + firstName + " " + lastName + "!");
            }
        }
    }

    private void refreshEmployeeList() {

        ArrayList<Employee> all = SQL_Database.getInstance().getAllEmployees(Session.getInstance().getUsername());
        ObservableList<Employee> allEmployees = FXCollections.observableArrayList(all);

        EM_UsernameCol.setCellValueFactory(new PropertyValueFactory<>("UsernameString"));
        EM_FirstCol.setCellValueFactory(new PropertyValueFactory<>("FirstNameString"));
        EM_LastCol.setCellValueFactory(new PropertyValueFactory<>("LastNameString"));
        EM_Table.setItems(allEmployees);
    }


}
