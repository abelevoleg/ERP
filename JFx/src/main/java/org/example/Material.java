package org.example;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

public class Material implements Comparable<Material>, Serializable {
    private String name;
    private String materialDescription;
    private int quantity;

    public Material(String name, String materialDescription, int quantity) {
        this.name = name;
        this.materialDescription = materialDescription;
        this.quantity = quantity;
    }

    public Material(){
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getMaterialDescription(){
        return materialDescription;
    }

    public void setMaterialDescription(String materialDescription){
        this.materialDescription = materialDescription;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format(name + "   " + materialDescription + "   " + quantity);
    }

    @Override
    public int compareTo(Material o) {
        String name = this.name;
        String nameSecond = o.getName();
        return name.compareTo(nameSecond);
    }
}
