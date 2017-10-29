package com.knatola.tabstest;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.knatola.tabstest.Data.GroceryItem;
import com.knatola.tabstest.Database.DatabaseHelper;
import com.knatola.tabstest.Fridge.FridgeViewFragment;
import com.knatola.tabstest.GroceryView.GroceryAddView;
import com.knatola.tabstest.Data.GroceryList;
import com.knatola.tabstest.GroceryView.GroceryListView;
import com.knatola.tabstest.GroceryView.GroceryListsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String LOG = "MainActivity";
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private boolean isGroceryListViewOn = false;
    private boolean dataChange;
    private ArrayList<GroceryList> groceryLists;
    private String groceryListName;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataChange = false;
        db = new DatabaseHelper(getApplicationContext());

        groceryLists = new ArrayList<>();
        final ArrayList<String> listNames = db.getAllGroceryListNames();
        for(String i: listNames){
            GroceryList groceryList = new GroceryList(i);
            groceryLists.add(groceryList);
        }

        for(String i: listNames){
            Log.d(LOG, i);
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton newListButton = (FloatingActionButton) findViewById(R.id.fab);
        newListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mViewPager.getCurrentItem() == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Create a new list");
                    builder.setMessage("Give a name for your grocery list.");
                    final EditText et = new EditText(MainActivity.this);
                    et.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(et);

                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //if the field is empty or the given name is already used,
                            // prompt an error
                            String inputName = et.getText().toString();
                            if (inputName.equals("") || listNames.contains(inputName)) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                                builder1.setTitle("Error");
                                builder1.setMessage("Give an unique name.");
                                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });

                                builder1.show();

                            } else {
                                final String groceryListName = et.getText().toString();
                                Intent groceryAddIntent = new Intent(MainActivity.this, GroceryAddView.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("name", groceryListName);
                                bundle.putString("clicked_list", "");
                                bundle.putBoolean("data_change", isDataChange());
                                groceryAddIntent.putExtras(bundle);
                                Log.d(LOG, "changing to GroceryAddView");
                                startActivity(groceryAddIntent);
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();

                }else {
                    List<GroceryItem> apuLista = db.getGroceryList("test13");
                    GroceryItem test = apuLista.get(2);
                    Snackbar.make(view, "item:" + test.getName(), Snackbar.LENGTH_LONG).setAction("Action", null)
                            .show();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    FridgeViewFragment tab1 = new FridgeViewFragment();
                    setGroceryListViewOn(false);
                    return tab1;
                case 1:
                    GroceryListView tab2 = new GroceryListView();
                    setGroceryListViewOn(true);
                    return tab2;
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Fridge";
                case 1:
                    return "Grocery Lists";
            }
            return null;
        }
    }

    //Handling backButton press
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public boolean isDataChange() {
        return dataChange;
    }

    public void setDataChange(boolean dataChange) {
        this.dataChange = dataChange;
    }

    public boolean isGroceryListViewOn() {
        return isGroceryListViewOn;
    }

    public void setGroceryListViewOn(boolean groceryListViewOn) {
        isGroceryListViewOn = groceryListViewOn;
    }

    public void setGroceryListName(String groceryListName) {
        this.groceryListName = groceryListName;
    }
}
