package presentation;

import domain.controller.Controller;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import data.GameLoader;

import java.io.IOException;
import java.util.ResourceBundle;

public class StartScreenController {

    public Text title, header;
    public Button slot1, slot2, slot3, slot4;
    @FXML
    public Button createNewGameButton, resumeGameButton, deleteGameButton, changeGameImagesButton;
    public Text selectedSlotText;
    public Button exitButton;
    private ResourceBundle messages;

    @FXML
    public void initialize() {
        this.setMessages(GameLoader.getInstance().getMessageBundle());

        createNewGameButton.setDisable(true);
        resumeGameButton.setDisable(true);
    }

    public void setMessages(ResourceBundle messages) {
        this.messages = messages;

        title.setText(messages.getString("startScreenTitle"));
        header.setText(messages.getString("startScreenHeader"));

        GameLoader loader = GameLoader.getInstance();
        String empty = messages.getString("emptySlot");
        String slot = messages.getString("usedSlot");
        slot1.setText(loader.isSlotEmpty(1) ? empty : slot + " " + 1);
        slot2.setText(loader.isSlotEmpty(2) ? empty : slot + " " + 2);
        slot3.setText(loader.isSlotEmpty(3) ? empty : slot + " " + 3);
        slot4.setText(loader.isSlotEmpty(4) ? empty : slot + " " + 4);

        resumeGameButton.setText(messages.getString("resumeGameButton"));
        createNewGameButton.setText(messages.getString("createNewGameButton"));
        deleteGameButton.setText(messages.getString("deleteGameButton"));
        exitButton.setText(messages.getString("exitButton"));

        selectedSlotText.setText(messages.getString("noSlotSelected"));
    }

    @FXML
    public void selectSlot(ActionEvent e) {
        String slot = ((Button) e.getSource()).getId();
        int slotNumber = Integer.parseInt(slot.substring(4));
        selectedSlotText.setText(messages.getString("selectedSlot") + " " + slotNumber);

        GameLoader loader = GameLoader.getInstance();
        loader.setSlot(slotNumber);

        createNewGameButton.setDisable(false);
        resumeGameButton.setDisable(loader.isSlotEmpty(slotNumber));
        deleteGameButton.setDisable(loader.isSlotEmpty(slotNumber));
    }

    @FXML
    public void resumeGame() throws IOException {
        GameLoader loader = GameLoader.getInstance();
        Controller domainController = loader.loadGame();
        ResourceBundle messages = loader.getMessageBundle();

        // close Start Screen window
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
        guiController.changeIconSet(GameLoader.getInstance().getIconFolderPath());

        // initialize the game-board
        guiController.internationalize(messages);
        guiController.initializeGameBoard();
    }

    @FXML
    public void createNewGame() throws IOException {
        // close Start Screen window
        Stage stage = (Stage) createNewGameButton.getScene().getWindow();
        stage.close();

        //Open up game board window
        FXMLLoader fxmlLoader = new FXMLLoader(Catan.class.getResource("new_game.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void deleteGame() throws IOException {
        if (GameLoader.getInstance().deleteGame()) {
            this.initialize();
            resumeGameButton.setDisable(true);
            deleteGameButton.setDisable(true);
        }
    }

    @FXML
    public void exit() {
        Platform.exit();
    }
}
