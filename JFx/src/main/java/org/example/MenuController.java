package org.example;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;

public class MenuController {
    static File file;

    static void save(){
        if (file != null) {
            FileOutputStream outputStream = null;
            FXMLController cont = Context.getInstance().getFXMLController();
            FXMLMaterialController materialCont = Context.getInstance().getFXMLMaterialController();
            FXMLArchiveController archiveCont = Context.getInstance().getFXMLArchiveController();
            DataForSerialization d = new DataForSerialization(new ArrayList<Order>(cont.orderList),
                    new ArrayList<Material>(materialCont.materialList), new ArrayList<Order>(archiveCont.orderArchiveList));
            try {
                outputStream = new FileOutputStream(file);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(d);
                objectOutputStream.close();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось сохранить файл!");
                alert.showAndWait();
                e.printStackTrace();
            }
        } else saveToFile();
    }

    static void saveToFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить");
        fileChooser.setInitialFileName("шкафулькин");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
        fileChooser.getExtensionFilters()
                .addAll(
                        new FileChooser.ExtensionFilter("shkafulkin files (*.shkaf)", "*.shkaf")
                );
        file = fileChooser.showSaveDialog(MainApp.stage);

        if (file != null) {
            FileOutputStream outputStream = null;
            FXMLController cont = Context.getInstance().getFXMLController();
            FXMLMaterialController materialCont = Context.getInstance().getFXMLMaterialController();
            FXMLArchiveController archiveCont = Context.getInstance().getFXMLArchiveController();
            DataForSerialization d = new DataForSerialization(new ArrayList<Order>(cont.orderList),
                    new ArrayList<Material>(materialCont.materialList), new ArrayList<Order>(archiveCont.orderArchiveList));
            try {
                outputStream = new FileOutputStream(file);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(d);
                objectOutputStream.close();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось сохранить файл!");
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    static void openFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Открыть");
        fileChooser.setInitialFileName("шкафулькин");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
        fileChooser.getExtensionFilters()
                .addAll(
                        new FileChooser.ExtensionFilter("shkafulkin files (*.shkaf)", "*.shkaf")
                );
        file = fileChooser.showOpenDialog(MainApp.stage);

        openFromFile(file);
    }

    static void openFromFile(File file) {
        if (file != null) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                DataForSerialization d = (DataForSerialization) objectInputStream.readObject();

                FXMLController cont = Context.getInstance().getFXMLController();
                cont.orderList.clear();
                cont.orderList.addAll(d.getOrderList());
                cont.histogramLoad(cont.orderList);
                cont.tableOrder.refresh();
                Context.getInstance().setFXMLController(cont);

                FXMLMaterialController materialCont = Context.getInstance().getFXMLMaterialController();
                materialCont.materialList.clear();
                materialCont.materialList.addAll(d.getMaterialList());
                materialCont.tableMaterial.refresh();
                Context.getInstance().setFXMLMaterialController(materialCont);

                FXMLArchiveController archiveCont = Context.getInstance().getFXMLArchiveController();
                archiveCont.orderArchiveList.clear();
                archiveCont.orderArchiveList.addAll(d.getOrderArchiveList());
                archiveCont.tableArchiveOrder.refresh();
                Context.getInstance().setFXMLArchiveController(archiveCont);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть файл!");
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }
}
