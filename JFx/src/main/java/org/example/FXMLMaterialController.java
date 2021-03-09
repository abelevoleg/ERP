package org.example;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.DoubleStringConverter;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class FXMLMaterialController implements Initializable {
    private ObservableList<Material> materialList = FXCollections.observableArrayList();

    @FXML
    public TableView <Material> tableMaterial;

    @FXML
    public TableColumn nameMaterialColumn;

    @FXML
    public TableColumn descriptionMaterialColumn;

    @FXML
    public TableColumn quantityMaterialColumn;

    @FXML
    public TextField materialDescription;

    @FXML
    public TextField name;

    @FXML
    public TextField quantity;

    @FXML
    public Button putMaterial;

    @FXML
    public Button deleteMaterial;

    @FXML
    public Button newMaterialInBase;

    @FXML
    public Button saveMaterialInBase;

    @FXML
    public TextField newName;

    @FXML
    public TextField newMaterialDescription;

    @FXML
    public TextField newQuantityInBase;

    @FXML
    public Button enterpriseSet;

    @FXML
    public void choiceMaterial(MouseEvent mouseEvent) {
        Material selectedMaterial = tableMaterial.getSelectionModel().getSelectedItem();
        name.setText(selectedMaterial.getName());
        materialDescription.setText(selectedMaterial.getMaterialDescription());
        quantity.setText(String.valueOf(selectedMaterial.getQuantity()));

        putMaterial.setDisable(false);
        deleteMaterial.setDisable(false);
    }

    @FXML
    public void putMaterial(ActionEvent actionEvent) {
        try {
            Integer.parseInt(quantity.getText());
        } catch (NumberFormatException e) {
            quantity.setStyle("-fx-text-inner-color:Red;");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Количество листов должно быть целым числом!");
            alert.showAndWait();
            return;
        }
        Material changedMaterial = new Material(name.getText(), materialDescription.getText(), Integer.parseInt(quantity.getText()));
        int index = tableMaterial.getSelectionModel().getFocusedIndex();

        materialList.set(index, changedMaterial);
        Collections.sort(materialList);
        quantity.setStyle("-fx-text-inner-color:Black;");
        name.clear();
        materialDescription.clear();
        quantity.clear();
        putMaterial.setDisable(true);
        deleteMaterial.setDisable(true);
    }

    @FXML
    public void deleteMaterial(ActionEvent actionEvent) {
        int index = tableMaterial.getSelectionModel().getFocusedIndex();

        materialList.remove(index);
        name.clear();
        materialDescription.clear();
        quantity.clear();
        putMaterial.setDisable(true);
        deleteMaterial.setDisable(true);
    }

    @FXML
    public void setEnterpriseMode(ActionEvent actionEvent) throws IOException {
        MainApp.changeRoot("primary");
    }

    @FXML
    public void newMaterialInBase(ActionEvent actionEvent) {
        newName.setDisable(false);
        newMaterialDescription.setDisable(false);
        newQuantityInBase.setDisable(false);
        saveMaterialInBase.setDisable(false);
    }

    @FXML
    public void saveMaterialInBase(ActionEvent actionEvent) {
        try {
            Integer.parseInt(newQuantityInBase.getText());
        } catch (NumberFormatException e) {
            newQuantityInBase.setStyle("-fx-text-inner-color:Red;");
            newQuantityInBase.setText("0");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Количество листов должно быть целым числом!");
            alert.showAndWait();
            return;
        }
        Material material = new Material(newName.getText(), newMaterialDescription.getText(), Integer.parseInt(newQuantityInBase.getText()));
        materialList.add(material);
        Collections.sort(materialList);
        newQuantityInBase.setStyle("-fx-text-inner-color:Black;");
        newName.clear();
        newMaterialDescription.clear();
        newQuantityInBase.clear();

        newName.setDisable(true);
        newMaterialDescription.setDisable(true);
        newQuantityInBase.setDisable(true);
        saveMaterialInBase.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initData();
        nameMaterialColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("name"));
        descriptionMaterialColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("materialDescription"));
        quantityMaterialColumn.setCellValueFactory(new PropertyValueFactory<Order, Integer>("quantity"));
        tableMaterial.setItems(materialList);
    }

    private void initData() {
        Material material = new Material("МДФ 22мм односторонний", "односторонняя ламинация", 3);
        Material material1 = new Material("МДФ 19мм односторонний", "односторонняя ламинация", 15);
        Material material2 = new Material("МДФ 16мм односторонний", "односторонняя ламинация", 10);
        Material material3 = new Material("МДФ 25мм односторонний", "односторонняя ламинация", 7);
        Material material4 = new Material("МДФ 30мм", "без ламинации", 5);
        materialList.addAll(material, material1, material2, material3, material4);
        Collections.sort(materialList);
    }
}
