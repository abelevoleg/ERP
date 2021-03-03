package org.example;

import java.net.URL;
import java.util.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;


public class FXMLController implements Initializable {
    private ObservableList<Order> orderList = FXCollections.observableArrayList();
    private ObservableList<Material> materialList = FXCollections.observableArrayList();
    private ObservableList<MaterialDataForOrder> materialListForOrder = FXCollections.observableArrayList();
    private ObservableList<MaterialDataForOrder> materialListInOrder = FXCollections.observableArrayList();
    private ObservableList<Order.StatusOfOrder> statusList = FXCollections.observableArrayList(List.of(Order.StatusOfOrder.values()));

    @FXML
    private Label lblOut;

    // список заказов
    @FXML
    private TableView<Order> tableOrder;

    // список материалов для создания нового заказа
    @FXML
    private TableView<MaterialDataForOrder> tableMaterialNewOrder;

    // список материалов в выбранном заказе
    @FXML
    private TableView<MaterialDataForOrder> tableMaterialOrder;

    // колонка даты в списке заказов
    @FXML
    private TableColumn<Order, String> dateOrderColumn;

    // колонка номера заказа в списке заказов
    @FXML
    private TableColumn<Order, String> numberOrderColumn;

    // колонка статуса заказа в списке заказов
    @FXML
    private TableColumn<Order, String> statusOrderColumn;

    // колонка названия материала в списке для создания нового заказа
    @FXML
    private TableColumn<MaterialDataForOrder,String> materialNewColumn;

    // количество материала в списке для создания нового заказа
    @FXML
    private TableColumn<MaterialDataForOrder, Double> quantityNewColumn;

    // колонка названия материала в списке для создания нового заказа
    @FXML
    private TableColumn<MaterialDataForOrder,String> materialColumn;

    // количество материала в списке для создания нового заказа
    @FXML
    private TableColumn<MaterialDataForOrder, Double> quantityColumn;

    @FXML
    private Button saveOrder;

    @FXML
    private Button putOrder;

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

    @FXML
    private TextField date;

    @FXML
    private TextField number;

    @FXML
    private TextField description;

    @FXML
    private ChoiceBox<Order.StatusOfOrder> status;


    public FXMLController() {
    }

    // активация полей ввода при нажатии кнопки нового заказа
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
        List<MaterialDataForOrder> materialListForSaveOrder = new ArrayList<>();
        materialListForSaveOrder.addAll(materialListForOrder);
        Order order = new Order(newDate.getText(), newNumber.getText(), materialListForSaveOrder, newDescription.getText());
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

    @FXML
    private void choiceOrder(MouseEvent event) {
        Order selectedOrder = tableOrder.getSelectionModel().getSelectedItem();
        number.setText(selectedOrder.getNumber());
        date.setText(selectedOrder.getDate());
        description.setText(selectedOrder.getDescription());
        status.setValue(selectedOrder.getStatus());
        materialListInOrder.clear();
        materialListInOrder.addAll(selectedOrder.getMaterialList());
        putOrder.setDisable(false);
    }

    @FXML
    private void putOrder(ActionEvent event) {
        List<MaterialDataForOrder> materialListForPutOrder = new ArrayList<>();
        materialListForPutOrder.addAll(materialListInOrder);
        Order changedOrder = new Order(date.getText(), number.getText(), materialListForPutOrder, description.getText());
        changedOrder.setStatusOfOrder(status.getValue());
        int index = tableOrder.getSelectionModel().getFocusedIndex();
        orderList.set(index, changedOrder);
        materialListInOrder.clear();
        date.clear();
        number.clear();
        description.clear();
        status.setValue(null);
        putOrder.setDisable(true);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        initData();
        dateOrderColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("date"));
        numberOrderColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("number"));
        statusOrderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatusText()));
        tableOrder.setItems(orderList);

        materialNewColumn.setCellValueFactory(new PropertyValueFactory<MaterialDataForOrder, String>("materialName"));
        quantityNewColumn.setCellValueFactory(new PropertyValueFactory<MaterialDataForOrder, Double>("quantity"));
        tableMaterialNewOrder.setItems(materialListForOrder);

        materialColumn.setCellValueFactory(new PropertyValueFactory<MaterialDataForOrder, String>("materialName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<MaterialDataForOrder, Double>("quantity"));
        tableMaterialOrder.setItems(materialListInOrder);

        status.setItems(statusList);
    }

    private void initData() {
        ObservableList<MaterialDataForOrder> materialOrderList = FXCollections.observableArrayList();
        materialOrderList.add(new MaterialDataForOrder("МДФ 19мм", 1.5));
        materialOrderList.add(new MaterialDataForOrder("МДФ 16мм", 2.5));
        Order order = new Order("11.03.2021", "1122", materialOrderList, "покраска");
        orderList.add(order);
    }
}
