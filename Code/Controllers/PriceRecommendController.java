package Controllers;

import javafx.fxml.FXML;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

public class PriceRecommendController {
    @FXML
    private TextField destination;

    @FXML
    private TextField basePrice;

    @FXML
    private Label recommendation;

    @FXML
    void HandlePriceClick(ActionEvent event) {
        double P = Double.parseDouble(basePrice.getText());
        String dest = destination.getText();
        src.Database db = src.SQL_Database.getInstance();

        double avgEmpty = db.calculateAvgEmpty(dest);
        if (avgEmpty == -1) {
            //not enough flights in database
            avgEmpty = 0;
        }
        Double p = P - ((avgEmpty/2)*P);
        String price = p.toString();
        recommendation.setText(price);
    }
}
