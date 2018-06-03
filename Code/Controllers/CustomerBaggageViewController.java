package Controllers;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class CustomerBaggageViewController {

    private int confirmationNum = 0;

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private JFXTextField numCarryOn;
    @FXML private JFXTextField carryOnWeight;
    @FXML private JFXTextField numCheckIn;
    @FXML private JFXTextField checkInWeight;

    @FXML
    void HandleBack(ActionEvent event) {

    }

    @FXML
    void HandleConfirmCheckIn(ActionEvent event) {

    }

    @FXML
    void initialize() {

    }

    public void setConfirmationNum(int confirmationNum) {
        this.confirmationNum = confirmationNum;
    }
}
