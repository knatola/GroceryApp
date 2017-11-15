package com.knatola.GroceryApp.Data;

/**
 * Created by knatola on 11.10.2017.
 */

public class GroceryList {

    private String name = "";
    private boolean isChecked;
    private double price;

    public GroceryList(String name){
        this.name = name;
    }

    public GroceryList(String name, double price){
        this.name = name;
        this.price = price;
    }

    public int getIntBool(){
        int bool = (this.isChecked()) ? 1 : 0;
        return bool;
    }
    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean checked) {
        this.isChecked = checked;
    }
}
