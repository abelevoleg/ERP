package org.example;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Order {
    private String date;
    private int number;
    private double materialQuantity;
    private String description;

    SimpleDateFormat dateformatddMMyyyy = new SimpleDateFormat("dd.MM.yyyy");

    public Order(Date date, int number, double materialQuantity, String description) {
        this.date = dateformatddMMyyyy.format(date);
        this.number = number;
        this.materialQuantity = materialQuantity;
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format(date + "   " + number + "   " + materialQuantity + "   " + description);
    }
}
