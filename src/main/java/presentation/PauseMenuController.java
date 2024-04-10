package presentation;

import data.GameLoader;
import data.SaveException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import presentation.popups.Popup;

import java.io.IOException;
import java.util.ResourceBundle;

public class PauseMenuController implements Popup {

    public Text title, selectorHeader;
    @FXML
    private Button saveButton, closeButton;
    @FXML
    public ChoiceBox<String> languageSelector;
    private CatanGUIController guiController;
    private ResourceBundle messages;
    private Pane gameBoard;


    @FXML
    private void initialize() {
        languageSelector.setItems(FXCollections.observableArrayList("English", "Espanol"));
    }

    public void setControllers(CatanGUIController guiController, Pane gameBoard) {
        this.guiController = guiController;
        this.gameBoard = gameBoard;
    }

    public void setMessages(ResourceBundle messages) {
        this.messages = messages;

        saveButton.setText(messages.getString("saveGameButton"));
        closeButton.setText(messages.getString("closeButton"));
        title.setText(messages.getString("pauseMenuTitle"));
        selectorHeader.setText(messages.getString("languageSelectorHeader"));
    }

    public void saveButtonPressed() throws IOException {
        if (!GameLoader.getInstance().saveGame()) {
            guiController.tooltipText.setText(messages.getString("saveFail"));
        } else {
            // switch back to the main screen
            Stage stage = (Stage) gameBoard.getScene().getWindow();
            stage.close();
            stage = (Stage) saveButton.getScene().getWindow();
            stage.close();


            FXMLLoader fxmlLoader = new FXMLLoader(Catan.class.getResource("start_screen.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        }
    }

    public void changeLanguage() throws IOException {
        String language = this.languageSelector.getValue();

        this.setMessages(GameLoader.getInstance().setLanguage(language));
        guiController.internationalize(this.messages);
        guiController.tooltipText.setText(messages.getString("paused"));
    }

    public void close() {
        this.guiController.notifyOfPopupClose(this);
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
