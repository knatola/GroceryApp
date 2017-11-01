package com.knatola.tabstest.Data;

import com.knatola.tabstest.ImageChooser;

/**
 * Created by knatola on 9.10.2017.
 */

//Plain java class describing data of GroceryItems

public class GroceryItem {

    private String name = "";
    private String price = "";
    private String amount = "";
    private boolean isChecked;
    private GroceryList list;
    private int id;
    private String groceryListName;

    public GroceryItem(){}

    public GroceryItem(String name, String price, String amount, int id){
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.id = id;
    }

    public GroceryItem(String name, String price, String amount){
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public GroceryItem(String name, String price, String amount, String groceryListName){
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.groceryListName = groceryListName;

    }

    /*
    *Default generated getters/setters
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGroceryListName(String groceryListName) {
        this.groceryListName = groceryListName;
    }

    public String getGroceryListName(){ return groceryListName;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public boolean isChecked(){
        return isChecked;
    }

    public void setIsChecked(boolean isChecked){
        this.isChecked = isChecked;
    }
}
