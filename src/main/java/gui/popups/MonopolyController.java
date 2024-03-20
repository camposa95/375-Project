package gui.popups;

import controller.Controller;
import controller.SuccessCode;
import gamedatastructures.Resource;
import gui.CatanGUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class MonopolyController {

    @FXML
    private Button lumber, brick, wool, grain, ore;
    @FXML
    private Text monopolyTitleText;
    private CatanGUIController guiController;
    private Controller domainController;
    private ResourceBundle messages;
    @FXML
    public void initialize(){
        lumber.setGraphic(new ImageView(new Image("images/card_lumber.png")));
        brick.setGraphic(new ImageView(new Image("images/card_brick.png")));
        wool.setGraphic(new ImageView(new Image("images/card_wool.png")));
        grain.setGraphic(new ImageView(new Image("images/card_grain.png")));
        ore.setGraphic(new ImageView(new Image("images/card_ore.png")));
    }

    public void setControllers(CatanGUIController guiController, Controller domainController) {
        this.guiController = guiController;
        this.domainController = domainController;
    }

    public void setMessages(ResourceBundle messages){
        this.messages = messages;
        internationalize();
    }

    public void internationalize(){
        monopolyTitleText.setText(messages.getString("monopolyTitleText"));
    }

    public void selectResource(MouseEvent event){
        String source = ((Button)event.getSource()).getId();
        SuccessCode success;
        switch(source){
            case "lumber":
                success = this.executeMonopoly(Resource.LUMBER);
                break;
            case "brick":
                success = this.executeMonopoly(Resource.BRICK);
                break;
            case "wool":
                success = this.executeMonopoly(Resource.WOOL);
                break;
            case "grain":
                success = this.executeMonopoly(Resource.GRAIN);
                break;
            case "ore":
                success = this.executeMonopoly(Resource.ORE);
                break;
            default:
                System.out.println("Something went wrong, try again.");
                return;
        }
        if(success == SuccessCode.SUCCESS){
            Stage stage = (Stage)lumber.getScene().getWindow();
            stage.close();
        }
    }

    private SuccessCode executeMonopoly(Resource resource){
        SuccessCode success = this.domainController.playMonopolyCard(resource);
        if(success == SuccessCode.SUCCESS){
            this.guiController.finishedMove();
        }

        return success;
    }
}
