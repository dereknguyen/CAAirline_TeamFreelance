package Controllers;

import javafx.fxml.FXML;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

public class PriceRecommendController {
    @FXML
    private JFXTextField destination;

    @FXML
    private JFXTextField basePrice;

    @FXML
    private Label recommendation;

    @FXML
    void HandlePriceClick(ActionEvent event) {
        double P = Double.parseDouble(basePrice.getText());
        String dest = destination.getText();
        src.Database db = src.SQL_Database.getInstance();

        double avgEmpty = db.calculateAvgEmpty(dest);
        Double p = P - ((avgEmpty/2)*P);
        String price = p.toString();
        recommendation.setText(price);

    }
}
