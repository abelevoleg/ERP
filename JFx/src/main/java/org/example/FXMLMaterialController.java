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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FXMLMaterialController implements Initializable {
    ObservableList<Material> materialList = FXCollections.observableArrayList();
    ObservableList<LackMaterialData> lackMaterialList = FXCollections.observableArrayList();

    @FXML
    private MenuItem save;

    @FXML
    private MenuItem saveToFile;

    @FXML
    public MenuItem open;

    @FXML
    TableView <Material> tableMaterial;

    @FXML
    private TableView<LackMaterialData> tableMaterialLack;

    @FXML
    private TableColumn<Material, String> nameMaterialColumn;

    @FXML
    private TableColumn<Material, String> descriptionMaterialColumn;

    @FXML
    private TableColumn<Material, Integer> quantityMaterialColumn;

    @FXML
    private TableColumn<LackMaterialData, String> lackMaterialColumn;

    @FXML
    private TableColumn<LackMaterialData, Integer> lackQuantityColumn;

    @FXML
    private TableColumn<LackMaterialData, String> lackMaterialOrderNumber;

    @FXML
    private TextField materialDescription;

    @FXML
    private TextField name;

    @FXML
    private TextField quantity;

    @FXML
    private TextField quantityForCheck;

    @FXML
    private Button checkOrders;

    @FXML
    private TextField numberForCheck;

    @FXML
    private Button checkNumberOrder;

    @FXML
    private Button putMaterial;

    @FXML
    private Button deleteMaterial;

    @FXML
    private Button newMaterialInBase;

    @FXML
    private Button saveMaterialInBase;

    @FXML
    private TextField newName;

    @FXML
    private TextField newMaterialDescription;

    @FXML
    private TextField newQuantityInBase;

    @FXML
    private Button enterpriseSet;

    @FXML
    private Button archiveSet;

    @FXML
    private void choiceMaterial(MouseEvent mouseEvent) {
        Material selectedMaterial = tableMaterial.getSelectionModel().getSelectedItem();
        name.setText(selectedMaterial.getName());
        materialDescription.setText(selectedMaterial.getMaterialDescription());
        quantity.setText(String.valueOf(selectedMaterial.getQuantity()));

        putMaterial.setDisable(false);
        deleteMaterial.setDisable(false);
    }

    @FXML
    private void putMaterial(ActionEvent actionEvent) {
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
    private void deleteMaterial(ActionEvent actionEvent) {
        int index = tableMaterial.getSelectionModel().getFocusedIndex();

        materialList.remove(index);
        name.clear();
        materialDescription.clear();
        quantity.clear();
        putMaterial.setDisable(true);
        deleteMaterial.setDisable(true);
    }

    @FXML
    private void setEnterpriseMode(ActionEvent actionEvent) throws IOException {
        MainApp.changeRoot("primary");
    }

    // переключение в режим архива заказов
    @FXML
    public void setArchiveMode(ActionEvent actionEvent) throws IOException {
        MainApp.changeRoot("archiveMode");
    }

    @FXML
    private void newMaterialInBase(ActionEvent actionEvent) {
        newName.setDisable(false);
        newMaterialDescription.setDisable(false);
        newQuantityInBase.setDisable(false);
        saveMaterialInBase.setDisable(false);
    }

    @FXML
    private void saveMaterialInBase(ActionEvent actionEvent) {
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

    // проверка наличия материалов для заданного количества планируемых к выдаче в работу заказов
    @FXML
    private void checkMaterialForOrders(ActionEvent actionEvent) {
        lackMaterialList.clear();
        FXMLController controller = Context.getInstance().getFXMLController();
        int quantityOrders = Integer.parseInt(quantityForCheck.getText());
        List<Order> newOrders = controller.orderList.stream().filter(order -> order.getStatus() == Order.StatusOfOrder.NEW).collect(Collectors.toList());

        if (quantityOrders > newOrders.size()){
            quantityOrders = newOrders.size();
        }
        List<Order> checkOrderList = newOrders.subList(0, quantityOrders);

        for (Material material : materialList){
            int lackQuantity = material.getQuantity();
            String orders = "";
            for (Order checkOrder : checkOrderList){
                for (MaterialDataForOrder materialDataForOrder : checkOrder.getMaterialList()){
                    String materialName = materialDataForOrder.getMaterialName();
                    if (materialName.equals(material.getName())){
                        lackQuantity = lackQuantity - (int) (materialDataForOrder.getQuantity() / FXMLController.LISTAREA + 0.75);
                        if (lackQuantity < 0){
                            orders = orders + checkOrder.getNumber() + " ";
                        }
                    }
                }
            }
            if (lackQuantity < 0) {
                LackMaterialData lackMaterialData = new LackMaterialData(material.getName(), orders, lackQuantity);
                lackMaterialList.add(lackMaterialData);
            }
        }
        haveMaterial(checkOrderList);

        if (lackMaterialList.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Материал в наличии!");
            alert.showAndWait();
        } else Collections.sort(lackMaterialList);
    }

    // проверка существования материала в базе
    private void haveMaterial(List<Order> checkOrderList){
        for (Order order : checkOrderList){
            for (MaterialDataForOrder m : order.getMaterialList()){
                boolean haveMaterialInBase = false;
                String materialName = m.getMaterialName();
                for (Material material : materialList){
                    if (materialName.equals(material.getName())){
                        haveMaterialInBase = true;
                    }
                }
                if (!haveMaterialInBase){
                    LackMaterialData lackMaterialData = new LackMaterialData(materialName, order.getNumber(), -(int) (m.getQuantity() / FXMLController.LISTAREA + 0.75));
                    lackMaterialList.add(lackMaterialData);
                }
            }
        }
    }

    // проверка наличия материала по номеру заказа
    @FXML
    private void checkMaterialForNumberOrder(ActionEvent actionEvent) {
        Order checkOrder = null;
        lackMaterialList.clear();
        FXMLController controller = Context.getInstance().getFXMLController();
        List<Order> orderList = controller.orderList;
        for (Order order : orderList) {
            if (numberForCheck.getText().equals(order.getNumber())){
                checkOrder = order;
                break;
            }
        }

        if (checkOrder == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Заказ с таким номером не найден!");
            alert.showAndWait();
            return;
        }

        for (MaterialDataForOrder materialDataForOrder : checkOrder.getMaterialList()){
            String materialName = materialDataForOrder.getMaterialName();
            for (Material material : materialList){
                if (materialName.equals(material.getName())){
                    int lackQuantity = material.getQuantity() - (int) (materialDataForOrder.getQuantity() / FXMLController.LISTAREA + 0.75);
                    if (lackQuantity < 0){
                        LackMaterialData lackMaterialData = new LackMaterialData(materialName, checkOrder.getNumber(), lackQuantity);
                        lackMaterialList.add(lackMaterialData);
                    }
                    break;
                }
            }
        }

        haveMaterial(List.of(checkOrder));

        if (lackMaterialList.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Материал в наличии!");
            alert.showAndWait();
        } else Collections.sort(lackMaterialList);
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initData();
        nameMaterialColumn.setCellValueFactory(new PropertyValueFactory<Material, String>("name"));
        descriptionMaterialColumn.setCellValueFactory(new PropertyValueFactory<Material, String>("materialDescription"));
        quantityMaterialColumn.setCellValueFactory(new PropertyValueFactory<Material, Integer>("quantity"));
        tableMaterial.setItems(materialList);

        lackMaterialColumn.setCellValueFactory(new PropertyValueFactory<LackMaterialData, String>("materialName"));
        lackQuantityColumn.setCellValueFactory(new PropertyValueFactory<LackMaterialData, Integer>("lackQuantity"));
        lackMaterialOrderNumber.setCellValueFactory(new PropertyValueFactory<LackMaterialData, String>("orderNumber"));
        tableMaterialLack.setItems(lackMaterialList);

        Context.getInstance().setFXMLMaterialController(this);
    }

    private void initData() {
        Material material = new Material("МДФ 22мм односторонний", "односторонняя ламинация", 3);
        Material material1 = new Material("МДФ 19мм односторонний", "односторонняя ламинация", 15);
        Material material2 = new Material("МДФ 16мм односторонний", "односторонняя ламинация", 0);
        Material material3 = new Material("МДФ 25мм односторонний", "односторонняя ламинация", 7);
        Material material4 = new Material("МДФ 30мм", "без ламинации", 5);
        materialList.addAll(material, material1, material2, material3, material4);
        Collections.sort(materialList);
    }

}
