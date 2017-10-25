package com.knatola.tabstest.GroceryView;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.knatola.tabstest.Data.GroceryItem;
import com.knatola.tabstest.R;

import java.util.ArrayList;

/**
 * Created by knatola on 9.10.2017.
 */

public class GroceryView extends Fragment {

    private ListView listView;
    private EditText editName, editPrice, editAmount;
    private Button addButton, removeButton;
    private ArrayList<GroceryItem> lista;
    private CustomAdapter adapter;
    PagerAdapter pagerAdapter;
    ViewPager viewPager;

    //static Context context = GroceryView.context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grocery_view, container, false);
        addButton = view.findViewById(R.id.addButton);
        removeButton = view.findViewById(R.id.removeButton);

        lista = new ArrayList<GroceryItem>();
        adapter = new CustomAdapter(getActivity(), R.layout.grocery_list_row, lista);

        editName = (EditText) view.findViewById(R.id.newGroceryItem);
        editAmount = (EditText) view.findViewById(R.id.addAmount);
        editPrice = (EditText) view.findViewById(R.id.addPrice);
        listView = (ListView) view.findViewById(R.id.groceryList);
        listView.setAdapter(adapter);

        //handling of addButton press, with AddItem()
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddItem(view);
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lista.size() > 0){
                    for (int i = 0; i < lista.size(); i++){
                        if (i > lista.size()){
                            break;
                        }if (lista.get(i).isChecked()){
                            lista.remove(i);
                            adapter.notifyDataSetChanged();
                            continue;
                        }
                    }
                }
            }
        });
        return view;
    }

    //Handling backButton press
    /*@Override
    public void onBackPressed() {
        finish();
        Intent backIntent = new Intent(GroceryListView.this, MainActivity.class);
        startActivity(backIntent);
    }*/

    //Logic for handling the groceryItem adding to listview
    public void AddItem(View view){

        if (editName.getText().toString().equals("") || editAmount.getText().toString().equals("") ||
                editPrice.getText().toString().equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Error: information missing.");
            builder.setMessage("Please add more information...");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            builder.show();

        }else{
            final String name = editName.getText().toString();
            final String price = editPrice.getText().toString();
            final String amount = editAmount.getText().toString();
            GroceryItem item = new GroceryItem(name, price, amount);
            lista.add(0, item);
            adapter.notifyDataSetChanged();
            editName.setText("");
            editPrice.setText("");
            editAmount.setText("");
            editName.requestFocus();
        }

    }
}
