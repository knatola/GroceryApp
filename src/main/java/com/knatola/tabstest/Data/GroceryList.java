package com.knatola.tabstest.Data;

import com.knatola.tabstest.Data.GroceryItem;

import java.util.ArrayList;

/**
 * Created by knatola on 11.10.2017.
 */

public class GroceryList {
    private ArrayList<GroceryItem> listOfGroceries;
    private String name;
    private boolean isChecked;

    public GroceryList(){}

    public GroceryList(String name){
        this.name = name;
        //this.listOfGroceries = listOfGroceries;
    }

    public ArrayList<GroceryItem> getListOfGroceries() {
        return listOfGroceries;
    }

    public void setListOfGroceries(ArrayList<GroceryItem> listOfGroceries) {
        this.listOfGroceries = listOfGroceries;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }
}
