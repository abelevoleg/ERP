package org.example;

import java.net.URL;
import java.text.ParseException;
import java.util.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.DoubleStringConverter;


public class FXMLController implements Initializable {
    private ObservableList<Order> orderList = FXCollections.observableArrayList();
    private ObservableList<Material> materialList = FXCollections.observableArrayList();
    private ObservableList<MaterialDataForOrder> materialListForOrder = FXCollections.observableArrayList();
    private ObservableList<MaterialDataForOrder> materialListInOrder = FXCollections.observableArrayList();
    private ObservableList<Order.StatusOfOrder> statusList = FXCollections.observableArrayList(List.of(Order.StatusOfOrder.values()));

    @FXML
    private BarChart<String, Number> orderInWork;

    @FXML
    private CategoryAxis statusAxe;

    @FXML
    private NumberAxis quantityAxe;

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
    private Button findOrder;

    @FXML
    private Button deleteOrder;

    @FXML
    private Button saveMaterial;

    // поле ввода номера заказа для поиска
    @FXML
    private TextField numberToFind;

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
        try {
            Double.parseDouble(newQuantity.getText());
        } catch (Exception e) {
            newQuantity.setStyle("-fx-text-inner-color:Red;");
            newQuantity.setText("*.*");
            return;
        }
        MaterialDataForOrder materialDataForOrder = new MaterialDataForOrder(newMaterial.getText(), Double.parseDouble(newQuantity.getText()));
        materialListForOrder.add(materialDataForOrder);
        newMaterial.clear();
        newQuantity.clear();
        newQuantity.setStyle("-fx-text-inner-color:Black;");
    }

    // изменение материала в таблице материалов нового заказа
    @FXML
    private void changeNewMaterial(TableColumn.CellEditEvent<MaterialDataForOrder, String> event) {
        event.getTableView().getItems().get(event.getTablePosition().getRow()).setMaterialName(event.getNewValue());
    }

    // изменение количества материала в таблице материалов нового заказа
    @FXML
    private void changeNewQuantity(TableColumn.CellEditEvent<MaterialDataForOrder, Double> event) {
        try {
            event.getNewValue();
        } catch (NumberFormatException e) {
            return;
        }
        event.getTableView().getItems().get(event.getTablePosition().getRow()).setQuantity(event.getNewValue());
    }

    // удаление выбранного материала в таблице материалов нового заказа
    @FXML
    public void deleteNewMaterial(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DELETE) {
            int index = tableMaterialNewOrder.getSelectionModel().getFocusedIndex();
            materialListForOrder.remove(index);
        }
    }

    // сохранение нового заказа в таблице заказов
    @FXML
    private void saveOrder(ActionEvent event) {
        try {
            Order.dateformatddMMyyyy.parse(newDate.getText());
        } catch (ParseException e) {
            newDate.setStyle("-fx-text-inner-color:Red;");
            newDate.setText("Формат даты dd.mm.yyyy");
            return;
        }
        List<MaterialDataForOrder> materialListForSaveOrder = new ArrayList<>();
        materialListForSaveOrder.addAll(materialListForOrder);
        Order order = new Order(newDate.getText(), newNumber.getText(), materialListForSaveOrder, newDescription.getText());
        orderList.add(order);
        Collections.sort(orderList);
        newDate.setStyle("-fx-text-inner-color:Black;");
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

    // поиск заказа в таблице заказов
    @FXML
    private void findOrder(ActionEvent event) {
        String findNumber = numberToFind.getText();
        for (Order order : orderList){
            if (findNumber.equals(order.getNumber())){
                Order selectedOrder = order;
                tableOrder.requestFocus();
                tableOrder.getFocusModel().focus(orderList.indexOf(selectedOrder));
                tableOrder.scrollTo(selectedOrder);
                number.setText(selectedOrder.getNumber());
                date.setText(selectedOrder.getDate());
                description.setText(selectedOrder.getDescription());
                status.setValue(selectedOrder.getStatus());
                materialListInOrder.clear();
                materialListInOrder.addAll(selectedOrder.getMaterialList());
                putOrder.setDisable(false);
                deleteOrder.setDisable(false);
                return;
            }
        }
        numberToFind.setText(findNumber + " не найден");
    }

    // изменение материала в таблице материалов выбранного заказа
    @FXML
    private void changeMaterial(TableColumn.CellEditEvent<MaterialDataForOrder, String> event) {
        event.getTableView().getItems().get(event.getTablePosition().getRow()).setMaterialName(event.getNewValue());
    }

    // изменение количества материала в таблице материалов выбранного заказа
    @FXML
    private void changeQuantity(TableColumn.CellEditEvent<MaterialDataForOrder, Double> event) {
        try {
            event.getNewValue();
        } catch (NumberFormatException e) {
            return;
        }
        event.getTableView().getItems().get(event.getTablePosition().getRow()).setQuantity(event.getNewValue());
    }

    // удаление выбранного материала в таблице материалов выбранного заказа
    @FXML
    public void deleteMaterial(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DELETE) {
            int index = tableMaterialOrder.getSelectionModel().getFocusedIndex();
            materialListInOrder.remove(index);
        }
    }

    // сохранение измененного заказа
    @FXML
    private void putOrder(ActionEvent event) {
        try {
            Order.dateformatddMMyyyy.parse(date.getText());
        } catch (ParseException e) {
            date.setStyle("-fx-text-inner-color:Red;");
            return;
        }
        List<MaterialDataForOrder> materialListForPutOrder = new ArrayList<>();
        materialListForPutOrder.addAll(materialListInOrder);
        Order changedOrder = new Order(date.getText(), number.getText(), materialListForPutOrder, description.getText());
        changedOrder.setStatusOfOrder(status.getValue());
        int index = tableOrder.getSelectionModel().getFocusedIndex();
        orderList.set(index, changedOrder);
        Collections.sort(orderList);
        materialListInOrder.clear();
        date.setStyle("-fx-text-inner-color:Black;");
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
        materialNewColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        quantityNewColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityNewColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tableMaterialNewOrder.setItems(materialListForOrder);

        materialColumn.setCellValueFactory(new PropertyValueFactory<>("materialName"));
        materialColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        tableMaterialOrder.setItems(materialListInOrder);

        status.setItems(statusList);

        String[] valuesStatus = Arrays.stream(Order.StatusOfOrder.values()).map( value -> value.getStatus()).toArray( String[]::new );
        statusAxe.setCategories(FXCollections.observableArrayList(valuesStatus));
        orderInWork = new BarChart<>(statusAxe, quantityAxe);
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.getData().add(new XYChart.Data<>("Не выдан", 15.0));
        orderInWork.getData().addAll(series1);
    }

    private void initData() {
        ObservableList<MaterialDataForOrder> materialOrderList = FXCollections.observableArrayList();
        materialOrderList.add(new MaterialDataForOrder("МДФ 19мм", 1.5));
        materialOrderList.add(new MaterialDataForOrder("МДФ 16мм", 2.5));
        Order order = new Order("11.03.2021", "1122", materialOrderList, "покраска");
        orderList.add(order);
    }
}
