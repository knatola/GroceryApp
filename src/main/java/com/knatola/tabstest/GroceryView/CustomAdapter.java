package com.knatola.tabstest.GroceryView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.knatola.tabstest.R;

import java.util.ArrayList;

/**
 * Created by knatola on 7.10.2017.
 */

public class CustomAdapter extends ArrayAdapter<GroceryItem> {
    private Activity context;
    private int id;
    ArrayList<GroceryItem> list;

    public CustomAdapter(Activity context, int resource, ArrayList<GroceryItem> objects){
        super(context, resource, objects);
        this.context = context;
        this.id = resource;
        this.list = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(id, null);
        }

        final GroceryItem item = list.get(position);
        TextView itemName = (TextView) convertView.findViewById(R.id.newGrocery);
        TextView amount = (TextView) convertView.findViewById(R.id.newAmount);
        TextView price = (TextView) convertView.findViewById(R.id.newPrice);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                item.setIsChecked(isChecked);
            }
        });

        itemName.setText("Name: " + item.getName());
        amount.setText("Amount: " + item.getAmount());
        price.setText("Price: " + item.getPrice());
        checkBox.setChecked(item.isChecked());

        return convertView;
    }
}
