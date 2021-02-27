package org.example;

import java.net.URL;
import java.util.*;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;

public class FXMLController implements Initializable {
    private ObservableList<Order> orderList = FXCollections.observableArrayList();
    private ObservableList<Material> materialList = FXCollections.observableArrayList();
    private ObservableList<MaterialDataForOrder> materialListForOrder = FXCollections.observableArrayList();

    @FXML
    private Label lblOut;

    @FXML
    private TableView<Order> tableOrder;

    @FXML
    private TableView<MaterialDataForOrder> tableMaterialNewOrder;

    @FXML
    private TableColumn<Order, String> dateOrderColumn;

    @FXML
    private TableColumn<Order, String> numberOrderColumn;

    @FXML
    private TableColumn<Order, String> statusOrderColumn;

    @FXML
    private TableColumn<MaterialDataForOrder,String> materialNewColumn;

    @FXML
    private TableColumn<MaterialDataForOrder, Double> quantityNewColumn;

    @FXML
    private Button saveOrder;

    @FXML
    private Button saveMaterial;

    @FXML
    private TextField newDate;

    @FXML
    private TextField newNumber;

    @FXML
    private TextField newDescription;

    @FXML
    private TextField newMaterial;

    @FXML
    private TextField newQuantity;

    public FXMLController() {
    }

    @FXML
    private void newOrder(ActionEvent event) {
        newDate.setDisable(false);
        newNumber.setDisable(false);
        newDescription.setDisable(false);
        newMaterial.setDisable(false);
        newQuantity.setDisable(false);
        saveOrder.setDisable(false);
        saveMaterial.setDisable(false);
        tableMaterialNewOrder.setDisable(false);
    }

    @FXML
    private void saveMaterial(ActionEvent event) {
        MaterialDataForOrder materialDataForOrder = new MaterialDataForOrder(newMaterial.getText(), Double.parseDouble(newQuantity.getText()));
        materialListForOrder.add(materialDataForOrder);
    }

    @FXML
    private void saveOrder(ActionEvent event) {
        Order order = new Order(newDate.getText(), newNumber.getText(), null, newDescription.getText());
        orderList.add(order);
        newDate.clear();
        newNumber.clear();
        newDescription.clear();
        newMaterial.clear();
        newQuantity.clear();

        materialListForOrder.clear();
        
        newDate.setDisable(true);
        newNumber.setDisable(true);
        newDescription.setDisable(true);
        newMaterial.setDisable(true);
        newQuantity.setDisable(true);
        saveOrder.setDisable(true);
        saveMaterial.setDisable(true);
        tableMaterialNewOrder.setDisable(true);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        initData();
        dateOrderColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("date"));
        numberOrderColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("number"));
        statusOrderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        tableOrder.setItems(orderList);

        materialNewColumn.setCellValueFactory(new PropertyValueFactory<MaterialDataForOrder, String>("materialName"));
        quantityNewColumn.setCellValueFactory(new PropertyValueFactory<MaterialDataForOrder, Double>("quantity"));

        tableMaterialNewOrder.setItems(materialListForOrder);
    }

    private void initData() {
        HashMap<Material, Double> orderMap = new HashMap<>();
        orderMap.put(new Material("МДФ 19мм", "односторонний"), 1.5);
        orderMap.put(new Material("МДФ 16мм", "односторонний"), 2.5);
        Order order = new Order("11.03.2021", "1122", orderMap, "покраска");
        orderList.add(order);
    }
}
