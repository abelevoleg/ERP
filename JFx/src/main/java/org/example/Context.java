package org.example;

public class Context {
    private final static Context instance = new Context();
    public static Context getInstance() {
        return instance;
    }

    private FXMLController fxmlController;
    private FXMLMaterialController fxmlMaterialController;

    public void setFXMLController(FXMLController fxmlController) {
        this.fxmlController = fxmlController;
    }

    public FXMLController getFXMLController() {
        return fxmlController;
    }

    public void setFXMLMaterialController(FXMLMaterialController fxmlMaterialController) {
        this.fxmlMaterialController = fxmlMaterialController;
    }

    public FXMLMaterialController getFXMLMaterialController() {
        return fxmlMaterialController;
    }
}

