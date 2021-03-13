package org.example;

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
            DataForSerialization d = new DataForSerialization(new ArrayList<Order>(cont.orderList), new ArrayList<Material>(materialCont.materialList));
            try {
                outputStream = new FileOutputStream(file);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(d);
                objectOutputStream.close();
            } catch (Exception e) {
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
            DataForSerialization d = new DataForSerialization(new ArrayList<Order>(cont.orderList), new ArrayList<Material>(materialCont.materialList));
            try {
                outputStream = new FileOutputStream(file);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(d);
                objectOutputStream.close();
            } catch (Exception e) {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
