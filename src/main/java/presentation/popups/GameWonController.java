package presentation.popups;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.util.ResourceBundle;

public class GameWonController {

    @FXML
    private Text playerWonNumber, gameWonTitle1, gameWonTitle2;

    private ResourceBundle messages;

    public void setPlayerWon(int playerWon, ResourceBundle messages){
        playerWonNumber.setText(Integer.toString(playerWon));
        this.messages=messages;
        internationalize();
    }

    public void internationalize(){
        gameWonTitle1.setText(messages.getString("gameWonTitle1"));
        gameWonTitle2.setText(messages.getString("gameWonTitle2"));
    }
}
