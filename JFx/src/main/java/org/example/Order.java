package org.example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Order {
    private String date;
    private int number;
    private StatusOfOrder statusOfOrder;
    private HashMap<Material, Double> materialQuantity;
    private String description;

    SimpleDateFormat dateformatddMMyyyy = new SimpleDateFormat("dd.MM.yyyy");

    public Order(Date date, int number, StatusOfOrder statusOfOrder, HashMap<Material, Double> materialQuantity, String description) {
        this.date = dateformatddMMyyyy.format(date);
        this.number = number;
        this.statusOfOrder = statusOfOrder;
        this.materialQuantity = materialQuantity;
        this.description = description;
    }

    enum StatusOfOrder {
        NEW ("Не запущен"),
        START ("Выдан в работу"),
        PAINTING ("На шлифовке/покраска"),
        PACKING("На упаковке"),
        FINISH ("Отгружен");

        private String status;

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

    @Override
    public String toString() {
        return String.format(date + "   " + number + "   " + statusOfOrder.getStatus() + "   " + materialQuantity + "   " + description);
    }
}
