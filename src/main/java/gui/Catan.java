package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import saving.GameLoader;

import java.io.IOException;

public class Catan extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String pageToStartAt = "";
        if (GameLoader.getInstance().hasSavedSlot()) {
            pageToStartAt = "start_screen.fxml";
        } else {
            pageToStartAt = "new_game.fxml";
        }

        FXMLLoader fxmlLoader = new FXMLLoader(Catan.class.getResource(pageToStartAt));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Welcome to Catan!");
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public static void startGame() {
        launch();
    }
}
