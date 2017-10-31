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
    private CustomAdapter adapter;
    private FloatingActionButton saveButton;
    private String clickedListName;
    DatabaseHelper db;
    private Expand expand;
    private boolean isVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groceryadd_view);
        db = new DatabaseHelper(getApplicationContext());
        lista = new ArrayList<>();
        adapter = new CustomAdapter(this, R.layout.grocery_list_row, lista);

        final Bundle bundle = getIntent().getExtras();
        groceryListName = bundle.getString("name");
        clickedListName = bundle.getString("clicked_list");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addButton = (Button) findViewById(R.id.addButton);
        removeButton = (Button) findViewById(R.id.removeButton);
        saveButton = (FloatingActionButton) findViewById(R.id.saveButton);

        final RelativeLayout slideLayout = (RelativeLayout) findViewById(R.id.addItems);
        final RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.groceries);


        LayoutTransition t = mainLayout.getLayoutTransition();
        t.setDuration(2000);
        mainLayout.setLayoutTransition(t);



        editName = (EditText) findViewById(R.id.newGroceryItem);
        editAmount = (EditText) findViewById(R.id.addAmount);
        editPrice = (EditText) findViewById(R.id.addPrice);
        listView = (ListView) findViewById(R.id.groceryList);
        listView.setAdapter(adapter);

        //Log.d(LOG,bundle.getString("clicked_list"));

        //Checking if activity was started from a ListView button click
        if (groceryListName.equals("")) {
            ArrayList<GroceryItem> clickedList = db.getGroceryList(clickedListName);
            toolbar.setTitle(clickedListName);

            for (GroceryItem item : clickedList)
                lista.add(item);

            adapter.notifyDataSetChanged();
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
                            adapter.notifyDataSetChanged();
                            //continue;
                        }
                    }
                }
            }
        });

        //saveButton handling
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVisible) {
                    slideLayout.setVisibility(View.GONE);
                    //animationHelper.expand(slideLayout, 500);
                    isVisible = false;
                } else if (!slideLayout.isShown()) {
                    slideLayout.setVisibility(View.VISIBLE);
                    //animationHelper.expand(slideLayout, 500);

                    isVisible = true;
                }
                //onBackPressed();
            }
        });

    }

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
            adapter.notifyDataSetChanged();
            editName.setText("");
            editPrice.setText("");
            editAmount.setText("");
            editName.requestFocus();
            db.createGrocery(item);
            db.closeDB();
        }

    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    //Handling backButton press
    @Override
    public void onBackPressed() {
        finish();
        Intent backIntent = new Intent(GroceryAddView.this, MainActivity.class);
        startActivity(backIntent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class Expand extends Animation {

        private final int targetHeight;
        private final View view;
        private final boolean down;

        public Expand(View view, int targetHeight, boolean down) {
            this.view = view;
            this.targetHeight = targetHeight;
            this.down = down;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int newHeight;
            if (down) {
                newHeight = (int) (targetHeight * interpolatedTime);
            } else {
                newHeight = (int) (targetHeight * (1 - interpolatedTime));
            }
            view.getLayoutParams().height = newHeight;
            view.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth,
                               int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
}