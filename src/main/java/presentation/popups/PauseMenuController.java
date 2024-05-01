package presentation.popups;

import data.GameLoader;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import presentation.Catan;

import java.io.IOException;

public class PauseMenuController extends Popup {

    public Text title, selectorHeader;
    @FXML
    private Button saveButton, closeButton, changeGameImagesButton;
    @FXML
    public ChoiceBox<String> languageSelector;

    @FXML
    private void initialize() {
        languageSelector.setItems(FXCollections.observableArrayList("English", "Espanol"));
    }

    protected void internationalize() {
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
            Stage stage = (Stage) guiController.gameboardPane.getScene().getWindow();
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

        setMessages(GameLoader.getInstance().setLanguage(language));
        guiController.internationalize(this.messages);
        guiController.tooltipText.setText(messages.getString("paused"));
    }

    public void changeGameImages() throws IOException {
        // Open up game board window
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Catan.class.getResource("change_game_images_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();

        ChangeGameImagesController pauseMenuController = fxmlLoader.getController();
        pauseMenuController.setControllers(this.guiController);
    }

    protected void closeStage() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
