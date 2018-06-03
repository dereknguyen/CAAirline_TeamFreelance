package Controllers;

import java.sql.Date;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import src.Database;
import src.SQL_Database;

public class CustomerMainViewController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private TabPane mainTabPane;
    @FXML private Tab bookingTab;
    @FXML private Tab checkInTab;

    @FXML private TabPane tripOptionsTabPane;
    @FXML private Tab oneWayTripTab;
    @FXML private Tab roundTripTab;
    @FXML private Tab flightStatusTab;

    @FXML private JFXComboBox<String> oneWayFrom;
    @FXML private JFXComboBox<String> oneWayTo;
    @FXML private JFXDatePicker oneWayDepartDate;

    @FXML private JFXComboBox<String> roundTripFrom;
    @FXML private JFXComboBox<String> roundTripTo;
    @FXML private JFXDatePicker roundTripDepartDate;
    @FXML private JFXDatePicker roundTripReturnDate;

    @FXML private JFXButton searchTicketButton;
    @FXML private JFXButton purchaseButton;

    @FXML private TableColumn<?, ?> fromCol;
    @FXML private TableColumn<?, ?> toCol;
    @FXML private TableColumn<?, ?> departTimeCol;
    @FXML private TableColumn<?, ?> arrivalTimeCol;
    @FXML private TableColumn<?, ?> priceCol;

    @FXML private Label searchErrMsg;

    @FXML private JFXTextField checkinFlightNumber;
    @FXML private JFXButton checkinButton;

    @FXML private JFXTextField statusFlightNumber;
    @FXML private JFXButton statusViewButton;
    @FXML private TableView<?> statusTable;

    @FXML
    void checkIn(ActionEvent event) {

    }

    @FXML
    void purchaseSelected(ActionEvent event) {

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
    void searchForTickets() {
        int selectedIndex = tripOptionsTabPane.getSelectionModel().getSelectedIndex();

        if (selectedIndex == 0) {
            searchErrMsg.setText(searchOneWay());
        }
        else if (selectedIndex == 1) {
            searchErrMsg.setText(searchRoundTrip());
        }
    }

    @FXML
    void viewFlightStatus(ActionEvent event) {

    }

    @FXML
    void initialize() {
        oneWayFrom.getItems().addAll("Los Angeles", "San Francisco", "San Diego", "Arizona", "Seattle", "Dallas");
        oneWayTo.getItems().addAll("Los Angeles", "San Francisco", "San Diego", "Arizona", "Seattle", "Dallas");
        roundTripFrom.getItems().addAll("Los Angeles", "San Francisco", "San Diego", "Arizona", "Seattle", "Dallas");
        roundTripTo.getItems().addAll("Los Angeles", "San Francisco", "San Diego", "Arizona", "Seattle", "Dallas");
    }

    private String searchOneWay() {
        System.out.println("\nSearching: One Way");

        String from = oneWayFrom.getSelectionModel().getSelectedItem();
        String to = oneWayTo.getSelectionModel().getSelectedItem();
        LocalDate localD = oneWayDepartDate.getValue();

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

        String from = roundTripFrom.getSelectionModel().getSelectedItem();
        String to = roundTripTo.getSelectionModel().getSelectedItem();

        LocalDate departLocal = roundTripDepartDate.getValue();
        LocalDate returnLocal = roundTripReturnDate.getValue();

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
