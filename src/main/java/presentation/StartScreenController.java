package presentation;

import domain.controller.Controller;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import data.GameLoader;

import java.io.IOException;
import java.util.ResourceBundle;

public class StartScreenController {

    @FXML
    public Button createNewGameButton;

    @FXML
    public void resumeGame() throws IOException {
        GameLoader loader = GameLoader.getInstance();
        Controller domainController = loader.loadGame();
        ResourceBundle messages = loader.getMessageBundle();

        //close Start Screen window
        Stage stage = (Stage) createNewGameButton.getScene().getWindow();
        stage.close();

        //Open up game board window
        FXMLLoader fxmlLoader = new FXMLLoader(Catan.class.getResource("gameboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(messages.getString("catanTitle"));
        stage.setScene(scene);
        stage.show();

        // Link the Gui Controller to the Domain Controller
        CatanGUIController guiController = fxmlLoader.getController();
        guiController.setController(domainController);

        // initialize the game-board
        guiController.initializeGameBoard();
        guiController.internationalize(messages);
    }

    @FXML
    public void createNewGame() throws IOException {
        //close Start Screen window
        Stage stage = (Stage) createNewGameButton.getScene().getWindow();
        stage.close();

        //Open up game board window
        FXMLLoader fxmlLoader = new FXMLLoader(Catan.class.getResource("new_game.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void exit() {
        // Close the main application window
        Platform.exit();
    }
}
