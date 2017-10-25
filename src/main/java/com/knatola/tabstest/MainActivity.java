package com.knatola.tabstest;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.knatola.tabstest.Database.DatabaseHelper;
import com.knatola.tabstest.Fridge.FridgeViewFragment;
import com.knatola.tabstest.GroceryView.GroceryAddView;
import com.knatola.tabstest.Data.GroceryList;
import com.knatola.tabstest.GroceryView.GroceryListView;
import com.knatola.tabstest.GroceryView.GroceryListsAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private boolean isGroceryListViewOn = false;
    GroceryListsAdapter listsAdapter;
    private ArrayList<GroceryList> groceryListslist;
    private String groceryListName;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(getApplicationContext());

        android.app.FragmentManager fm = getFragmentManager();

        //returning ListName from bundle
        if (getIntent().getExtras() != null){
            setGroceryListName(getIntent().getExtras().getString("name"));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
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
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                     //   .setAction("Action", null).show();
                //Fragment page = getSupportFragmentManager().findFragmentById(mViewPager.getCurrentItem());
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
                            //if the field is empty, prompt an error
                            if (et.getText().toString().equals("")) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                                builder1.setTitle("Error");
                                builder1.setMessage("Give a name.");
                                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });

                                builder1.show();

                            } else {
                                // if the field isn't empty move the name to MainActivity
                                // and start the activity.
                                final String groceryListName = et.getText().toString();
                                //GroceryList newList = new GroceryList(groceryListName);
                                Intent groceryAddIntent = new Intent(MainActivity.this, GroceryAddView.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("name", groceryListName);
                                groceryAddIntent.putExtras(bundle);
                                startActivity(groceryAddIntent);
                                GroceryList newList = new GroceryList(groceryListName);
                                db.createGrocery_List(newList);
                                db.closeDB();

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
                    Snackbar.make(view, "This is FridgeView", Snackbar.LENGTH_LONG).setAction("Action", null)
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    /*public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }*/

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
                    Bundle bundle = new Bundle();
                    bundle.putString("name", getGroceryListName());
                    tab2.setArguments(bundle);
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

    public boolean isGroceryListViewOn() {
        return isGroceryListViewOn;
    }

    public void setGroceryListViewOn(boolean groceryListViewOn) {
        isGroceryListViewOn = groceryListViewOn;
    }

    public String getGroceryListName() {
        return groceryListName;
    }

    public void setGroceryListName(String groceryListName) {
        this.groceryListName = groceryListName;
    }
}
