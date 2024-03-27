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

public class RobPlayerController {

    @FXML
    private Button otherPlayer1, otherPlayer2, otherPlayer3;
    private Player[] players;
    private Player currentPlayer;
    private CatanGUIController guiController;
    private Controller domainController;
    private int robberId = 0;
    private Button[] buttons;
    @FXML
    private Text robPlayerTitleText;
    ResourceBundle messages;

    @FXML
    public void initialize(){
        this.buttons = new Button[]{otherPlayer1, otherPlayer2, otherPlayer3};
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
            Stage stage = (Stage) otherPlayer1.getScene().getWindow();
            stage.close();
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

    private void internationalize(){
        robPlayerTitleText.setText(messages.getString("robPlayerTitleText"));
        otherPlayer1.setText(messages.getString("robPlayerPlayerButton") + " ");
    }

    //make sure Players is the list of players on the hex being robbed
    public void setPlayerData(Player currentPlayer, Player[] playersOnHex, CatanGUIController guiController, ResourceBundle messages, Controller domainController) {
        this.currentPlayer = currentPlayer;
        this.guiController = guiController;
        this.domainController = domainController;
        this.players=playersOnHex;
        this.messages=messages;

        internationalize();

        int numPlayers = this.players.length;
        for(int i = 0; i < numPlayers; i++){
            if(playersOnHex[i].playerNum != currentPlayer.playerNum){
                this.buttons[i].setText(messages.getString("robPlayerPlayerButton") + playersOnHex[i].playerNum);
            }
        }

        int count = 0;
        int total = 0;
        String basis = messages.getString("robPlayerPlayerButton") + "X";
        System.out.println(basis);
        for(Button button: buttons){
            total++;
            if(button.getText().length() != basis.length()){
                count++;
                button.setVisible(false);
            }
        }
        if(count==total){
            this.rob(-1);
            Stage stage = (Stage) otherPlayer1.getScene().getWindow();
            stage.close();
        }

    }
}
