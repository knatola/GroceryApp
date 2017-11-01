package com.knatola.tabstest.Groceries;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

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
    private CustomAdapter mAdapter;
    private FloatingActionButton saveButton;
    private String clickedListName;
    DatabaseHelper db;
    private boolean mIsVisible = false;
    private RelativeLayout mSlideLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groceryadd_view);
        db = new DatabaseHelper(getApplicationContext());
        lista = new ArrayList<>();
        mAdapter = new CustomAdapter(this, R.layout.grocery_list_row, lista);

        final Bundle bundle = getIntent().getExtras();
        groceryListName = bundle.getString("name");
        clickedListName = bundle.getString("clicked_list");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addButton = (Button) findViewById(R.id.addButton);
        removeButton = (Button) findViewById(R.id.removeButton);
        saveButton = (FloatingActionButton) findViewById(R.id.saveButton);
        mSlideLayout = (RelativeLayout) findViewById(R.id.addItems);

        editName = (EditText) findViewById(R.id.newGroceryItem);
        editAmount = (EditText) findViewById(R.id.addAmount);
        editPrice = (EditText) findViewById(R.id.addPrice);
        listView = (ListView) findViewById(R.id.groceryList);
        listView.setAdapter(mAdapter);

        //Checking if activity was started from a ListView button click
        if (groceryListName.equals("")) {
            ArrayList<GroceryItem> clickedList = db.getGroceryList(clickedListName);
            toolbar.setTitle(clickedListName);

            for (GroceryItem item : clickedList)
                lista.add(item);

            mAdapter.notifyDataSetChanged();
        } else {
            toolbar.setTitle(groceryListName);
        }

        //handling of addButton press, with AddItem()
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = editName.getText().toString();
                final String price = editPrice.getText().toString();
                final String amount = editAmount.getText().toString();
                if (bundle.getString("clicked_list").equals("")) {
                    GroceryItem item = new GroceryItem(name, price, amount, groceryListName);
                    AddItem(item);
                } else {
                    GroceryItem item2 = new GroceryItem(name, price, amount, clickedListName);
                    AddItem(item2);
                }
            }
        });

        //removeButton
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lista.size() > 0) {
                    for (int i = 0; i < lista.size(); i++) {
                        if (i > lista.size()) {
                            break;
                        }
                        if (lista.get(i).isChecked()) {
                            db.deleteGrocery(lista.get(i).getName());
                            lista.remove(i);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });

        //saveButton handling
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsVisible) {
                    mSlideLayout.setVisibility(View.GONE);
                    mIsVisible = false;
                } else if (!mSlideLayout.isShown()) {
                    mSlideLayout.setVisibility(View.VISIBLE);
                    mIsVisible = true;
                }
            }
        });

    }

    //help method
    public void AddItem(GroceryItem item) {

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

        } else {
            lista.add(0, item);
            mAdapter.notifyDataSetChanged();
            editName.setText("");
            editPrice.setText("");
            editAmount.setText("");
            editName.requestFocus();
            db.createGrocery(item);
            db.closeDB();
        }
    }

    @Override
    protected void onStop() {
        db.closeDB();
        super.onStop();
    }

    //Handling backButton press
    @Override
    public void onBackPressed() {
        if(mSlideLayout.getVisibility()== View.VISIBLE) {
            mSlideLayout.setVisibility(View.GONE);
        }else {
            finish();
            Intent backIntent = new Intent(GroceryAddView.this, MainActivity.class);
            startActivity(backIntent);
        }
    }

    //action bar back button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem deleteList =  menu.findItem(R.id.action_delete);
        deleteList.setVisible(false);
        MenuItem saveList =  menu.findItem(R.id.action_save);
        saveList.setVisible(true);
        MenuItem value = menu.findItem(R.id.action_value);
        value.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
