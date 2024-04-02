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

public class YearOfPlentyController implements Popup {
    private CatanGUIController guiController;
    private Controller domainController;

    @FXML
    private RadioButton lumber1, brick1, wool1, grain1, ore1, lumber2, brick2, wool2, grain2, ore2;
    @FXML
    private ToggleGroup resource1, resource2;
    ResourceBundle messages;
    @FXML
    private Button selectButton;
    @FXML
    private Text yearOfPlentyTitleText, firstResourceText, secondResourceText;
    @FXML
    public void initialize() {
        resource1 = new ToggleGroup();
        resource2 = new ToggleGroup();

        lumber1.setToggleGroup(resource1);
        brick1.setToggleGroup(resource1);
        wool1.setToggleGroup(resource1);
        grain1.setToggleGroup(resource1);
        ore1.setToggleGroup(resource1);

        lumber2.setToggleGroup(resource2);
        brick2.setToggleGroup(resource2);
        wool2.setToggleGroup(resource2);
        grain2.setToggleGroup(resource2);
        ore2.setToggleGroup(resource2);
    }

    public void setControllers(CatanGUIController guiController, Controller domainController) {
        this.guiController = guiController;
        this.domainController = domainController;
    }

    public void setMessages(ResourceBundle messages) {
        this.messages=messages;
        internationalize();
    }


    private void internationalize() {
        yearOfPlentyTitleText.setText(messages.getString("yearOfPlentyTitleText"));
        firstResourceText.setText(messages.getString("yearOfPlentyFirstResourceText"));
        secondResourceText.setText(messages.getString("yearOfPlentySecondResourceText"));

        lumber1.setText(messages.getString("yearOfPlentyLumber"));
        lumber2.setText(messages.getString("yearOfPlentyLumber"));
        brick1.setText(messages.getString("yearOfPlentyBrick"));
        brick2.setText(messages.getString("yearOfPlentyBrick"));
        wool1.setText(messages.getString("yearOfPlentyWool"));
        wool2.setText(messages.getString("yearOfPlentyWool"));
        grain1.setText(messages.getString("yearOfPlentyGrain"));
        grain2.setText(messages.getString("yearOfPlentyGrain"));
        ore1.setText(messages.getString("yearOfPlentyOre"));
        ore2.setText(messages.getString("yearOfPlentyOre"));

        selectButton.setText(messages.getString("yearOfPlentySelectButton"));
    }

    public void submitYOP() {
        Resource selected1 = stringToResource(((RadioButton) resource1.getSelectedToggle()).getText());
        Resource selected2 = stringToResource(((RadioButton) resource2.getSelectedToggle()).getText());

        SuccessCode code = this.submitYearOfPlenty(selected1, selected2);
        if(code == SuccessCode.SUCCESS){
            this.close();
        }else{
            System.out.println("Bank does not have enough resources!");
        }
    }

    private SuccessCode submitYearOfPlenty(Resource resource1, Resource resource2) {
        //submit year of plenty
        SuccessCode success = this.domainController.playYearOfPlenty(resource1, resource2);
        if(success == SuccessCode.SUCCESS){
            this.guiController.finishedMove();
        }
        return success;
    }

    private Resource stringToResource(String resource) {
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

    public void close() {
        this.guiController.notifyOfPopupClose(this);
        Stage stage = (Stage) lumber1.getScene().getWindow();
        stage.close();
    }
}
