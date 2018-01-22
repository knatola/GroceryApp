package com.knatola.GroceryApp.Login;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.knatola.GroceryApp.Data_Models.Account;
import com.knatola.GroceryApp.Database.DatabaseHelper;
import com.knatola.GroceryApp.MainActivity;
import com.knatola.GroceryApp.R;
import com.knatola.GroceryApp.SettingsActivity;

/*
 * Created by Juho on 9.1.2018.
 */

public class LoginFragment extends Fragment {
    View rootView;
    private static final String LOG = "LOGINFRAGMENT:";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.login_frag_layout, container, false);
        final EditText mNameEdit = rootView.findViewById(R.id.loginName_edit);
        final EditText mPasswordEdit = rootView.findViewById(R.id.loginPassword_edit);
        final Button mCreateAccBtn = rootView.findViewById(R.id.createAccBtn);
        final DatabaseHelper db = new DatabaseHelper(getContext());

        mCreateAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG, "CreateAccBtn Click");
                CreateAccFragment accFragment = new CreateAccFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.login_container, accFragment);
                transaction.remove(LoginFragment.this);
                transaction.commit();
            }
        });

        Button mLoginBtn =  rootView.findViewById(R.id.loginButton);

        /*
        Check that account is created and that the given username and password match.
         */
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(db.getAccount() != null){
                    Account account = db.getAccount();
                    String name = mNameEdit.getText().toString();
                    String testHash = new Account().createHash(name, mPasswordEdit.getText().toString());
                    String accHash = db.getAccount().getStringHash();
                    Log.d(LOG, "testing: "+ accHash + " and "+ testHash);
                    if(testHash.equals(account.getStringHash())){
                        Log.d(LOG, "Login success");
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("login_status", true);
                        Intent mainIntent = new Intent(getContext(), MainActivity.class);
                        mainIntent.putExtras(bundle);
                        startActivity(mainIntent);
                    }else{
                        Log.d(LOG, "Login fail");
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Failure");
                        builder.setMessage("Username and Password don't match.\nPlease try again.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mPasswordEdit.setText("");
                            }
                        });
                        builder.show();
                    }
                }else{
                    Log.d(LOG, "Login fail");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Failure");
                    builder.setMessage("Account not found.\n");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mPasswordEdit.setText("");
                            mNameEdit.setText("");
                        }
                    });
                    builder.show();
                }
                db.closeDB();
            }
        });
        return rootView;
    }
}
