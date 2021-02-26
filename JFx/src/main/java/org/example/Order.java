package org.example;

import org.example.Material;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Order {
    private String date;
    private String number;
    private StatusOfOrder statusOfOrder;
    private HashMap<Material, Double> materialQuantity;
    private String description;

//    SimpleDateFormat dateformatddMMyyyy = new SimpleDateFormat("dd.MM.yyyy");

    public Order(String date, String number, HashMap<Material, Double> materialQuantity, String description) {
        this.date = date;
        this.number = number;
        this.statusOfOrder = StatusOfOrder.NEW;
        this.materialQuantity = materialQuantity;
        this.description = description;
    }

    enum StatusOfOrder {
        NEW ("Не запущен"),
        START ("Выдан в работу"),
        PAINTING ("На шлифовке/покраска"),
        PACKING("На упаковке"),
        FINISH ("Отгружен");

        String status;

        StatusOfOrder(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        @Override
        public String toString() {
            return "Статус заказа: " + status;
        }
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getNumber(){
        return number;
    }

    public void setNumber(String number){
        this.number = number;
    }

    public String getStatus(){
        return statusOfOrder.getStatus();
    }

    @Override
    public String toString() {
        return String.format(date + "   " + number + "   " + statusOfOrder.getStatus() + "   " + description);
    }
}
