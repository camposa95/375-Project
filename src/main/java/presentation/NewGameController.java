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
import javafx.scene.text.Text;
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
    public Text title, playerSelectorLabel, GameModeSelectorLabel, languageSelectorLabel;
    private Integer numPlayers;
    private GameType gameType;
    private String language;

    @FXML
    private void initialize() {
        this.setMessages(GameLoader.getInstance().getMessageBundle());

        playersSelector.setItems(FXCollections.observableArrayList(2,3,4));
        languageSelector.setItems(FXCollections.observableArrayList("English", "Espanol"));

        startGameButton.setDisable(true);
    }

    public void setMessages(ResourceBundle messages) {

        String beginner = messages.getString("beginner");
        String advanced = messages.getString("advanced");
        gameModeSelector.setItems(FXCollections.observableArrayList(beginner, advanced));
        if (gameType != null) { // have to reset the selection since it is cleared out unfortunately by the above action
            gameModeSelector.setValue(gameType == GameType.Beginner ? beginner : advanced);
        }

        title.setText(messages.getString("newGameTitle"));
        playerSelectorLabel.setText(messages.getString("playerSelectorLabel"));
        GameModeSelectorLabel.setText(messages.getString("gameModeSelectorLabel"));
        languageSelectorLabel.setText(messages.getString("languageSelectorLabel"));
        startGameButton.setText(messages.getString("startGameButton"));
    }

    public void changeLanguage() throws IOException {
        language = this.languageSelector.getValue();
        this.setMessages(GameLoader.getInstance().setLanguage(language));

        checkForGameParameters();
    }

    public void selectPlayers() {
        Integer selection = playersSelector.getValue();
        if (selection != null) {
            numPlayers = selection;

            checkForGameParameters();
        }
    }

    public void selectGameMode() {
        String selection = gameModeSelector.getValue();
        if (selection != null) {
            gameType = translateGameType(selection);
            checkForGameParameters();
        }
    }

    private GameType translateGameType(String selection) {
        return switch (selection.toLowerCase()) {
            case "beginner", "principiante" -> GameType.Beginner;
            case "advanced", "experto" -> GameType.Advanced;
            default -> null;
        };
    }

    public void checkForGameParameters() {
        this.startGameButton.setDisable(this.numPlayers == null || this.gameType == null || this.language == null);
    }

    @FXML
    public void startGame() throws IOException {
        // set up the game to start, instantiate objects
        GameLoader loader = GameLoader.getInstance();
        Controller domainController = loader.createNewGame(gameType, numPlayers, this.language);
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
