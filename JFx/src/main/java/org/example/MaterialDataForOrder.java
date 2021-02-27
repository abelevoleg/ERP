package org.example;

public class MaterialDataForOrder {
    private String materialName;
    private Double quantity;

    public MaterialDataForOrder(String materialName, Double quantity) {
        this.materialName = materialName;
        this.quantity = quantity;
    }

    public String getMaterialName(){
        return materialName;
    }

    public void setMaterialName(String materialName){
        this.materialName = materialName;
    }

    public Double getQuantity(){
        return quantity;
    }

    public void setQuantity(Double quantity){
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format(materialName + "   " + quantity);
    }
}
