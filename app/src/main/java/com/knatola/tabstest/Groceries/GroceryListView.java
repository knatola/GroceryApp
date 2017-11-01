package com.knatola.tabstest.Groceries;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.knatola.tabstest.Data.GroceryList;
import com.knatola.tabstest.Database.DatabaseHelper;
import com.knatola.tabstest.MainActivity;
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

            if(groceryList.getName().equals("fridge")){//Don't show fridge
                continue;
            }
            groceryLists.add(0, groceryList);
        }

        final ArrayList<String> listNames = db.getAllGroceryListNames();

        //Setting up adapter with groceryLists
        listsAdapter = new GroceryListsAdapter(this.getActivity(), R.layout.grocerylists_list_row, groceryLists);
        groceryListsView = rootView.findViewById(R.id.groceryLists);
        groceryListsView.setAdapter(listsAdapter);
        listsAdapter.notifyDataSetChanged();

        FloatingActionButton newListButton = rootView.findViewById(R.id.newList);
        newListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Create a new list");
                builder.setMessage("Give a name for your grocery list.");
                final EditText et = new EditText(getContext());
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(et);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //if the field is empty or the given name is already used,
                        // prompt an error
                        String inputName = et.getText().toString();
                        if (inputName.equals("") || listNames.contains(inputName)) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
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
                            Intent groceryAddIntent = new Intent(getContext(), GroceryAddView.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("name", groceryListName);
                            bundle.putString("clicked_list", "");
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
            }
        });
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here.
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
