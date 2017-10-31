package com.knatola.tabstest.Groceries;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(id, null);
        }

        final GroceryList groceryList = lists.get(position);
        Button editListBtn = convertView.findViewById(R.id.editListBtn);
        TextView listName = (TextView) convertView.findViewById(R.id.groceryListName);
        TextView listPrice = convertView.findViewById(R.id.groceryListPrice);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.listCheckBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                groceryList.setIsChecked(b);
            }
        });

        editListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clickedList = getItem(position).getName();
                Log.d("adapter", "Clicked: " + clickedList );
                Bundle bundle = new Bundle();
                bundle.putString("clicked_list",clickedList);
                bundle.putString("name", "");
                Intent addViewIntent = new Intent(context, GroceryAddView.class);
                addViewIntent.putExtras(bundle);
                context.startActivity(addViewIntent);
            }
        });

        listName.setText(groceryList.getName());
        checkBox.setChecked(groceryList.isChecked());
        listPrice.setText("Price: " + String.format("%.2f", groceryList.getPrice()));

        return convertView;
    }

    @Nullable
    @Override
    public GroceryList getItem(int position) {
        return super.getItem(position);
    }
}

