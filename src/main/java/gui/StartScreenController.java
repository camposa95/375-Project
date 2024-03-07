package gui;

import gamedatastructures.GameType;
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

public class StartScreenController {
    @FXML
    public ChoiceBox playersSelector, gamemodeSelector, languageSelector;

    @FXML
    public Button startGameButton;

    @FXML
    private void initialize(){
        gamemodeSelector.setItems(FXCollections.observableArrayList("Basic (Principiante)", "Advanced (Experto)"));
        playersSelector.setItems(FXCollections.observableArrayList(2,3,4));
        languageSelector.setItems(FXCollections.observableArrayList("English", "Espanol"));
    }

    @FXML
    public void startGame() throws IOException {
        if(playersSelector.getValue()==null || gamemodeSelector.getValue() == null || languageSelector.getValue() == null){
            return;
        }

        //call stuff to instantiate the backend objects here
        String numPlayers = playersSelector.getValue().toString();
        String gameMode = gamemodeSelector.getValue().toString();
        String language = languageSelector.getValue().toString();

        ResourceBundle messages;
        if(language.equals("English")){
            messages = ResourceBundle.getBundle("i18n/messages");
        }else if(language.equals("Espanol")){
            messages = ResourceBundle.getBundle("i18n/messages_es");
        }else{
            return;
        }

        GameType gameType = null;
        if(gameMode.equals("Basic (Principiante)")){
            gameType = GameType.Beginner;
        }else if (gameMode.equals("Advanced (Experto)")){
            gameType = GameType.Advanced;
        }

        //close Start Screen window
        Stage stage = (Stage) startGameButton.getScene().getWindow();
        stage.close();

        //Open up game board window
        FXMLLoader fxmlLoader = new FXMLLoader(Catan.class.getResource("gameboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(messages.getString("catanTitle"));
        stage.setScene(scene);
        stage.show();

        CatanGUIController guiController = fxmlLoader.getController();
        guiController.setData(gameType, Integer.parseInt(numPlayers));
        guiController.initializeGameBoard();
        if(gameType==GameType.Beginner){
            guiController.initializeSetupBoard();
        }
        guiController.internationalize(messages);
    }
}
