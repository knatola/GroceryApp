package com.knatola.GroceryApp.Fridge;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.knatola.GroceryApp.Data_Models.GroceryItem;
import com.knatola.GroceryApp.Database.DatabaseHelper;
import com.knatola.GroceryApp.Groceries.CustomAdapter;
import com.knatola.GroceryApp.R;

import java.util.ArrayList;

/**
 * Created by knatola on 11.10.2017.
 */

public class FridgeViewFragment extends Fragment implements CustomAdapter.OnCheckChangeListener {
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
    private LinearLayout fridgeLinear;
    private View clickHere;
    private FloatingActionButton mRemoveItemBtn;
    private RelativeLayout mSlideLayout;
    private View rootView;

    @Override
    public void onCheckChange(boolean isChecked) {
        if (isChecked && mAdapter.isAnyItemChecked(mLista))
            mRemoveButton.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fridgerator_view, container, false);
        mSlideLayout = rootView.findViewById(R.id.addItems);
        FloatingActionButton addToFridge = rootView.findViewById(R.id.addToFridge);
        db = new DatabaseHelper(getContext());


        mRemoveItemBtn = rootView.findViewById(R.id.removeItem);
        clickHere = rootView.findViewById(R.id.clickHere);
        fridgeLinear = rootView.findViewById(R.id.fridgeLinear);
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

        //If any checkbox is checked, mRemoveItemBtn will be visible.
        mAdapter.setOnDataChangeListener(new CustomAdapter.OnCheckChangeListener() {
            @Override
            public void onCheckChange(boolean isChecked) {
                if (isChecked && mAdapter.isAnyItemChecked(mLista)){
                    mRemoveItemBtn.setVisibility(View.VISIBLE);
                }
                else{
                    mRemoveItemBtn.setVisibility(View.GONE);
                }
            }
        });

        mAdapter.notifyDataSetChanged();

        //fab
        addToFridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsVisible) {
                    mSlideLayout.setVisibility(View.GONE);
                    clickHere.setVisibility(View.GONE);
                    mIsVisible = false;
                } else if (!mSlideLayout.isShown()) {
                    mSlideLayout.setVisibility(View.VISIBLE);
                    clickHere.setVisibility(View.VISIBLE);
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
        mRemoveItemBtn.setOnClickListener(new View.OnClickListener() {
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

        clickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideLayout.setVisibility(View.GONE);
                mIsVisible = false;
                clickHere.setVisibility(View.GONE);
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
            mSlideLayout.setVisibility(View.GONE);
            clickHere.setVisibility(View.GONE);
            Snackbar newSnackBar = Snackbar.make(rootView.findViewById(R.id.addItems),
                    item.getName()+ " added.", Snackbar.LENGTH_LONG);
            newSnackBar.setAction("UNDO", new UndoListener(item));
            newSnackBar.setActionTextColor(Color.RED);
            newSnackBar.show();
        }
    }
    //helper class to communicate with list adapter through interface
    public class UndoListener implements View.OnClickListener{
        GroceryItem item;
        public UndoListener(GroceryItem item){
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            db.deleteGrocery(item.getName());
            mLista.remove(item);
            mAdapter.notifyDataSetChanged();
        }
    }
}
