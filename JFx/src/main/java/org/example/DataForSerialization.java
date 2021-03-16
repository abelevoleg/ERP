package org.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataForSerialization implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Order> orderList;
    private List<Order> orderArchiveList;
    private List<Material> materialList;

    public DataForSerialization(ArrayList<Order> orderList, ArrayList<Material> materialList, ArrayList<Order> orderArchiveList){
        this.orderList = orderList;
        this.materialList = materialList;
        this.orderArchiveList = orderArchiveList;
    }

    public DataForSerialization(){
    }

    public List<Order> getOrderList(){
        return orderList;
    }

    public void setOrderList(ArrayList<Order> orderList){
        this.orderList = orderList;
    }

    public List<Order> getOrderArchiveList(){
        return orderArchiveList;
    }

    public void setOrderArchiveList(ArrayList<Order> orderArchiveList){
        this.orderArchiveList = orderArchiveList;
    }

    public List<Material> getMaterialList(){
        return materialList;
    }

    public void setMaterialList(ArrayList<Material> materialList){
        this.materialList = materialList;
    }

}
