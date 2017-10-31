package com.knatola.tabstest.Groceries;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.knatola.tabstest.Data.GroceryList;
import com.knatola.tabstest.Database.DatabaseHelper;
import com.knatola.tabstest.R;

import java.util.ArrayList;

/**
 * Created by knatola on 11.10.2017.
 */

public class GroceryListView extends Fragment {

    private static final String LOG = "GroceryListView fragment";
    GroceryListsAdapter listsAdapter;
    private ArrayList<GroceryList> groceryLists;
    private ListView groceryListsView;
    DatabaseHelper db;
    private Boolean isDataChanged;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.grocerylists_view, container, false);
        setHasOptionsMenu(true);
        db = new DatabaseHelper(getContext());
        Bundle bundle = new Bundle();
        isDataChanged = bundle.getBoolean("data_change");


        //Returning all the groceryListNames from db, assigning them to ArrayList
        ArrayList<String> stringNames = db.getAllGroceryListNames();

        //Initializing groceryLists, from the returned names
        groceryLists = new ArrayList<>();
        for(String i: stringNames){
            GroceryList groceryList = new GroceryList(i, db.getGroceryListPrice(i));
            groceryLists.add(0, groceryList);
        }

        //Setting up adapter with groceryLists
        listsAdapter = new GroceryListsAdapter(this.getActivity(), R.layout.grocerylists_list_row, groceryLists);
        groceryListsView = rootView.findViewById(R.id.groceryLists);
        groceryListsView.setAdapter(listsAdapter);
        listsAdapter.notifyDataSetChanged();

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            if (groceryLists.size() > 0){
                for (int i = 0; i < groceryLists.size(); i++) {
                    if (i > groceryLists.size()) {
                        break;
                    }
                    if (groceryLists.get(i).isChecked()) {
                        db.destroyGrocery_List(groceryLists.get(i).getName());
                        db.closeDB();
                        groceryLists.remove(i);
                        listsAdapter.notifyDataSetChanged();

                        continue;
                    }
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
