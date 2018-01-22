package com.knatola.GroceryApp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

/*
 * Created by knatola on 14.11.2017.
 */

public class SettingsActivity extends AppCompatActivity{
    private Boolean mLoginStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingspage_view);
        Switch mSwitch = (Switch) findViewById(R.id.settings_switch1);
        TextView mSuggestion = (TextView) findViewById(R.id.login_suggestion);
        TextView mCloudText = (TextView) findViewById(R.id.cloudTxt);
        Bundle b = getIntent().getExtras();
        if(b != null){
            mLoginStatus = b.getBoolean("login_status");
            if(mLoginStatus){
                mSwitch.setVisibility(View.VISIBLE);
                mCloudText.setVisibility(View.VISIBLE);
                mSuggestion.setVisibility(View.GONE);
            }
        }
    }
}
