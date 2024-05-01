package presentation.popups;

import domain.controller.Controller;
import domain.controller.GameState;
import domain.controller.SuccessCode;
import domain.player.Player;
import presentation.CatanGUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class RobPlayerController extends Popup {

    @FXML
    private Button otherPlayer1, otherPlayer2, otherPlayer3;
    private Button[] buttons;
    @FXML
    private Text robPlayerTitleText;

    @FXML
    public void initialize(){
        this.buttons = new Button[] {otherPlayer1, otherPlayer2, otherPlayer3};
    }

    @Override
    protected void setupStateData() {
        // make sure Players is the list of players on the hex being robbed
        Player currentPlayer = domainController.getCurrentPlayer();
        Player[] playersOnHex = domainController.getPlayersOnTile(guiController.robberId);

        int numPlayers = playersOnHex.length;
        for(int i = 0; i < numPlayers; i++){
            if(playersOnHex[i].playerNum != currentPlayer.playerNum){
                this.buttons[i].setText(messages.getString("robPlayerPlayerButton") + playersOnHex[i].playerNum);
            }
        }

        int count = 0;
        int total = 0;
        String basis = messages.getString("robPlayerPlayerButton") + "X";
        for (Button button: buttons) {
            total++;
            if (button.getText().length() != basis.length()) {
                count++;
                button.setVisible(false);
            }
        }
        if (count == total) {
            this.rob(-1);
            this.close();
        }
    }

    protected void internationalize() {
        robPlayerTitleText.setText(messages.getString("robPlayerTitleText"));
        otherPlayer1.setText(messages.getString("robPlayerPlayerButton") + " ");
    }

    private int getSelectedPlayer(MouseEvent event){
        String buttonText =((Button)event.getSource()).getText();
        System.out.println(buttonText);
        return Integer.parseInt(buttonText.substring(buttonText.length()-1));
    }

    public void robPlayer(MouseEvent event){
        int playerRobbed = this.getSelectedPlayer(event);
        SuccessCode success = this.rob(playerRobbed);
        if(success == SuccessCode.SUCCESS){
            this.close();
        }
    }

    private SuccessCode rob(int passedPlayer){
        // Called from RobPlayerController.java
        if(passedPlayer == -1){
            //means that there is no one to rob, don't submit anything
            guiController.finishedMove();
            domainController.setState(GameState.DEFAULT);
            return SuccessCode.SUCCESS;
        }
        SuccessCode code = domainController.robPlayer(passedPlayer);

        if(code == SuccessCode.SUCCESS){
            domainController.setState(GameState.DEFAULT);
            guiController.finishedMove();
        }
        return code;
    }

    @Override
    protected void closeStage() {
        Stage stage = (Stage) otherPlayer1.getScene().getWindow();
        stage.close();
    }
}
