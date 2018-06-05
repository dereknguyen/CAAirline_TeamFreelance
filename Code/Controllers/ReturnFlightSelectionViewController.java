package Controllers;

import com.jfoenix.controls.JFXDatePicker;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import src.Database;
import src.SQL_Database;
import src.Trip;

public class ReturnFlightSelectionViewController {

    private String from;
    private String to;
    private int startingTripID;
    private static final int ONE_WAY = 0;
    private static final int ROUND_TRIP = 1;

    @FXML private JFXDatePicker ReturnFlightDate;
    @FXML private Label ErrMsg;
    @FXML private TableView<Trip> AvailableFlightsTable;
    @FXML private TableColumn<Trip, String> FromCol;
    @FXML private TableColumn<Trip, String> ToCol;
    @FXML private TableColumn<Trip, String> DepartDateCol;
    @FXML private TableColumn<Trip, String> ReturnDateCol;
    @FXML private TableColumn<Trip, String> PriceCol;

    public void setLocations(String from, String to, int startingTripID) {
        this.from = from;
        this.to = to;
        this.startingTripID = startingTripID;
    }

    @FXML
    void HandleBack() {
        ErrMsg.getScene().getWindow().hide();
    }

    @FXML
    void HandlePurchase() {
        SQL_Database db = SQL_Database.getInstance();
        Trip selectedTrip = AvailableFlightsTable.getSelectionModel().getSelectedItem();

        if (selectedTrip != null) {
            int flightID = db.getFlightId(from, to);

            Calendar date = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM HH:mm:ss z yyyy", Locale.ENGLISH);

            try {
                date.setTime(sdf.parse(selectedTrip.getDateString()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            int tripID = db.getTripId(flightID, date);

            if (tripID != -1) {
                toPaymentView(startingTripID, tripID);
            }
        }
    }

    @FXML
    void HandleSearch() {
        LocalDate date = ReturnFlightDate.getValue();
        ErrMsg.setText(searchFlight(this.from, this.to, date));
    }

    private String searchFlight(String from, String to, LocalDate localDate) {

        if (from == null) {
            return "Please specify location you are from.";
        }
        else if (to == null) {
            return "Please specify location are are going to.";
        }
        else if (localDate == null) {
            return "Please specify departure date.";
        }
        else {
            Calendar departDate = Calendar.getInstance();
            departDate.setTime(Date.valueOf(localDate));

            Database db = SQL_Database.getInstance();
            ObservableList<Trip> results = FXCollections.observableArrayList(
                    db.getTripsByFlightAndDate(db.getFlightId(from, to), departDate)
            );

            FromCol.setCellValueFactory(new PropertyValueFactory<>("FromString"));
            ToCol.setCellValueFactory(new PropertyValueFactory<>("ToString"));
            DepartDateCol.setCellValueFactory(new PropertyValueFactory<>("DateString"));
            ReturnDateCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper("N/A"));
            PriceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));

            AvailableFlightsTable.setItems(results);
        }
        return null;
    }

    private void toPaymentView(int startingTripID, int returnTripID) {
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
        controller.setTripID(startingTripID, returnTripID);
        controller.setSelectedMode(ROUND_TRIP);
        controller.loadReturnSeat();
        stage.setTitle("Confirm Ticket");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
