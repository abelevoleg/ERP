package org.example;

public class Material {
    private String name;
    private String materialDescription;

    public Material(String name, String materialDescription) {
        this.name = name;
        this.materialDescription = materialDescription;
    }

    @Override
    public String toString() {
        return String.format(name + "   " + materialDescription);
    }
}
