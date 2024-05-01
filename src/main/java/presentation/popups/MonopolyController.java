package presentation.popups;

import domain.controller.SuccessCode;
import domain.bank.Resource;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MonopolyController extends Popup {

    @FXML
    private Button lumber, brick, wool, grain, ore;
    @FXML
    private Text monopolyTitleText;
    @FXML
    public void initialize() {
        lumber.setGraphic(new ImageView(new Image("images/default/card_lumber.png")));
        brick.setGraphic(new ImageView(new Image("images/default/card_brick.png")));
        wool.setGraphic(new ImageView(new Image("images/default/card_wool.png")));
        grain.setGraphic(new ImageView(new Image("images/card_grain.png")));
        ore.setGraphic(new ImageView(new Image("images/default/card_ore.png")));
    }

    protected void internationalize(){
        monopolyTitleText.setText(messages.getString("monopolyTitleText"));
    }

    public void selectResource(MouseEvent event) {
        String source = ((Button)event.getSource()).getId();
        SuccessCode success;
        switch (source) {
            case "lumber" -> success = this.executeMonopoly(Resource.LUMBER);
            case "brick" -> success = this.executeMonopoly(Resource.BRICK);
            case "wool" -> success = this.executeMonopoly(Resource.WOOL);
            case "grain" -> success = this.executeMonopoly(Resource.GRAIN);
            case "ore" -> success = this.executeMonopoly(Resource.ORE);
            default -> {
                System.out.println("Something went wrong, try again.");
                return;
            }
        }
        if(success == SuccessCode.SUCCESS){
            this.close();
        }
    }

    private SuccessCode executeMonopoly(Resource resource) {
        SuccessCode success = this.domainController.playMonopolyCard(resource);
        if(success == SuccessCode.SUCCESS){
            this.guiController.finishedMove();
        }

        return success;
    }

    protected void closeStage() {
        Stage stage = (Stage)lumber.getScene().getWindow();
        stage.close();
    }
}
