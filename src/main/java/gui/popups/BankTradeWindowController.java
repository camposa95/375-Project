package gui.popups;

import controller.SuccessCode;
import gamedatastructures.Resource;
import gui.CatanGUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class BankTradeWindowController {

    @FXML
    private RadioButton giveLumber, giveBrick, giveWool, giveGrain, giveOre, receiveLumber, receiveBrick, receiveWool, receiveGrain, receiveOre;
    @FXML
    private Button tradeButton, cancelButton;
    @FXML
    private Text tooltip, bankTradeWindowTitleText, resourceGiveText, resourceReceiveText;
    private ToggleGroup giveGroup, receiveGroup;
    private CatanGUIController guiController;
    private ResourceBundle messages;

    @FXML
    private void initialize(){
        giveGroup = new ToggleGroup();
        receiveGroup = new ToggleGroup();
        giveLumber.setToggleGroup(giveGroup);
        giveBrick.setToggleGroup(giveGroup);
        giveWool.setToggleGroup(giveGroup);
        giveGrain.setToggleGroup(giveGroup);
        giveOre.setToggleGroup(giveGroup);

        receiveLumber.setToggleGroup(receiveGroup);
        receiveBrick.setToggleGroup(receiveGroup);
        receiveWool.setToggleGroup(receiveGroup);
        receiveGrain.setToggleGroup(receiveGroup);
        receiveOre.setToggleGroup(receiveGroup);

        giveLumber.setSelected(true);
        receiveLumber.setSelected(true);
    }

    public Resource stringToResource(String resource){
        if(resource.equals(messages.getString("bankTradeWindowLumber"))){
            return Resource.LUMBER;
        }else if(resource.equals(messages.getString("bankTradeWindowBrick"))){
            return Resource.BRICK;
        }else if(resource.equals(messages.getString("bankTradeWindowWool"))){
            return Resource.WOOL;
        }else if(resource.equals(messages.getString("bankTradeWindowGrain"))){
            return Resource.GRAIN;
        }else if(resource.equals(messages.getString("bankTradeWindowOre"))){
            return Resource.ORE;
        }else{
            throw new IllegalArgumentException("No matching resource");
        }
    }

    public void tradeButtonPressed(){
        Resource giveSelected = stringToResource(((RadioButton) giveGroup.getSelectedToggle()).getText());
        Resource receiveSelected = stringToResource(((RadioButton) receiveGroup.getSelectedToggle()).getText());

        if(giveSelected.equals(receiveSelected)){
            tooltip.setText(messages.getString("bankTradeWindowTooltipSameGiveReceive"));
        }else{
            SuccessCode success = guiController.executeBankTrade(giveSelected, receiveSelected);
            if(success == SuccessCode.SUCCESS){
                closeModal();
            }else if(success == SuccessCode.INSUFFICIENT_RESOURCES){
                tooltip.setText(messages.getString("bankTradeWindowTooltipTradeFailed"));
            }
        }
    }

    public void setData(CatanGUIController gui, ResourceBundle messages){
        this.messages=messages;
        this.guiController = gui;

        bankTradeWindowTitleText.setText(messages.getString("bankTradeWindowTitleText"));
        tradeButton.setText(messages.getString("bankTradeWindowSubmitTradeButton"));
        cancelButton.setText(messages.getString("bankTradeWindowCancelTradeButton"));
        tooltip.setText(messages.getString("bankTradeWindowTooltipSubmitText"));
        resourceGiveText.setText(messages.getString("bankTradeWindowResourceGiveText"));
        resourceReceiveText.setText(messages.getString("bankTradeWindowResourceReceiveText"));

        giveLumber.setText(messages.getString("bankTradeWindowLumber"));
        giveBrick.setText(messages.getString("bankTradeWindowBrick"));
        giveWool.setText(messages.getString("bankTradeWindowWool"));
        giveGrain.setText(messages.getString("bankTradeWindowGrain"));
        giveOre.setText(messages.getString("bankTradeWindowOre"));

        receiveLumber.setText(messages.getString("bankTradeWindowLumber"));
        receiveBrick.setText(messages.getString("bankTradeWindowBrick"));
        receiveWool.setText(messages.getString("bankTradeWindowWool"));
        receiveGrain.setText(messages.getString("bankTradeWindowGrain"));
        receiveOre.setText(messages.getString("bankTradeWindowOre"));
    }
    public void closeModal(){
        //close Bank Trade window
        Stage stage = (Stage) giveLumber.getScene().getWindow();
        stage.close();
    }
}
