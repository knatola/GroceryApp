package com.knatola.GroceryApp.Login;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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

/*
 * Created by Juho on 9.1.2018.
 */

//todo create account
public class CreateAccFragment extends Fragment {
    View rootView;
    private static final String LOG = "CreateAccFragment: ";
    EditText editPassword;
    EditText editPassword2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.create_acc_frag_layout, container, false);
        Button mCreateAccBtn =rootView.findViewById(R.id.createAccBtn);
        editPassword = rootView.findViewById(R.id.loginPassword_edit);
        editPassword2 = rootView.findViewById(R.id.loginPassword_edit2);
        final EditText nameEdit = rootView.findViewById(R.id.loginName_edit);

        /*
        Check that given Accout isn't alresdy created and create Account.
         */
        mCreateAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(getContext());
                //Check that given passwords match
                if(!editPassword.getText().toString().equals(editPassword2.getText().toString())){
                    Snackbar errorSnack = Snackbar.make(rootView, "Passwords don't match",Snackbar.LENGTH_LONG);
                    errorSnack.show();
                }else{
                    if(!db.getAccountNames().contains(nameEdit.getText().toString())){
                        Account newAcc = new Account(nameEdit.getText().toString(), editPassword.getText().toString());
                        db.createAccount(newAcc);
                        Log.d(LOG,"New account: "+ newAcc.getUserName()+ "," + newAcc.getStringHash());

                        LoginFragment login = new LoginFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.login_container, login);
                        transaction.remove(CreateAccFragment.this);
                        transaction.commit();
                    }else{
                        Snackbar snack = Snackbar.make(rootView, "Account already created", Snackbar.LENGTH_LONG);
                        snack.show();
                    }
                }
                db.closeDB();
            }
        });
        return rootView;
    }
}
