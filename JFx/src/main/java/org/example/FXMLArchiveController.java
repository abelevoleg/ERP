package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FXMLArchiveController implements Initializable {
    ObservableList<Order> orderArchiveList = FXCollections.observableArrayList();
    ObservableList<MaterialDataForOrder> materialListInArchiveOrder = FXCollections.observableArrayList();

    @FXML
    private MenuItem save;

    @FXML
    private MenuItem saveToFile;

    @FXML
    public MenuItem open;

    // список заказов
    @FXML
    TableView<Order> tableArchiveOrder;

    // список материалов в выбранном заказе
    @FXML
    private TableView<MaterialDataForOrder> tableArchiveMaterialOrder;

    // колонка даты в списке заказов
    @FXML
    private TableColumn<Order, String> dateArchiveOrderColumn;

    // колонка номера заказа в списке заказов
    @FXML
    private TableColumn<Order, String> numberArchiveOrderColumn;

    // колонка статуса заказа в списке заказов
    @FXML
    private TableColumn<Order, String> descriptionArchiveOrderColumn;

    // колонка названия материала в списке
    @FXML
    private TableColumn<MaterialDataForOrder,String> materialArchiveColumn;

    // количество материала в списке
    @FXML
    private TableColumn<MaterialDataForOrder, Double> quantityArchiveColumn;

    @FXML
    private Button findOrder;

    @FXML
    private Button deleteOrder;

    @FXML
    private Button enterprise;

    @FXML
    private Button warehouse;

    // поле ввода номера заказа для поиска
    @FXML
    private TextField numberToFind;

    // поля данных просмотра заказа
    @FXML
    private TextField date;

    @FXML
    private TextField number;

    @FXML
    private TextField description;


    // выбор заказа в таблице заказов
    @FXML
    private void choiceOrder(MouseEvent event) {
        Order selectedOrder = tableArchiveOrder.getSelectionModel().getSelectedItem();
        number.setText(selectedOrder.getNumber());
        date.setText(selectedOrder.getDate());
        description.setText(selectedOrder.getDescription());
        materialListInArchiveOrder.clear();
        materialListInArchiveOrder.addAll(selectedOrder.getMaterialList());
        deleteOrder.setDisable(false);
    }

    // поиск заказа в таблице заказов
    @FXML
    private void findOrder(ActionEvent event) throws IOException {
        String findNumber = numberToFind.getText();
        if (!findOrder(findNumber)) {
            FXMLController cont = Context.getInstance().getFXMLController();
            if (cont.findOrder(findNumber)) {
                MainApp.changeRoot("primary");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Заказ не найден!");
                alert.showAndWait();
            }
        }
    }

    boolean findOrder(String findNumber) {
        for (Order order : orderArchiveList){
            if (findNumber.equals(order.getNumber())){
                Order selectedOrder;
                selectedOrder = order;
                tableArchiveOrder.requestFocus();
                tableArchiveOrder.getFocusModel().focus(orderArchiveList.indexOf(selectedOrder));
                tableArchiveOrder.scrollTo(selectedOrder);
                number.setText(selectedOrder.getNumber());
                date.setText(selectedOrder.getDate());
                description.setText(selectedOrder.getDescription());
                materialListInArchiveOrder.clear();
                materialListInArchiveOrder.addAll(selectedOrder.getMaterialList());
                deleteOrder.setDisable(false);
                return true;
            }
        }
        return false;
    }

    // удаление заказа
    @FXML
    private void deleteOrder(ActionEvent event) {
        int index = tableArchiveOrder.getSelectionModel().getFocusedIndex();
        orderArchiveList.remove(index);
        materialListInArchiveOrder.clear();
        date.clear();
        number.clear();
        description.clear();
        deleteOrder.setDisable(true);
    }

    // сохранение состояния таблиц заказов и материалов в текущий файл
    @FXML
    void save(){
        MenuController.save();
    }

    // сохранение состояния таблиц заказов и материалов в новый файл
    @FXML
    void saveToFile(){
        MenuController.saveToFile();
    }

    // загрузка состояния таблиц заказов и материалов из файла
    @FXML
    void openFromFile(){
        MenuController.openFromFile();
    }

    @FXML
    private void setEnterpriseMode(ActionEvent actionEvent) throws IOException {
        MainApp.changeRoot("primary");
    }

    @FXML
    private void setWarehouseMode(ActionEvent actionEvent) throws IOException {
        MainApp.changeRoot("materialMode");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dateArchiveOrderColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("date"));
        numberArchiveOrderColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("number"));
        descriptionArchiveOrderColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("description"));
        tableArchiveOrder.setItems(orderArchiveList);

        materialArchiveColumn.setCellValueFactory(new PropertyValueFactory<>("materialName"));
        quantityArchiveColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tableArchiveMaterialOrder.setItems(materialListInArchiveOrder);

        Context.getInstance().setFXMLArchiveController(this);
    }

}
