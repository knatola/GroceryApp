package com.knatola.tabstest.GroceryView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.knatola.tabstest.Database.DatabaseHelper;
import com.knatola.tabstest.MainActivity;
import com.knatola.tabstest.R;
import com.knatola.tabstest.Data.GroceryItem;

import java.util.ArrayList;

/**
 * Created by knatola on 7.10.2017.
 */

public class GroceryAddView extends AppCompatActivity {

    private static final String LOG = "GroceryAddView";
    private String groceryListName;
    private ListView listView;
    private EditText editName, editPrice, editAmount;
    private Button addButton, removeButton;
    private ArrayList<GroceryItem> lista;
    private CustomAdapter adapter;
    private FloatingActionButton saveButton;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grocery_view);
        db = new DatabaseHelper(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        groceryListName = bundle.getString("name");
        Log.d(LOG,"Listan  nimi:" + groceryListName);

        addButton = (Button) findViewById(R.id.addButton);
        removeButton = (Button) findViewById(R.id.removeButton);
        saveButton = (FloatingActionButton) findViewById(R.id.saveButton);

        lista = new ArrayList<>();
        adapter = new CustomAdapter(this, R.layout.grocery_list_row, lista);

        editName = (EditText) findViewById(R.id.newGroceryItem);
        editAmount = (EditText) findViewById(R.id.addAmount);
        editPrice = (EditText) findViewById(R.id.addPrice);
        listView = (ListView) findViewById(R.id.groceryList);
        listView.setAdapter(adapter);

        //handling of addButton press, with AddItem()
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = editName.getText().toString();
                final String price = editPrice.getText().toString();
                final String amount = editAmount.getText().toString();
                GroceryItem item = new GroceryItem(name, price, amount, groceryListName);
                Log.d(LOG,"Nimi:" + item.getGroceryListName());
                AddItem(item);
                db.createGrocery(item);
                db.closeDB();
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lista.size() > 0) {
                    for (int i = 0; i < lista.size(); i++) {
                        if (i > lista.size()) {
                            break;
                        }
                        if (lista.get(i).isChecked()) {
                            lista.remove(i);
                            adapter.notifyDataSetChanged();
                            continue;
                        }
                    }
                }
            }
        });

        //saveButton handling
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void AddItem(GroceryItem item){

        if (item.getName().equals("") || item.getAmount().equals("") ||
                item.getPrice().equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(GroceryAddView.this);
            builder.setTitle("Error: information missing.");
            builder.setMessage("Please add more information...");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            builder.show();

        }else{
            lista.add(0, item);
            adapter.notifyDataSetChanged();
            editName.setText("");
            editPrice.setText("");
            editAmount.setText("");
            editName.requestFocus();
        }

    }

    //Handling backButton press
    @Override
    public void onBackPressed() {
        finish();
        Intent backIntent = new Intent(GroceryAddView.this, MainActivity.class);
        startActivity(backIntent);
    }
}
