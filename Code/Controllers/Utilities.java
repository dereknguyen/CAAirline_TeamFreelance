package Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Utilities {

    /**
     * Present FXML scene as a new root stage.
     *
     * @param resource Relative path to FXML file
     * @param stageTitle Title for stage. (Set according scene you want to present)
     */
    static void present(String resource, String stageTitle) {
        try {
            Parent mainTabRoot = FXMLLoader.load(Utilities.class.getResource(resource));
            Stage stage = new Stage();
            stage.setTitle(stageTitle);
            stage.setScene(new Scene(mainTabRoot));
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    static FXMLLoader present(Stage stage, String resource, String stageTitle) {
        FXMLLoader loader = new FXMLLoader(Utilities.class.getResource(resource));
        Parent root;

        try {
            root = loader.load();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        stage.setTitle(stageTitle);
        stage.setScene(new Scene(root));

        return loader;
    }
}
