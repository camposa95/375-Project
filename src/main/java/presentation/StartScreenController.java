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

    @FXML
    public Button createNewGameButton, resumeGameButton, deleteGameButton;
    public Button slot1, slot2, slot3, slot4;
    public Button exitButton;
    public Text selectedSlotText;

    @FXML
    public void initialize() {
        createNewGameButton.setDisable(true);
        resumeGameButton.setDisable(true);

        GameLoader loader = GameLoader.getInstance();
        slot1.setText(loader.isSlotEmpty(1) ? "Empty" : "Slot 1");
        slot2.setText(loader.isSlotEmpty(2) ? "Empty" : "Slot 2");
        slot3.setText(loader.isSlotEmpty(3) ? "Empty" : "Slot 3");
        slot4.setText(loader.isSlotEmpty(4) ? "Empty" : "Slot 4");
    }

    @FXML
    public void selectSlot(ActionEvent e) {
        String slot = ((Button) e.getSource()).getId();
        int slotNumber = Integer.parseInt(slot.substring(4));
        selectedSlotText.setText("Selected Slot " + slotNumber);

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
        } else {
            System.out.println("Failed to Delete Game");
        }
    }

    @FXML
    public void exit() {
        // Close the main application window
        Platform.exit();
    }
}
