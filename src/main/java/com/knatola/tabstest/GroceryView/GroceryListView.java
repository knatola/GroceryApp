package com.knatola.tabstest.GroceryView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.knatola.tabstest.MainActivity;
import com.knatola.tabstest.R;

import java.util.ArrayList;

/**
 * Created by knatola on 11.10.2017.
 */

public class GroceryListView extends Fragment {

    GroceryListsAdapter listsAdapter;
    private ArrayList<GroceryList> groceryListslist;
    private String groceryListName;
    private ListView groceryListsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.grocerylists_view, container, false);
        Bundle bundle = getArguments();

        /*
        if(bundle.getString("name").equals("")){
            return rootView;
        }else {*/

            groceryListName = getListName();
            GroceryList groceryList = new GroceryList(groceryListName);
            groceryListslist = new ArrayList<>();


            //Initialization
            listsAdapter = new GroceryListsAdapter(getActivity(), R.layout.grocerylists_list_row, groceryListslist);
            groceryListsView = rootView.findViewById(R.id.groceryLists);
            groceryListsView.setAdapter(listsAdapter);

            //handling adapter
            groceryListslist.add(0, groceryList);
            listsAdapter.notifyDataSetChanged();
        //}
        return rootView;
    }
    public String getListName(){
        String listName;
        Bundle bundle = getArguments();
        listName = bundle.getString("name");
        return listName;
    }
}
