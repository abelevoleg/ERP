package org.example;

/*
desktop ERP for average furniture manufacture
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class MainApp extends Application {
    private static Stage stage;
    private static Scene scene;
    private static Scene sceneMaterial;

    @Override
    public void start(@SuppressWarnings("exports") Stage s) throws IOException {
        stage=s;
        setRoot("primary", "materialMode", "ERP Шкафулькин");
    }

    static void setRoot(String fxml, String materialModeFxml) throws IOException {
        setRoot(fxml,stage.getTitle());
    }

    static void setRoot(String fxml, String materialModeFxml, String title) throws IOException {
        scene = new Scene(loadFXML(fxml));
        sceneMaterial = new Scene(loadFXML(materialModeFxml));
        stage.setTitle(title);
        stage.setScene(scene);
//        stage.getScene().getStylesheets().add("fxml/styles.css");
        stage.show();
    }

    static void changeRoot(String fxml) throws IOException {
        switch (fxml){
            case "primary":
                stage.setScene(scene);
                break;
            case "materialMode":
                stage.setScene(sceneMaterial);
                break;
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/fxml/"+fxml + ".fxml"));
        return fxmlLoader.load();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
