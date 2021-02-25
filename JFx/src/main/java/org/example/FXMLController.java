package org.example;

import java.net.URL;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class FXMLController implements Initializable {
    
    @FXML
    private Label lblOut;

    @FXML
    private Button mainButton;
    
    @FXML
    private void btnClickAction(ActionEvent event) {
        HashMap<Material, Double> orderMap = new HashMap<>();
        orderMap.put(new Material("МДФ 19мм", "односторонний"), 1.5);
        orderMap.put(new Material("МДФ 16мм", "односторонний"), 2.5);
        Order order = new Order(new Date(), 1122, Order.StatusOfOrder.NEW, orderMap, "покраска");
        lblOut.setText(lblOut.getText() + "\n" + order.toString());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
