package gui;

import controller.SuccessCode;
import gamedatastructures.Player;
import gamedatastructures.Resource;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class PlayerTradeWindowController {

    @FXML
    private Button otherPlayer1, otherPlayer2, otherPlayer3;
    @FXML
    private TextField giveLumber, giveBrick, giveWool, giveGrain, giveOre, receiveLumber, receiveBrick, receiveWool, receiveGrain, receiveOre;
    @FXML
    private Rectangle lumberIcon, brickIcon, woolIcon, grainIcon, oreIcon;
    @FXML
    private Text tooltip, playerTradeWindowTitle, youGiveText, youReceiveText;
    private TextField[] give = null;
    private TextField[] receive = null;
    Player currentPlayer;
    Player[] players;
    CatanGUIController guiController;
    ResourceBundle messages;

    @FXML
    private void initialize(){
        lumberIcon.setFill(new ImagePattern(new Image("images/card_lumber.png")));
        brickIcon.setFill(new ImagePattern(new Image("images/card_brick.png")));
        woolIcon.setFill(new ImagePattern(new Image("images/card_wool.png")));
        grainIcon.setFill(new ImagePattern(new Image("images/card_wheat.png")));
        oreIcon.setFill(new ImagePattern(new Image("images/card_ore.png")));

        give = new TextField[]{giveLumber, giveBrick, giveWool, giveGrain, giveOre};
        receive = new TextField[]{receiveLumber, receiveBrick, receiveWool, receiveGrain, receiveOre};
    }

    private void internationalize(){
        playerTradeWindowTitle.setText(messages.getString("playerTradeWindowTitle"));

        youGiveText.setText(messages.getString("playerTradeYouGiveText"));
        youReceiveText.setText(messages.getString("playerTradeYouReceiveText"));
        tooltip.setText(messages.getString("playerTradeTooltipDefault"));
    }

    public void setPlayersData(Player currentPlayer, Player[] players, CatanGUIController gui, ResourceBundle messages){
        this.currentPlayer = currentPlayer;
        this.players = players;
        this.guiController = gui;
        this.messages = messages;

        internationalize();

        int numPlayers = this.players.length;

        if(numPlayers<4){
            otherPlayer3.setVisible(false);
        }else{
            int player3 = currentPlayer.playerNum<4 ? 4 : 3;
            otherPlayer3.setText(messages.getString("playerTradeTooltipAcceptButtonText") + player3);
        }
        if(numPlayers<3){
            otherPlayer2.setVisible(false);
        }else{
            int player2 = currentPlayer.playerNum<3 ? 3 : 2;
            otherPlayer2.setText(messages.getString("playerTradeTooltipAcceptButtonText") + player2);
        }
        int player1 = currentPlayer.playerNum==1 ? 2 : 1;
        otherPlayer1.setText(messages.getString("playerTradeTooltipAcceptButtonText") + player1);
    }

    private Resource[] getResources(TextField[] fields){
        int numLumber = Integer.parseInt(fields[0].getText());
        int numBrick = Integer.parseInt(fields[1].getText());
        int numWool = Integer.parseInt(fields[2].getText());
        int numGrain = Integer.parseInt(fields[3].getText());
        int numOre = Integer.parseInt(fields[4].getText());
        Resource[] resources = new Resource[numLumber + numBrick + numWool + numGrain + numOre];
        int counter = 0;
        for(int l = 0; l < numLumber; l++){
            resources[counter] = Resource.LUMBER;
            counter++;
        }
        for(int b = 0; b < numBrick; b++){
            resources[counter] = Resource.BRICK;
            counter++;
        }
        for(int w = 0; w < numWool; w++){
            resources[counter] = Resource.WOOL;
            counter++;
        }
        for(int g = 0; g < numGrain; g++){
            resources[counter] = Resource.GRAIN;
            counter++;
        }
        for(int o = 0; o < numOre; o++){
            resources[counter] = Resource.ORE;
            counter++;
        }
        return resources;
    }

    private int getOtherPlayerNum(MouseEvent event){
        String buttonPressedText = ((Button)event.getSource()).getText();
        int otherPlayerNum = Integer.parseInt(buttonPressedText.substring(buttonPressedText.length()-1));
        return otherPlayerNum;
    }

    public void otherPlayerAccepts(MouseEvent event){
        int otherPlayer = getOtherPlayerNum(event);
        Player other = null;
        for(int i = 0; i < players.length; i++){
            Player cur = players[i];
            if(cur.playerNum==otherPlayer){
                other=cur;
            }
        }
        Resource[] giving = this.getResources(give);
        Resource[] receiving = this.getResources(receive);
        SuccessCode success = guiController.executeTrade(other, giving, receiving);

        if(success==SuccessCode.SUCCESS){
            closeModal();
        }else if(success == SuccessCode.INSUFFICIENT_RESOURCES){
            tooltip.setText(messages.getString("playerTradeTooltipInsufficientResources"));
        }else{
            tooltip.setText(messages.getString("playerTradeTooltipFailedTrade"));
        }
    }

    private void closeModal(){
        Stage stage = (Stage) lumberIcon.getScene().getWindow();
        stage.close();
    }
}
