package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import src.Trip;

public class PaymentSuccessViewController {

    private boolean isEmployee = false;

    @FXML private Label D_From;
    @FXML private Label D_To;
    @FXML private Label D_Date;
    @FXML private Label D_ID;
    @FXML private VBox ReturnFlightInfo;
    @FXML private Label R_From;
    @FXML private Label R_To;
    @FXML private Label R_Date;
    @FXML private Label R_ID;

    @FXML
    void HandleReturn() {

        ReturnFlightInfo.getScene().getWindow().hide();

        if (isEmployee) {
            Utilities.present("/Views/EmployeeMainView.fxml", "Main");
        }
        else {
            Utilities.present("/Views/CustomerMainView.fxml", "Main");
        }
    }

    @FXML
    void initialize() {
        ReturnFlightInfo.setVisible(false);
    }

    public void setInfo(Trip departTrip, Trip returnTrip) {
        D_From.setText("From: " + departTrip.getFromString());
        D_To.setText("To: " + departTrip.getToString());
        D_Date.setText("Date: " + departTrip.formatDate());
        D_ID.setText("Flight ID: " + departTrip.getTripId());

        if (returnTrip != null) {
            ReturnFlightInfo.setVisible(true);

            R_From.setText("From: " + returnTrip.getFromString());
            R_To.setText("To: " + returnTrip.getToString());
            R_Date.setText("Date: " + returnTrip.formatDate());
            R_ID.setText("Flight ID: " + returnTrip.getTripId());

        }
    }


}
