package presentation.popups;

import data.GameLoader;
import domain.bank.Loan;
import domain.bank.Resource;
import domain.controller.Controller;
import domain.controller.SuccessCode;
import domain.game.NotEnoughResourcesException;
import domain.player.Player;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import presentation.CatanGUIController;

import java.util.ResourceBundle;

public class BankLoanWindowController extends Popup {
    @FXML
    private TextField giveLumber, giveBrick, giveWool, giveGrain, giveOre, receiveLumber, receiveBrick, receiveWool, receiveGrain, receiveOre;
    @FXML
    private Rectangle lumberIcon, brickIcon, woolIcon, grainIcon, oreIcon;
    @FXML
    private Text title, loanText, loanInfoText, tooltip;
    @FXML
    private Button submitButton, cancelButton;
    private TextField[] give = null;

    @FXML
    private void initialize() {
        give = new TextField[]{giveLumber, giveBrick, giveWool, giveGrain, giveOre};
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setMessages(ResourceBundle messages) {
        this.messages=messages;
        internationalize();
    }

    public void internationalize() {
        title.setText(messages.getString("bankLoanTitle"));
        loanText.setText(messages.getString("bankLoanResourcesPrompt"));
        tooltip.setText(messages.getString("bankLoanTooltipDefault"));
        loanInfoText.setText(messages.getString("bankLoanRepaymentInfo"));
        submitButton.setText(messages.getString("bankLoanSubmit"));
        cancelButton.setText(messages.getString("cancelText"));
    }

    @Override
    protected void setupStateData() {
        lumberIcon.setFill(GameLoader.getInstance().getImage("card_lumber.png"));
        brickIcon.setFill(GameLoader.getInstance().getImage("card_brick.png"));
        woolIcon.setFill(GameLoader.getInstance().getImage("card_wool.png"));
        grainIcon.setFill(GameLoader.getInstance().getImage("card_wheat.png"));
        oreIcon.setFill(GameLoader.getInstance().getImage("card_ore.png"));
    }

    private Resource[] getResources(TextField[] fields) {
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

    @FXML
    public void handleSubmitButtonPressed() {
        Resource[] resources = getResources(give);
        if (Loan.loanIsValid(resources)) {
            try {
                this.domainController.takeOutLoan(resources);
                close();
            } catch (NotEnoughResourcesException e) {
                tooltip.setText(messages.getString("bankLoanTooltipNotEnoughResources"));
            } catch (IllegalStateException e) {
                tooltip.setText(messages.getString("bankLoanTooltipIllegalState"));
            }
        } else {
            tooltip.setText(messages.getString("bankLoanTooltipInvalidLoan").formatted(Loan.MAX_LOAN_SIZE));
        }
    }

    public void closeStage() {
        this.guiController.notifyOfPopupClose(this);
        Stage stage = (Stage) lumberIcon.getScene().getWindow();
        stage.close();
    }
}
