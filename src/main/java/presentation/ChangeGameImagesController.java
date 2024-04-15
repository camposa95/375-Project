package presentation;

import data.GameLoader;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class ChangeGameImagesController {
    private static final String IMAGE_ROOT_FOLDER = "images";
    @FXML
    ComboBox existingResourcesComboBox;
    @FXML
    Button selectNewFolderButton;

    @FXML
    public void initialize() {
        initComboBox();
    }

    @FXML
    private void handleNewFolderButtonClick() throws IOException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose Directory");
        File selectedDirectory = chooser.showDialog(selectNewFolderButton.getScene().getWindow());
        File newDir = new File(getClass().getClassLoader().getResource(IMAGE_ROOT_FOLDER).getPath(), selectedDirectory.getName());
        FileUtils.copyDirectory(selectedDirectory, newDir);
        close(Path.of(IMAGE_ROOT_FOLDER, selectedDirectory.getName()).toString());
    }

    @FXML
    private void handleExistingFolderClick() throws IOException {
        File selectedDir = new File(IMAGE_ROOT_FOLDER, (String) existingResourcesComboBox.getValue());
        close(selectedDir.getPath());
    }

    private void initComboBox() {
        File root = new File(getClass().getClassLoader().getResource(IMAGE_ROOT_FOLDER).getFile());
        for (File f : root.listFiles()) {
            existingResourcesComboBox.getItems().add(f.getName());
        }
    }

    private void close(String folder) throws IOException {
        Stage stage = (Stage) existingResourcesComboBox.getScene().getWindow();
        stage.close();

        //Open up start window again
        FXMLLoader fxmlLoader = new FXMLLoader(Catan.class.getResource("start_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();

        GameLoader.getInstance().setIconFolderPath(folder);
    }
}
