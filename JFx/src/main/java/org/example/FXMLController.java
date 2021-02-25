package org.example;

import java.net.URL;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    private Button mainButton;
    
    @FXML
    private void btnClickAction(ActionEvent event) {
        HashMap<Material, Double> orderMap = new HashMap<>();
        orderMap.put(new Material("МДФ 19мм", "односторонний"), 1.5);
        orderMap.put(new Material("МДФ 16мм", "односторонний"), 2.5);
        Order order = new Order(new Date(), "1122", Order.StatusOfOrder.NEW, orderMap, "покраска");
        orderList.add(order);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initData();
        dateOrderColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("date"));
        numberOrderColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("number"));

        tableOrder.setItems(orderList);
    }

    private void initData() {
        HashMap<Material, Double> orderMap = new HashMap<>();
        orderMap.put(new Material("МДФ 19мм", "односторонний"), 1.5);
        orderMap.put(new Material("МДФ 16мм", "односторонний"), 2.5);
        Order order = new Order(new Date(), "1122", Order.StatusOfOrder.NEW, orderMap, "покраска");
        orderList.add(order);
    }
}
