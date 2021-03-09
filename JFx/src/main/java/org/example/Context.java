package org.example;

public class Context {
    private final static Context instance = new Context();
    public static Context getInstance() {
        return instance;
    }

    private FXMLMaterialController fxmlMaterialController;
    public void setFXMLController(FXMLMaterialController fxmlMaterialController) {
        this.fxmlMaterialController = fxmlMaterialController;
    }

    public FXMLMaterialController getFXMLMaterialController() {
        return fxmlMaterialController;
    }
}

