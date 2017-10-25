package com.knatola.tabstest.GroceryView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.knatola.tabstest.Data.GroceryList;
import com.knatola.tabstest.R;

import java.util.ArrayList;

/**
 * Created by knatola on 11.10.2017.
 */

public class GroceryListsAdapter extends ArrayAdapter<GroceryList> {
    private Activity context;
    private int id;
    ArrayList<GroceryList> lists;

    public GroceryListsAdapter(Activity context, int resource, ArrayList<GroceryList> lists) {
        super(context, resource, lists);
        this.context = context;
        this.id = resource;
        this.lists = lists;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(id, null);
        }

        final GroceryList groceryList = lists.get(position);
        TextView listName = (TextView) convertView.findViewById(R.id.groceryListName);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.listCheckBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                groceryList.setChecked(b);
            }
        });

        listName.setText("Name: "+ groceryList.getName());
        checkBox.setChecked(groceryList.isChecked());

        return convertView;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

}
