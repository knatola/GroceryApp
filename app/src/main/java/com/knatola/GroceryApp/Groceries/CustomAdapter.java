package com.knatola.GroceryApp.Groceries;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.knatola.GroceryApp.Data.GroceryItem;
import com.knatola.GroceryApp.ImageChooser;
import com.knatola.GroceryApp.R;

import java.util.ArrayList;

/**
 * Created by knatola on 7.10.2017.
 */

//Adapter for Items in GroceryAddView

public class CustomAdapter extends ArrayAdapter<GroceryItem> {
    private Activity context;
    private int id;
    ArrayList<GroceryItem> list;
    private ImageChooser imgC;

    public CustomAdapter(Activity context, int resource, ArrayList<GroceryItem> objects){
        super(context, resource, objects);
        this.context = context;
        this.id = resource;
        this.list = objects;
        imgC = new ImageChooser();
    }

    public boolean isAnyItemChecked(ArrayList<GroceryItem> list){
        for (GroceryItem item: list){
            if (item.isChecked())
                    return true;
        }
        return false;
    }

    public interface OnCheckChangeListener{
        public void onCheckChange(boolean isChecked);
    }
    OnCheckChangeListener mOnCheckChangeListener;

    public void setOnDataChangeListener(OnCheckChangeListener onCheckChangeListener){
        mOnCheckChangeListener = onCheckChangeListener;
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
        ImageView pic  = convertView.findViewById(R.id.foodPic);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                item.setIsChecked(isChecked);
                if(mOnCheckChangeListener != null){
                    mOnCheckChangeListener.onCheckChange(isAnyItemChecked(list));
                }

            }
        });

        itemName.setText(item.getName());
        amount.setText("Amount: " + item.getAmount());
        price.setText("Price: " + item.getPrice());
        checkBox.setChecked(item.isChecked());
        pic.setImageResource(imgC.getFoodPic());

        return convertView;
    }
}
