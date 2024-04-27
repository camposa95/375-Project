package presentation.popups;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameWonController extends Popup {

    @FXML
    private Text playerWonNumber, gameWonTitle1, gameWonTitle2;

    protected void internationalize() {
        gameWonTitle1.setText(messages.getString("gameWonTitle1"));
        gameWonTitle2.setText(messages.getString("gameWonTitle2"));
    }

    @Override
    protected void setupStateData() {
        playerWonNumber.setText(Integer.toString(domainController.getCurrentPlayer().playerNum));
    }

    @Override
    protected void closeStage() {
        Stage stage = (Stage) playerWonNumber.getScene().getWindow();
        stage.close();
    }
}
