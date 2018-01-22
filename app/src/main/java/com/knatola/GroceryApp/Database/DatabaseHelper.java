package com.knatola.GroceryApp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import com.knatola.GroceryApp.Data_Models.Account;
import com.knatola.GroceryApp.Data_Models.GroceryItem;
import com.knatola.GroceryApp.Data_Models.GroceryList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/*
 * Created by knatola on 24.10.2017.
 * Databasehelper for the SQLite relational database.
 */


public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String LOG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 5;
    private static final String TAG ="Database";

    private static final String DATABASE_NAME = "groceries.db";
    private static final String TABLE_GROCERIES = "groceries";
    private static final String TABLE_GROCERY_LIST= "grocery_list";
    private static final String TABLE_ACCOUNT = "account";

    //common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    //GROCERIES table column names
    private static final String GROCERY_NAME = "grocery_name";
    private static final String GROCERY_AMOUNT = "amount";
    private static final String GROCERY_PRICE = "price";
    private static final String GROCERY_ITEM_LIST_NAME = "list";
    private static final String GROCERY_ISCHECKED = "is_checked";

    //grocery_list table column names
    private static final String GROCERY_LIST_NAME = "grocery_list_name";

    //account table column names
    private static final String ACCOUNT_NAME = "account_name";
    private static final String HASH_STRING = "hash_string";

    //boolean for checking if there is an account in the db
    private boolean isAccount = false;


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG,"database Constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_GROCERIES);
        db.execSQL(CREATE_TABLE_GROCERY_LIST);
        db.execSQL(CREATE_TABLE_ACCOUNT);
        setIsAccount(false);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROCERIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROCERY_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        onCreate(db);
    }

    // Table create statements
    // Groceries table statement
    private static final String CREATE_TABLE_GROCERIES = "CREATE TABLE " + TABLE_GROCERIES
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + GROCERY_NAME + " TEXT," + GROCERY_AMOUNT
            + " TEXT," + GROCERY_PRICE + " TEXT," + GROCERY_ITEM_LIST_NAME + " TEXT," +  KEY_CREATED_AT
            + " DATETIME" + ");";

    //grocerylist create statement
    private static final String CREATE_TABLE_GROCERY_LIST = "CREATE TABLE " + TABLE_GROCERY_LIST
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + GROCERY_LIST_NAME + " TEXT, " + KEY_CREATED_AT
            + " DATETIME" + ");";

    //Account create statement
    private static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE " + TABLE_ACCOUNT
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ACCOUNT_NAME + " TEXT, "
            + HASH_STRING + " TEXT, " + KEY_CREATED_AT + " DATETIME" + ");";

    // CRUD STATEMENTS:

    //Creating the account
    public long createAccount(Account account){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ACCOUNT_NAME, account.getUserName());
        values.put(HASH_STRING, account.getStringHash());
        values.put(KEY_CREATED_AT, getDateTime());

        long account_id = db.insert(TABLE_ACCOUNT, null, values);
        Log.d(TAG, account.getUserName() + " created");
        setIsAccount(true);

        return account_id;
    }

    //Destroying account
    public void deleteAccount(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_ACCOUNT);
        setIsAccount(false);
    }

    //return accountNames
    public List<String> getAccountNames(){
        List<String> names = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + ACCOUNT_NAME + " FROM " + TABLE_ACCOUNT + ";";

        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()){
            do {
               names.add(c.getString(c.getColumnIndex(ACCOUNT_NAME)));
            }while(c.moveToNext());
        }
        c.close();

        return names;
    }

    // Creating a grocery item method
    public long createGrocery(GroceryItem item){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GROCERY_NAME, item.getName());
        values.put(GROCERY_AMOUNT, item.getAmount());
        values.put(GROCERY_PRICE, item.getPrice());
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(GROCERY_ITEM_LIST_NAME, item.getGroceryListName());

        //inserting new row
        long grocery_id = db.insert(TABLE_GROCERIES, null, values);

        Log.d(TAG,item.getName() + " created");


        return grocery_id;
    }

    //method to destroy a grocery item
    public void deleteGrocery(String groceryName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GROCERIES, GROCERY_NAME + " = '" + groceryName + "';", null);
        Log.e(TAG, groceryName + " deleted");
    }

    // return a list of groceries by grocery_list name
    public ArrayList<GroceryItem> getGroceryList(String listName){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<GroceryItem> groceryItems = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_GROCERIES + " WHERE " + GROCERY_ITEM_LIST_NAME +
                " = '" + listName + "';";
        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do {
                GroceryItem item = new GroceryItem();
                item.setAmount(c.getString(c.getColumnIndex(GROCERY_AMOUNT)));
                item.setName(c.getString(c.getColumnIndex(GROCERY_NAME)));
                item.setPrice(c.getString(c.getColumnIndex(GROCERY_PRICE)));
                item.setId(c.getInt(c.getColumnIndex(KEY_ID)));

                groceryItems.add(item);
            }while(c.moveToNext());
        }
        return groceryItems;
    }

    //returns a grocery lists price
    public double getGroceryListPrice(String listName){
        SQLiteDatabase db = this.getReadableDatabase();
        double finalPrice = 0;
        String selectQuery = "SELECT " + GROCERY_PRICE + " FROM " + TABLE_GROCERIES + " WHERE "
                + GROCERY_ITEM_LIST_NAME + " = '" + listName + "';";
        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);


        if(c.moveToFirst()) {
            do {
                String addPrice = c.getString(c.getColumnIndex(GROCERY_PRICE));
                double price = Double.parseDouble(addPrice);
                finalPrice = finalPrice + price;
            } while (c.moveToNext());
        }
        return finalPrice;
    }

    /*
    *Destroy a Grocery List(all groceries with the same 'list')
    */
    public void destroyGrocery_List(String listName){
        Log.d(LOG, "destroying: " + listName);
        List<GroceryItem> destroyableGroceries = getGroceryList(listName);
        for(GroceryItem item: destroyableGroceries){
            deleteGrocery(item.getName());
        }
    }

    public Account getAccount(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT *" + " FROM " + TABLE_ACCOUNT + ";";
        Cursor c = db.rawQuery(selectQuery, null);

        Log.e(LOG, selectQuery);

        if(c != null)
            c.moveToFirst();
        Account account = new Account();
        try{
            account.setUserName(c.getString(c.getColumnIndex(ACCOUNT_NAME)));
            account.setStringHash(c.getString(c.getColumnIndex(HASH_STRING)));
        }catch (CursorIndexOutOfBoundsException e){
            return  null;
        }

        Log.d(LOG, "returning:" + account.getUserName());

        return account;
    }

    /*
    *Return all created grocery lists names, in a String []
    */
    public ArrayList<String> getAllGroceryListNames(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> groceryListNames = new ArrayList<>();

        String selectQuery = "SELECT " + GROCERY_ITEM_LIST_NAME + " FROM " + TABLE_GROCERIES +
                ";";

        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()){
            do {
                groceryListNames.add(c.getString(c.getColumnIndex(GROCERY_ITEM_LIST_NAME)));
            }while(c.moveToNext());
        }
        c.close();
        //Remove duplicates by creating a Set from the ArrayList
        Set<String>uniqueNames = new HashSet<>(groceryListNames);
        ArrayList<String> returnList = new ArrayList<>();
        returnList.addAll(uniqueNames);
        return returnList;
    }

    /*
    *Return a GroceryItem with a certain name
    */
    public GroceryItem getGrocery(String groceryName){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_GROCERIES + " WHERE " + GROCERY_NAME
                + " = '" + groceryName + "';";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null)
            c.moveToFirst();

        GroceryItem item = new GroceryItem();
        item.setAmount(c.getString(c.getColumnIndex(GROCERY_AMOUNT)));
        item.setName(c.getString(c.getColumnIndex(GROCERY_NAME)));
        item.setPrice(c.getString(c.getColumnIndex(GROCERY_PRICE)));

        return item;
    }

    //Create a grocery_list method
    public long createGrocery_List(GroceryList list){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GROCERY_LIST_NAME, list.getName());
        values.put(KEY_CREATED_AT, getDateTime());

        long grocery_list_id = db.insert(TABLE_GROCERY_LIST, null, values);
        Log.e("db","grocery_list created");
        return grocery_list_id;
    }

    // Delete a grocery_list method

    public void deleteGrocery_list(String listName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GROCERY_LIST, GROCERY_LIST_NAME + " = " + listName, null);
    }

    // close database
    public void closeDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        if ( db != null && db.isOpen())
            db.close();
    }

    //helper method with time
    private String getDateTime(){

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public boolean getIsAccount() {
        return isAccount;
    }

    private void setIsAccount(boolean isAccount) {
        this.isAccount = isAccount;
    }
}
