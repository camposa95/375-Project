package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Catan extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Catan.class.getResource("start-screen.fxml"));
        //FXMLLoader fxmlLoader = new FXMLLoader(Catan.class.getResource("playertrade_window.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Welcome to gui.Catan!");
        stage.setScene(scene);
        stage.show();
    }

    public static void startGame() {
        launch();
    }
}
