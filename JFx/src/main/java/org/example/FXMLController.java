package org.example;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
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
        Order order = new Order(new Date(), 1122, 1.5, "покраска");
        lblOut.setText(lblOut.getText() + "\n" + order.toString());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
