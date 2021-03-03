package org.example;

import javafx.collections.ObservableList;

import java.util.List;

public class Order {
    private String date;
    private String number;
    private StatusOfOrder statusOfOrder;
    private List<MaterialDataForOrder> materialList;
    private String description;

//    SimpleDateFormat dateformatddMMyyyy = new SimpleDateFormat("dd.MM.yyyy");

    public Order(String date, String number, List<MaterialDataForOrder> materialList, String description) {
        this.date = date;
        this.number = number;
        this.statusOfOrder = StatusOfOrder.NEW;
        this.materialList = materialList;
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

    public void setMaterialList(ObservableList<MaterialDataForOrder> materialList){
        this.materialList = materialList;
    }

    @Override
    public String toString() {
        return String.format(date + "   " + number + "   " + statusOfOrder.getStatus() + "   " + description);
    }
}
