package Controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.text.Format;
import java.text.SimpleDateFormat;
import javafx.beans.property.ReadOnlyStringWrapper;


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

    @FXML private TableColumn<Integer, String> fromCol;
    @FXML private TableColumn<Integer, String> toCol;
    @FXML private TableColumn<Integer, String> departTimeCol;
    @FXML private TableColumn<Integer, String> arrivalTimeCol;
    @FXML private TableColumn<Integer, String> priceCol;

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
            java.util.Date departDate = Date.valueOf(localD);

            presentTableOneWay(departDate, oneWayFrom.getValue(), oneWayTo.getValue());
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
            java.util.Date departDate = Date.valueOf(departLocal);
            java.util.Date returnDate = Date.valueOf(returnLocal);

            presentTableRoundTrip(departDate, returnDate, roundTripFrom.getValue(), roundTripTo.getValue());
        }

        return null;
    }

    private void presentTableRoundTrip(java.util.Date depart, java.util.Date arrive, String from, String to) {
        Calendar departCal = Calendar.getInstance();
        departCal.setTime(depart);

        src.Database db = SQL_Database.getInstance();
        ArrayList<src.Trip> allFlights = db.getAllTrips();
        ObservableList <src.Trip> departures = FXCollections.observableArrayList();
        int flightID = db.getFlightId(from,to);
        for(int i = 0; i < allFlights.size(); i++){
            if(allFlights.get(i).getFlightId() == flightID && sameDay(allFlights.get(i), departCal)){
                departures.add(allFlights.get(i));
            }
        }

        Calendar arriveCal = Calendar.getInstance();
        arriveCal.setTime(arrive);

        ObservableList <src.Trip> arrivals = FXCollections.observableArrayList();
        flightID = db.getFlightId(to, from);
        for(int i = 0; i < allFlights.size(); i++){
            if(allFlights.get(i).getFlightId() == flightID && sameDay(allFlights.get(i), arriveCal)){
                arrivals.add(allFlights.get(i));
            }
        }

        fromCol.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(from);
        });
        toCol.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(to);
        });
        departTimeCol.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            Calendar date = departures.get(rowIndex).getDate();
            Format formatter = new SimpleDateFormat("E, dd MMM HH:mm:ss");
            String s = formatter.format(date);
            return new ReadOnlyStringWrapper(s);
        });
        arrivalTimeCol.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            Calendar date = arrivals.get(rowIndex).getDate();
            Format formatter = new SimpleDateFormat("E, dd MMM HH:mm:ss");
            String s = formatter.format(date);
            return new ReadOnlyStringWrapper(s);
        });
        priceCol.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            double price1 = departures.get(rowIndex).getPrice();
            double price2 = arrivals.get(rowIndex).getPrice();
            Double price = price1 + price2;
            String p = "$"+price.toString();
            return new ReadOnlyStringWrapper(p);
        });


    }

    private void presentTableOneWay(java.util.Date departDate, String from, String to){
        Calendar departCal = Calendar.getInstance();
        departCal.setTime(departDate);

        // Pull from database
        src.Database db = SQL_Database.getInstance();
        ArrayList<src.Trip> allFlights = db.getAllTrips();
        ObservableList <src.Trip> data = FXCollections.observableArrayList();
        int flightID = db.getFlightId(from,to);
        int tripID = db.getTripId(flightID, departCal);
        for(int i = 0; i < allFlights.size(); i++){
            if(allFlights.get(i).getFlightId() == flightID && sameDay(allFlights.get(i), departCal)){
                data.add(allFlights.get(i));
            }
        }
        fromCol.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(from);
        });
        toCol.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(to);
        });
        departTimeCol.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            Calendar date = data.get(rowIndex).getDate();
            Format formatter = new SimpleDateFormat("E, dd MMM HH:mm:ss");
            String s = formatter.format(date);
            return new ReadOnlyStringWrapper(s);
        });
        // TODO ARRIVAL TIME: Is this needed for one way??
        priceCol.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            Double price = data.get(rowIndex).getPrice();
            String p = "$"+price.toString();
            return new ReadOnlyStringWrapper(p);
        });

    }

    private boolean sameDay(src.Trip trip, Calendar date) {
        if(trip.getDate().get(Calendar.YEAR) == date.get(Calendar.YEAR) && trip.getDate().get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR))
            return true;
        else
            return false;

    }


}
