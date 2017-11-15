package com.knatola.GroceryApp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.knatola.GroceryApp.Database.DatabaseHelper;
import com.knatola.GroceryApp.Fridge.FridgeViewFragment;
import com.knatola.GroceryApp.Data.GroceryList;
import com.knatola.GroceryApp.Groceries.GroceryListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String LOG = "MainActivity";
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ArrayList<GroceryList> groceryLists;
    DatabaseHelper db;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(getApplicationContext());
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open,
                R.string.close);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mToolbar.setNavigationIcon(R.drawable.nav_icon);

        groceryLists = new ArrayList<>();
        final ArrayList<String> listNames = db.getAllGroceryListNames();
        for(String i: listNames){
            GroceryList groceryList = new GroceryList(i);
            groceryLists.add(groceryList);
        }


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
        MenuItem value = menu.findItem(R.id.action_value);
        MenuItem settings = menu.findItem(R.id.action_settings);
        if(mViewPager.getCurrentItem()==1){
            saveList.setVisible(false);
            value.setVisible(false);
            settings.setVisible(true);
        }else{
            saveList.setVisible(false);
            value.setVisible(true);
            settings.setVisible(true);

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
        }else if(id == R.id.action_settings){
            SettingsActivity settings = new SettingsActivity();
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
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
        View v1 = findViewById(R.id.clickHere);

        if(mViewPager.getCurrentItem()==0 && v.getVisibility()== View.VISIBLE){
            v.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
        }else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        getDelegate().setSupportActionBar(toolbar);
        getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
