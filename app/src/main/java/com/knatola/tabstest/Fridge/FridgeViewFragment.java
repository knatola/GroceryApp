package com.knatola.tabstest.Fridge;


import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.knatola.tabstest.Data.GroceryItem;
import com.knatola.tabstest.Database.DatabaseHelper;
import com.knatola.tabstest.Groceries.CustomAdapter;
import com.knatola.tabstest.Groceries.GroceryAddView;
import com.knatola.tabstest.MainActivity;
import com.knatola.tabstest.R;

import java.util.ArrayList;

/**
 * Created by knatola on 11.10.2017.
 */

public class FridgeViewFragment extends Fragment {
    private boolean mIsVisible = false;
    private EditText editName;
    private EditText editAmount;
    private EditText editPrice;
    private ListView listView;
    private Button mAddButton;
    private Button mRemoveButton;
    private ArrayList<GroceryItem> mLista;
    private CustomAdapter mAdapter;
    private DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fridgerator_view, container, false);
        final RelativeLayout slideLayout = rootView.findViewById(R.id.addItems);
        FloatingActionButton addToFridge = rootView.findViewById(R.id.addToFridge);
        db = new DatabaseHelper(getContext());

        mAddButton = rootView.findViewById(R.id.addButton);
        mRemoveButton = rootView.findViewById(R.id.removeButton);
        editName = rootView.findViewById(R.id.newGroceryItem);
        editAmount = rootView.findViewById(R.id.addAmount);
        editPrice = rootView.findViewById(R.id.addPrice);
        listView = rootView.findViewById(R.id.fridgeList);

        //returning fridge items from db, adding them to adapter
        mLista = new ArrayList<>();
        ArrayList<GroceryItem> currentFridge = db.getGroceryList("fridge");
        for(GroceryItem item : currentFridge){
            mLista.add(item);
        }

        mAdapter = new CustomAdapter(getActivity(), R.layout.grocery_list_row, mLista);
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        //fab
        addToFridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsVisible) {
                    slideLayout.setVisibility(View.GONE);
                    mIsVisible = false;
                } else if (!slideLayout.isShown()) {
                    slideLayout.setVisibility(View.VISIBLE);
                    mIsVisible = true;
                }
            }
        });

        //addButton
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = editName.getText().toString();
                final String price = editPrice.getText().toString();
                final String amount = editAmount.getText().toString();
                GroceryItem item = new GroceryItem(name, price, amount, "fridge");
                AddItem(item);
            }
        });

        //remove button
        mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLista.size() > 0) {
                    for (int i = 0; i < mLista.size(); i++) {
                        if (i > mLista.size()) {
                            break;
                        }
                        if (mLista.get(i).isChecked()) {
                            db.deleteGrocery(mLista.get(i).getName());
                            mLista.remove(i);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });

        return rootView;
    }
    //help method
    public void AddItem(GroceryItem item) {

        if (item.getName().equals("") || item.getAmount().equals("") ||
                item.getPrice().equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Error: information missing.");
            builder.setMessage("Please add more information...");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            builder.show();

        } else {
            mLista.add(0, item);
            mAdapter.notifyDataSetChanged();
            editName.setText("");
            editPrice.setText("");
            editAmount.setText("");
            editName.requestFocus();
            db.createGrocery(item);
            db.closeDB();
        }
    }
}
