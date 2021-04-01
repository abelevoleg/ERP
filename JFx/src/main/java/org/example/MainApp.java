package org.example;

/*
desktop ERP for average furniture manufacture
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public class MainApp extends Application {
    static Stage stage;
    private static Scene scene;
    private static Scene sceneMaterial;
    private static Scene sceneArchive;

    @Override
    public void start(@SuppressWarnings("exports") Stage s) throws IOException {
        stage=s;
        setRoot("primary", "materialMode", "archiveMode", "ERP Шкафулькин");
        if (MenuController.file != null){
            MenuController.openFromFile(MenuController.file);
        }
    }

    static void setRoot(String fxml, String materialModeFxml) throws IOException {
        setRoot(fxml,stage.getTitle());
    }

    static void setRoot(String fxml, String materialModeFxml, String archiveModeFxml, String title) throws IOException {
        scene = new Scene(loadFXML(fxml));
        sceneMaterial = new Scene(loadFXML(materialModeFxml));
        sceneArchive = new Scene(loadFXML(archiveModeFxml));
        stage.setTitle(title);
        stage.setScene(scene);
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
            case "archiveMode":
                stage.setScene(sceneArchive);
                break;
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/fxml/"+fxml + ".fxml"));
        return fxmlLoader.load();
    }


    public static void main(String[] args) {
        if (args.length != 0){
            MenuController.file = new File(args[0]);
        }
        launch(args);
    }

}
