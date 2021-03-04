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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.DoubleStringConverter;


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
    private Button deleteOrder;

    @FXML
    private Button saveMaterial;

    // поля ввода данных нового материала
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

    // поля данных просмотра и редактирования заказа
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

    // сохранение данных нового материала в таблице в новом заказе
    @FXML
    private void saveMaterial(ActionEvent event) {
        MaterialDataForOrder materialDataForOrder = new MaterialDataForOrder(newMaterial.getText(), Double.parseDouble(newQuantity.getText()));
        materialListForOrder.add(materialDataForOrder);
        newMaterial.clear();
        newQuantity.clear();
    }

    // сохранение нового заказа в таблице заказов
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

    // выбор заказа в таблице заказов
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
        deleteOrder.setDisable(false);
    }

    // изменение материала в таблице материалов выбранного заказа
    @FXML
    private void changeMaterial(TableColumn.CellEditEvent<MaterialDataForOrder, String> event) {
        event.getTableView().getItems().get(event.getTablePosition().getRow()).setMaterialName(event.getNewValue());
    }

    // изменение количества материала в таблице материалов выбранного заказа
    @FXML
    private void changeQuantity(TableColumn.CellEditEvent<MaterialDataForOrder, Double> event) {
        event.getTableView().getItems().get(event.getTablePosition().getRow()).setQuantity(event.getNewValue());
    }

    // сохранение измененного заказа
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
        deleteOrder.setDisable(true);
    }

    // удаление заказа
    @FXML
    private void deleteOrder(ActionEvent event) {
        int index = tableOrder.getSelectionModel().getFocusedIndex();
        orderList.remove(index);
        materialListInOrder.clear();
        date.clear();
        number.clear();
        description.clear();
        status.setValue(null);
        putOrder.setDisable(true);
        deleteOrder.setDisable(true);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        initData();
        dateOrderColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("date"));
        numberOrderColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("number"));
        statusOrderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatusText()));
        tableOrder.setItems(orderList);

        materialNewColumn.setCellValueFactory(new PropertyValueFactory<>("materialName"));
        quantityNewColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tableMaterialNewOrder.setItems(materialListForOrder);

        materialColumn.setCellValueFactory(new PropertyValueFactory<>("materialName"));
        materialColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
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
