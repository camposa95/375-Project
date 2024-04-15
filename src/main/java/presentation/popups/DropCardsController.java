package presentation.popups;

import domain.controller.Controller;
import domain.player.Player;
import domain.bank.Resource;
import presentation.CatanGUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class DropCardsController implements Popup {

    @FXML
    private Text player1name, player2name, player3name, player4name, dropHalfTitle, tooltip;
    @FXML
    private TextField player1lumber, player1brick, player1wool, player1grain, player1ore,
            player2lumber, player2brick, player2wool, player2grain, player2ore,
            player3lumber, player3brick, player3wool, player3grain, player3ore,
            player4lumber, player4brick, player4wool, player4grain, player4ore;
    @FXML
    private Rectangle lumberIcon, brickIcon, woolIcon, grainIcon, oreIcon;

   private TextField[] player1, player2, player3, player4;
   private TextField[][] playerResources2d;
   private ResourceBundle messages;

    @FXML
    private Button submit;
    private Player[] players;
    private int numPlayers;
    private CatanGUIController guiController;
    private Controller domainController;
    private ArrayList<Integer> playersThatNeedToDrop = new ArrayList<>();

    @FXML
    public void initialize(){
        lumberIcon.setFill(new ImagePattern(new Image("images/default/card_lumber.png")));
        brickIcon.setFill(new ImagePattern(new Image("images/default/card_brick.png")));
        woolIcon.setFill(new ImagePattern(new Image("images/default/card_wool.png")));
        grainIcon.setFill(new ImagePattern(new Image("images/default/card_wheat.png")));
        oreIcon.setFill(new ImagePattern(new Image("images/default/card_ore.png")));

        player1 = new TextField[]{player1lumber, player1brick, player1wool, player1grain, player1ore};
        player2 = new TextField[]{player2lumber, player2brick, player2wool, player2grain, player2ore};
        player3 = new TextField[]{player3lumber, player3brick, player3wool, player3grain, player3ore};
        player4 = new TextField[]{player4lumber, player4brick, player4wool, player4grain, player4ore};
    }

    public void internationalize(){
        dropHalfTitle.setText(messages.getString("dropHalfTitle"));
        tooltip.setText(messages.getString("dropHalfTooltipDefault"));

        player1name.setText(messages.getString("dropHalfPlayer1"));
        player2name.setText(messages.getString("dropHalfPlayer2"));
        player3name.setText(messages.getString("dropHalfPlayer3"));
        player4name.setText(messages.getString("dropHalfPlayer4"));

        submit.setText(messages.getString("dropHalfSubmitButton"));
    }

    public void setPlayerData(Player[] playersArg, CatanGUIController guiController, ResourceBundle messages, Controller domainController){
        this.messages=messages;
        internationalize();

        players = playersArg;
        numPlayers = playersArg.length;
        this.playerResources2d = new TextField[numPlayers][];
        playerResources2d[0] = player1;
        playerResources2d[1] = player2;

        if(numPlayers < 4){
            player4name.setVisible(false);
            for(int i = 0; i < player4.length; i++){
                player4[i].setVisible(false);
            }
        }else{
            playerResources2d[3] = player4;
        }
        if(numPlayers < 3){
            player3name.setVisible(false);
            for(int i = 0; i < player3.length; i++){
                player3[i].setVisible(false);
            }
        }else{
            playerResources2d[2] = player3;
        }

        this.guiController = guiController;
        this.domainController = domainController;

        boolean check = doesAnyoneNeedToDrop();
        if(!check){
            this.dropResources(null);
            this.close();
        }
    }

    private Resource[] getPlayerResources(TextField[] playerResources) {
        int lumber = Integer.parseInt(playerResources[0].getText());
        int brick = Integer.parseInt(playerResources[1].getText());
        int wool = Integer.parseInt(playerResources[2].getText());
        int grain = Integer.parseInt(playerResources[3].getText());
        int ore = Integer.parseInt(playerResources[4].getText());

        int size = lumber + brick + wool + grain + ore;
        Resource[] resources = new Resource[size];
        int counter = 0;
        for(int l = 0; l < lumber; l++){
            resources[counter] = Resource.LUMBER;
            counter++;
        }
        for(int b = 0; b < brick; b++){
            resources[counter] = Resource.BRICK;
            counter++;
        }
        for(int w = 0; w < wool; w++){
            resources[counter] = Resource.WOOL;
            counter++;
        }
        for(int g = 0; g < grain; g++){
            resources[counter] = Resource.GRAIN;
            counter++;
        }
        for(int o = 0; o < ore; o++){
            resources[counter] = Resource.ORE;
            counter++;
        }
        return resources;
    }

    public void submitDropResource() {
        HashMap<Integer,Resource[]> resources = new HashMap<>();

        for(int i = 0; i < playersThatNeedToDrop.size(); i++){
            int droppingPlayer = playersThatNeedToDrop.get(i);
            Resource[] getPlayerResources = this.getPlayerResources(playerResources2d[droppingPlayer]);
            resources.put(droppingPlayer+1, getPlayerResources);
            int amountToDrop = players[droppingPlayer].hand.getResourceCount()/2;
            if(getPlayerResources.length != amountToDrop){
                System.out.println("Player " + i + ": Not enough resources dropped");
                return;
            }
        }
        this.dropResources(resources);
        this.close();
    }

    private void dropResources(HashMap<Integer, Resource[]> resourcesToDrop) {
        //Called from DropCardsController.java
        if(resourcesToDrop!=null){ //if null, no dropping occurred
            this.domainController.dropResources(resourcesToDrop);
            this.guiController.finishedMove();
        }

        guiController.setAllRobberSpotsVisibility(true);
    }

    //returns true if at least one player needs to drop cards
    public boolean doesAnyoneNeedToDrop() {
        for(int i = 0; i < players.length; i++){
            if(players[i].hand.getResourceCount() <=7 ){
                for(int j = 0; j < playerResources2d[i].length; j++){
                    playerResources2d[i][j].setVisible(false);
                    playerResources2d[i][j]=null;
                }
            }else{
                playersThatNeedToDrop.add(i);
            }
        }
        return playersThatNeedToDrop.size() != 0;
    }

    public void close() {
        this.guiController.notifyOfPopupClose(this);
        Stage stage = (Stage) lumberIcon.getScene().getWindow();
        stage.close();
    }
}
