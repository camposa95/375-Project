package presentation.popups;

import domain.controller.GameState;
import domain.controller.SuccessCode;
import domain.building.DistrictType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import presentation.CatanGUIController;

public class BuildDistrictController extends Popup {
    private int selectedVertex;
    private Polygon selectedBuilding;

    @FXML
    private RadioButton sawmill, kiln, barn, garden, mine;
    @FXML
    private ToggleGroup types;
    @FXML
    private Button selectButton, cancelButton;
    @FXML
    private Text buildDistrictText, tooltip;
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
    public void setSelectedVertex(int id) {
        this.selectedVertex = id;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setSelectedBuilding(Polygon building) {
        this.selectedBuilding = building;
    }

    protected void internationalize() {
        buildDistrictText.setText(messages.getString("buildDistrictPopupText"));
        tooltip.setText(messages.getString("buildDistrictDefaultTooltip"));

        sawmill.setText(messages.getString("buildDistrictSawmill"));
        kiln.setText(messages.getString("buildDistrictKiln"));
        barn.setText(messages.getString("buildDistrictBarn"));
        garden.setText(messages.getString("buildDistrictGarden"));
        mine.setText(messages.getString("buildDistrictMine"));

        selectButton.setText(messages.getString("buildDistrictSelectButton"));
        cancelButton.setText(messages.getString("cancelText"));
    }

    public void submitBD() {
        DistrictType type = stringToDistrictType(((RadioButton) types.getSelectedToggle()).getText());

        SuccessCode code = this.submitBuildDistrict(this.selectedVertex, type);
        if(code == SuccessCode.INSUFFICIENT_RESOURCES){
            tooltip.setText(messages.getString("buildDistrictInsufficientResources"));
        } else if (code == SuccessCode.INVALID_PLACEMENT){
            tooltip.setText(messages.getString("buildDistrictInnvalidPlacement"));
        } else {
            close();
        }
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

    @FXML
    public void close() {
        domainController.setState(GameState.DEFAULT);
        guiController.guiState = CatanGUIController.GUIState.IDLE;
        this.guiController.notifyOfPopupClose(this);
    }

    @Override
    protected void closeStage() {
        Stage stage = (Stage) sawmill.getScene().getWindow();
        stage.close();
    }
}
