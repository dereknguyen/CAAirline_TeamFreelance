package src;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Calendar;
import java.util.Random;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../Views/LoginView.fxml"));
        primaryStage.setTitle("California System");
        primaryStage.setScene( new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) { launch(args);}

    private static void mainLoop() {
        /* generate ticket database entries */
        SQL_Database db = SQL_Database.getInstance();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.WEEK_OF_MONTH, -4);
        c.set(Calendar.MINUTE, 0);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 100; j++) {
                db.addTrip(i, c, (Math.floor(Math.random() * 91) + 10));
                c.add(Calendar.DAY_OF_WEEK, 1);
            }
        }

        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 20; j++) {
                if (r.nextInt(4) == 0) {
                    db.addTicket("test", i, j);
                }
            }
        }

    }
}
