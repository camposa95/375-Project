package presentation.popups;

import data.GameLoader;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import presentation.CatanGUIController;
import presentation.popups.Popup;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ChangeGameImagesController extends Popup {
    private static final String IMAGE_ROOT_FOLDER = "images";
    @FXML
    ComboBox existingResourcesComboBox;
    @FXML
    Button selectNewFolderButton, closeButton;
    @FXML
    Label existingText, orText;

    @FXML
    public void initialize() {
        initComboBox();
    }

    @Override
    protected void internationalize() {
        existingText.setText(messages.getString("changeImagesExisting"));
        orText.setText(messages.getString("changeImagesOr"));
        selectNewFolderButton.setText(messages.getString("changeImagesNew"));
        closeButton.setText(messages.getString("closeButton"));
    }

    @FXML
    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    private void handleNewFolderButtonClick() throws IOException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose Directory");
        File selectedDirectory = chooser.showDialog(selectNewFolderButton.getScene().getWindow());
        File newDir = new File(getClass().getClassLoader().getResource(IMAGE_ROOT_FOLDER).getPath(), selectedDirectory.getName());
        FileUtils.copyDirectory(selectedDirectory, newDir);
        GameLoader.getInstance().setImageFolderPath(Path.of(IMAGE_ROOT_FOLDER, selectedDirectory.getName()).toString());
        this.guiController.initAllImages();
    }

    @FXML
    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    private void handleExistingFolderClick() throws IOException {
        File selectedDir = new File(IMAGE_ROOT_FOLDER, (String) existingResourcesComboBox.getValue());
        GameLoader.getInstance().setImageFolderPath(selectedDir.getPath());
        this.guiController.initAllImages();
    }

    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    private void initComboBox() {
        File root = new File(getClass().getClassLoader().getResource(IMAGE_ROOT_FOLDER).getFile());
        if (root != null) {
            File[] fs = root.listFiles();
            if (fs != null) {
                for (File f : fs) {
                    existingResourcesComboBox.getItems().add(f.getName());
                }
            }
        }
    }

    protected void closeStage() {
        Stage stage = (Stage) existingResourcesComboBox.getScene().getWindow();
        stage.close();
    }
}
