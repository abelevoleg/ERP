package org.example;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order implements Comparable<Order>, Serializable {
    private String date;
    private String number;
    private StatusOfOrder statusOfOrder;
    private List<MaterialDataForOrder> materialList;
    private String description;

    static SimpleDateFormat dateformatddMMyyyy = new SimpleDateFormat("dd.MM.yyyy");

    public Order(String date, String number, List<MaterialDataForOrder> materialList, String description) {
        this.date = date;
        this.number = number;
        this.statusOfOrder = StatusOfOrder.NEW;
        this.materialList = materialList;
        this.description = description;
    }

    public Order(){
    }

    @Override
    public int compareTo(Order o) {
        try {
            Date dateFirst = dateformatddMMyyyy.parse(o.getDate());
            Date dateSecond = dateformatddMMyyyy.parse(this.date);
            if (dateFirst.before(dateSecond)) return 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    enum StatusOfOrder {
        NEW ("Не запущен"),
        START ("ЧПУ/раскрой"),
        DRILL ("Присадка"),
        PAINTING ("Шлифовка/покраска"),
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
            return getStatus();
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

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public StatusOfOrder getStatus(){
        return statusOfOrder;
    }

    public String getStatusText(){
        return statusOfOrder.getStatus();
    }

    public void setStatusOfOrder(StatusOfOrder statusOfOrder){
        this.statusOfOrder = statusOfOrder;
    }

    public List<MaterialDataForOrder> getMaterialList(){
        return materialList;
    }

    public void setMaterialList(ArrayList<MaterialDataForOrder> materialList){
        this.materialList = materialList;
    }

    @Override
    public String toString() {
        return String.format(date + "   " + number + "   " + statusOfOrder.getStatus() + "   " + description);
    }
}
