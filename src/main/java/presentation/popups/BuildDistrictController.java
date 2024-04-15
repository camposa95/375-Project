package presentation.popups;

import domain.controller.Controller;
import domain.controller.GameState;
import domain.controller.SuccessCode;
import domain.game.DistrictType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import presentation.CatanGUIController;

import java.util.ResourceBundle;

public class BuildDistrictController implements Popup {
    private CatanGUIController guiController;
    private Controller domainController;
    private int selectedVertex;
    private Polygon selectedBuilding;

    @FXML
    private RadioButton sawmill, kiln, barn, garden, mine;
    @FXML
    private ToggleGroup types;
    ResourceBundle messages;
    @FXML
    private Button selectButton;
    @FXML
    private Text buildDistrictText;
    @FXML
    public void initialize() {
        types = new ToggleGroup();

        sawmill.setToggleGroup(types);
        kiln.setToggleGroup(types);
        barn.setToggleGroup(types);
        garden.setToggleGroup(types);
        mine.setToggleGroup(types);
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setControllers(CatanGUIController guiController, Controller domainController) {
        this.guiController = guiController;
        this.domainController = domainController;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setMessages(ResourceBundle messages) {
        this.messages=messages;
        internationalize();
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setSelectedVertex(int id) {
        this.selectedVertex = id;
    }
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setSelectedBuilding(Polygon building) {
        this.selectedBuilding = building;
    }

    private void internationalize() {
        buildDistrictText.setText(messages.getString("buildDistrictPopupText"));

        sawmill.setText(messages.getString("buildDistrictSawmill"));
        kiln.setText(messages.getString("buildDistrictKiln"));
        barn.setText(messages.getString("buildDistrictBarn"));
        garden.setText(messages.getString("buildDistrictGarden"));
        mine.setText(messages.getString("buildDistrictMine"));

        selectButton.setText(messages.getString("buildDistrictSelectButton"));
    }

    public void submitBD() {
        DistrictType type = stringToDistrictType(((RadioButton) types.getSelectedToggle()).getText());

        SuccessCode code = this.submitBuildDistrict(this.selectedVertex, type);
        if(code == SuccessCode.INSUFFICIENT_RESOURCES){
            guiController.setTooltipText(messages.getString("buildDistrictInsufficientResources"));
        } else if (code == SuccessCode.INVALID_PLACEMENT){
            guiController.setTooltipText(messages.getString("buildDistrictInnvalidPlacement"));
        }

        domainController.setState(GameState.DEFAULT);
        guiController.guiState = CatanGUIController.GUIState.IDLE;
        this.close();
    }

    private SuccessCode submitBuildDistrict(int vertexId, DistrictType type) {
        SuccessCode success = this.domainController.buildDistrict(vertexId, type);
        if(success == SuccessCode.SUCCESS){
            this.guiController.finishedMove();
            this.guiController.setDistrictColor(selectedBuilding, this.domainController.getDistrictTypeForVertex(vertexId));

        }
        return success;
    }

    private DistrictType stringToDistrictType(String district) {
        if(district.equals(messages.getString("buildDistrictSawmill"))){
            return DistrictType.SAWMILL;
        }else if(district.equals(messages.getString("buildDistrictKiln"))){
            return DistrictType.KILN;
        }else if(district.equals(messages.getString("buildDistrictBarn"))){
            return DistrictType.BARN;
        }else if(district.equals(messages.getString("buildDistrictGarden"))){
            return DistrictType.GARDEN;
        }else if(district.equals(messages.getString("buildDistrictMine"))){
            return DistrictType.MINE;
        }else{
            throw new IllegalArgumentException("No matching resource");
        }
    }

    public void close() {
        this.guiController.notifyOfPopupClose(this);
        Stage stage = (Stage) sawmill.getScene().getWindow();
        stage.close();
    }
}
