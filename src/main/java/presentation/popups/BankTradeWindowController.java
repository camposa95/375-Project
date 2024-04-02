package presentation.popups;

import domain.controller.Controller;
import domain.controller.SuccessCode;
import domain.bank.Resource;
import presentation.CatanGUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class BankTradeWindowController implements Popup {

    @FXML
    private RadioButton giveLumber, giveBrick, giveWool, giveGrain, giveOre, receiveLumber, receiveBrick, receiveWool, receiveGrain, receiveOre;
    @FXML
    private Button tradeButton, cancelButton;
    @FXML
    private Text tooltip, bankTradeWindowTitleText, resourceGiveText, resourceReceiveText;
    private ToggleGroup giveGroup, receiveGroup;
    private CatanGUIController guiController;
    private Controller domainController;
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

    public void setControllers(CatanGUIController guiController, Controller domainController) {
        this.guiController = guiController;
        this.domainController = domainController;
    }

    public void setMessages(ResourceBundle messages){
        this.messages=messages;

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
            SuccessCode success = this.executeBankTrade(giveSelected, receiveSelected);
            if(success == SuccessCode.SUCCESS){
                close();
            }else if(success == SuccessCode.INSUFFICIENT_RESOURCES){
                tooltip.setText(messages.getString("bankTradeWindowTooltipTradeFailed"));
            }
        }
    }

    private SuccessCode executeBankTrade(Resource giving, Resource receiving) {
        // Called from BankTradeWindowController.java
        SuccessCode success = domainController.tradeWithBank(giving, receiving);
        if (success == SuccessCode.SUCCESS) {
            this.guiController.finishedMove();
        }
        return success;
    }

    public void close() {
        this.guiController.notifyOfPopupClose(this);
        Stage stage = (Stage) giveLumber.getScene().getWindow();
        stage.close();
    }
}
