package presentation;

import data.GameLoader;
import domain.controller.Controller;
import domain.game.GameType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class NewGameController {
    @FXML
    public ChoiceBox<Integer> playersSelector;
    @FXML
    public ChoiceBox<String> gameModeSelector;
    @FXML
    public ChoiceBox<String> languageSelector;

    @FXML
    public Button startGameButton;

    @FXML
    private void initialize() {
        gameModeSelector.setItems(FXCollections.observableArrayList("Beginner (Principiante)", "Advanced (Experto)"));
        playersSelector.setItems(FXCollections.observableArrayList(2,3,4));
        languageSelector.setItems(FXCollections.observableArrayList("English", "Espanol"));
    }

    @FXML
    public void startGame() throws IOException {
        if(playersSelector.getValue() == null || gameModeSelector.getValue() == null || languageSelector.getValue() == null) {
            return;
        }

        // get the player count from input
        int playerCount = Integer.parseInt(playersSelector.getValue().toString());

        // get the gameType from input
        String input = gameModeSelector.getValue();
        String result = input.substring(0, input.indexOf(' ')); // Extract the substring before the space
        GameType gameType = GameType.valueOf(result);

        // get the language from input
        String language = languageSelector.getValue();

        // set up the game to start, instantiate objects
        GameLoader loader = GameLoader.getInstance();
        Controller domainController = loader.createNewGame(gameType, playerCount, language);
        ResourceBundle messages = loader.getMessageBundle();

        //close Start Screen window
        Stage stage = (Stage) startGameButton.getScene().getWindow();
        stage.close();

        //Open up game board window
        FXMLLoader fxmlLoader = new FXMLLoader(Catan.class.getResource("gameboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(messages.getString("catanTitle"));
        stage.setScene(scene);
        stage.show();

        // Set up the  Gui Controller
        CatanGUIController guiController = fxmlLoader.getController();
        guiController.setController(domainController);

        // initialize the game-board
        guiController.internationalize(messages);
        guiController.initializeGameBoard();
    }
}
