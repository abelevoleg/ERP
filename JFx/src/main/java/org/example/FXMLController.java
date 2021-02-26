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

public class FXMLController implements Initializable {
    private ObservableList<Order> orderList = FXCollections.observableArrayList();
    
    @FXML
    private Label lblOut;

    @FXML
    private TableView<Order> tableOrder;

    @FXML
    private TableColumn<Order, String> dateOrderColumn;

    @FXML
    private TableColumn<Order, String> numberOrderColumn;

    @FXML
    private TableColumn<Order, String> statusOrderColumn;

    @FXML
    private Button mainButton;

    @FXML
    private TextField newDate;

    @FXML
    private TextField newNumber;

    @FXML
    private TextField newDescription;
    
    @FXML
    private void btnClickAction(ActionEvent event) {
        HashMap<Material, Double> orderMap = new HashMap<>();
        orderMap.put(new Material("МДФ 19мм", "односторонний"), 1.5);
        orderMap.put(new Material("МДФ 16мм", "односторонний"), 2.5);
//        Order order = new Order(new Date(), "1122", null, "покраска");

        Order order = new Order(newDate.getText(), newNumber.getText(), null, newDescription.getText());
        orderList.add(order);
        newDate.clear();
        newNumber.clear();
        newDescription.clear();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        initData();
        dateOrderColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("date"));
        numberOrderColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("number"));
        statusOrderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        tableOrder.setItems(orderList);
    }

    private void initData() {
        HashMap<Material, Double> orderMap = new HashMap<>();
        orderMap.put(new Material("МДФ 19мм", "односторонний"), 1.5);
        orderMap.put(new Material("МДФ 16мм", "односторонний"), 2.5);
        Order order = new Order("11.03.2021", "1122", orderMap, "покраска");
        orderList.add(order);
    }
}
