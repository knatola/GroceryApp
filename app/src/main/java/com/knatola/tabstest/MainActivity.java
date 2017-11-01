package com.knatola.tabstest;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.knatola.tabstest.Database.DatabaseHelper;
import com.knatola.tabstest.Fridge.FridgeViewFragment;
import com.knatola.tabstest.Data.GroceryList;
import com.knatola.tabstest.Groceries.GroceryListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String LOG = "MainActivity";
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private boolean dataChange;
    private ArrayList<GroceryList> groceryLists;
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

    }

    @Override
    protected void onStop() {
        db.closeDB();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem saveList =  menu.findItem(R.id.action_save);
        MenuItem deleteList =  menu.findItem(R.id.action_delete);
        MenuItem value = menu.findItem(R.id.action_value);
        if(mViewPager.getCurrentItem()==1){
            deleteList.setVisible(true);
            saveList.setVisible(false);
            value.setVisible(false);
        }else{
            deleteList.setVisible(false);
            saveList.setVisible(false);
            value.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_value) {
            View parentLayout = findViewById(R.id.main_content);
            Snackbar snackbar = Snackbar.make(parentLayout,"Fridges total value: " + String.format("%.2f", db.getGroceryListPrice("fridge")),
                    Snackbar.LENGTH_LONG);
            snackbar.setAction("Action", null).show();
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * Fragment adapter that returns a fragment corresponding to
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
                    return tab1;
                case 1:
                    GroceryListView tab2 = new GroceryListView();
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

        View v = findViewById(R.id.addItems);

        if(mViewPager.getCurrentItem()==0 && v.getVisibility()== View.VISIBLE){
            v.setVisibility(View.GONE);
        }else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }
}
