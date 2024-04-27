package presentation.popups;

import domain.controller.Controller;
import presentation.CatanGUIController;

import java.util.ResourceBundle;

public abstract class Popup {

    protected CatanGUIController guiController;
    protected Controller domainController;
    protected ResourceBundle messages;

    public void setControllers(CatanGUIController guiController, Controller domainController) {
        this.guiController = guiController;
        this.domainController = domainController;
        setupStateData(); // delegate to optional hook method
    }
    protected void setupStateData() {
        // Do nothing by default, can and should be overridden
        // if you need to query the controllers in the concrete popups
        // to initialize state data
    }

    public void setMessages(ResourceBundle messages) {
        this.messages = messages;
        internationalize();
    }
    protected abstract void internationalize();

    public void close() {
        closeStage();
        guiController.notifyOfPopupClose(this);
    }
    protected abstract void closeStage();
}
