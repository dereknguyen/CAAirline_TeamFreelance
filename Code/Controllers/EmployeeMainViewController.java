package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


/*
 * AF - Available Flights Tab
 * PS - Pricing & Scheduling Tab
 * FS - Flight Status Tab
 * B  - Booking Tab
 */
public class EmployeeMainViewController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private TabPane mainTabPane;

    @FXML private TableColumn<?, ?> AF_FlightNumberCol;
    @FXML private TableColumn<?, ?> AF_FromCol;
    @FXML private TableColumn<?, ?> AF_ToCol;
    @FXML private TableColumn<?, ?> AF_DepartTimeCol;
    @FXML private TableColumn<?, ?> AF_ArivalTimeCol;
    @FXML private TableColumn<?, ?> AF_PriceCol;
    @FXML private TableColumn<?, ?> AF_StatusCol;

    @FXML private TabPane PS_TripModeTabPane;
    @FXML private JFXComboBox<?> PS_OneWayFrom;
    @FXML private JFXComboBox<?> PS_OneWayTo;
    @FXML private JFXDatePicker PS_OneWayDepartDate;
    @FXML private JFXTextField PS_OneWayBasePrice;
    @FXML private JFXComboBox<?> PS_RoundTripFrom;
    @FXML private JFXComboBox<?> PS_RoundTripTo;
    @FXML private JFXDatePicker PS_RoundTripDepartDate;
    @FXML private JFXDatePicker PS_RoundTripReturnDate;
    @FXML private JFXTextField PS_RoundTripBasePrice;
    @FXML private Label PS_ErrMsg;

    @FXML private JFXTextField FS_FlightNumber;
    @FXML private JFXButton FS_StatusViewButton;
    @FXML private TableView<?> FS_StatusTable;
    @FXML private TableColumn<?, ?> FS_FromCol;
    @FXML private TableColumn<?, ?> FS_ToCol;
    @FXML private TableColumn<?, ?> FS_DepartTime;
    @FXML private TableColumn<?, ?> FS_ArrivalTime;
    @FXML private TableColumn<?, ?> FS_CurrentStatus;

    @FXML private TabPane B_TripModeTabPane;
    @FXML private JFXComboBox<?> B_OneWayFrom;
    @FXML private JFXComboBox<?> B_OneWayTo;
    @FXML private JFXDatePicker B_OneWayDepartDate;
    @FXML private JFXComboBox<?> B_RoundTripFrom;
    @FXML private JFXComboBox<?> B_RoundTripTo;
    @FXML private JFXDatePicker B_RoundTripDepartDate;
    @FXML private JFXDatePicker B_RoundTripReturnDate;
    @FXML private Label B_ErrMsg;
    @FXML private TableColumn<?, ?> B_FromCol;
    @FXML private TableColumn<?, ?> B_ToCol;
    @FXML private TableColumn<?, ?> B_DepartTimeCol;
    @FXML private TableColumn<?, ?> B_ArrivalTimeCol;
    @FXML private TableColumn<?, ?> B_PriceCol;

    @FXML
    void AF_HandleRefresh(ActionEvent event) {

    }

    @FXML
    void B_HandlePurchaseSelected(ActionEvent event) {
    }

    @FXML
    void B_HandleSearchTickets(ActionEvent event) {

    }

    @FXML
    void FS_HandleChangeStatus(ActionEvent event) {

    }

    @FXML
    void FS_HandleViewStatus(ActionEvent event) {

    }

    @FXML
    void PS_HandleSetFlight(ActionEvent event) {

    }

    @FXML
    void initialize() {


    }
}
