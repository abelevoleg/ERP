package org.example;

public class LackMaterialData implements Comparable<LackMaterialData> {
    private String materialName;
    private String orderNumber;
    private int lackQuantity;

    public LackMaterialData(String materialName, String orderNumber, int lackQuantity) {
        this.materialName = materialName;
        this.orderNumber = orderNumber;
        this.lackQuantity = lackQuantity;
    }

    public LackMaterialData(){
    }

    public String getMaterialName(){
        return materialName;
    }

    public void setMaterialName(String materialName){
        this.materialName = materialName;
    }

    public String getOrderNumber(){
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber){
        this.orderNumber = orderNumber;
    }

    public int getLackQuantity(){
        return lackQuantity;
    }

    public void setLackQuantity(int lackQuantity){
        this.lackQuantity = lackQuantity;
    }

    @Override
    public String toString() {
        return String.format(materialName + "   " + lackQuantity + "   " + orderNumber);
    }

    @Override
    public int compareTo(LackMaterialData o) {
        String name = this.materialName;
        String nameSecond = o.getMaterialName();
        return name.compareTo(nameSecond);
    }
}
