package com.knatola.tabstest.Data;

import com.knatola.tabstest.Data.GroceryItem;

import java.util.ArrayList;

/**
 * Created by knatola on 11.10.2017.
 */

public class GroceryList {

    private String name = "";
    private boolean isChecked;

    public GroceryList(String name){
        this.name = name;
    }

    public int getIntBool(){
        int bool = (this.isChecked()) ? 1 : 0;
        return bool;
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

    public void setIsChecked(boolean checked) {
        this.isChecked = checked;
    }
}
