package com.knatola.tabstest.Fridge;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.knatola.tabstest.R;

/**
 * Created by knatola on 11.10.2017.
 */

public class FridgeViewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fridgerator_view, container, false);
        return rootView;
    }
}
