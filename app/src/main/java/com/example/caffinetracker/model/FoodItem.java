package com.example.caffinetracker.model;

public class FoodItem {

    private String itemNDB; // primary key on https://ndb.nal.usda.gov/ndb/
    private String itemName;
    private String itemMeasure;
    private String itemUnit;
    private String itemValue;

    public FoodItem() {
        itemNDB = "";
        itemName = "";
        itemMeasure = "";
        itemUnit = "";
        itemValue = "";
    }

    public FoodItem(String itemNDB, String itemName,String itemMeasure, String itemUnit, String itemValue) {
        this.itemNDB = itemNDB;
        this.itemName = itemName;
        this.itemMeasure = itemMeasure;
        this.itemUnit = itemUnit;
        this.itemValue = itemValue;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemNDB() {
        return itemNDB;
    }

    public void setItemNDB(String itemNDB) {
        this.itemNDB = itemNDB;
    }

    public String getItemMeasure() {
        return itemMeasure;
    }

    public void setItemMeasure(String itemMeasure) {
        this.itemMeasure = itemMeasure;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemMeasure = itemValue;
    }

    //You can modify this if you want
    @Override
    public String toString() {
        return "FoodItem{" +
                "itemName='" + itemName + '\'' +
                ", itemNDB='" + itemNDB + '\'' +
                ", itemMeasure='" + itemMeasure + '\'' +
                '}';
    }
    public String details(){
        return "Measure = " + this.getItemMeasure() + "\n" +
                "Unit = " + this.getItemUnit() + "\n" +
                "Value = " + this.getItemValue() + "\n";
    }

}
