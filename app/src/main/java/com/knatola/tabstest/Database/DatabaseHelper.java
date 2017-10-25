package com.knatola.tabstest.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import com.knatola.tabstest.Data.GroceryItem;
import com.knatola.tabstest.Data.GroceryList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by knatola on 24.10.2017.
 */


public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String LOG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG ="Database";

    public static final String DATABASE_NAME = "groceries.db";
    public static final String TABLE_GROCERIES = "groceries";
    public static final String TABLE_GROCERY_LIST= "grocery_list";

    //common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    //GROCERIES table column names
    private static final String GROCERY_NAME = "grocery_name";
    private static final String GROCERY_AMOUNT = "amount";
    private static final String GROCERY_PRICE = "price";
    private static final String GROCERY_ITEM_LIST_NAME = "list";

    //grocery_list table column names
    private static final String GROCERY_LIST_NAME = "grocery_list_name";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG,"database Constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_GROCERIES);
        db.execSQL(CREATE_TABLE_GROCERY_LIST);

        Log.d(TAG,"database onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROCERIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROCERY_LIST);

        onCreate(db);
    }

    // Table create statements
    // Groceries table statement
    private static final String CREATE_TABLE_GROCERIES = "CREATE TABLE " + TABLE_GROCERIES
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + GROCERY_NAME + " TEXT," + GROCERY_AMOUNT
            + " TEXT," + GROCERY_PRICE + " TEXT," + GROCERY_ITEM_LIST_NAME + " TEXT," + KEY_CREATED_AT
            + " DATETIME" + ")";

    private static final String CREATE_TABLE_GROCERY_LIST = "CREATE TABLE " + TABLE_GROCERY_LIST
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + GROCERY_LIST_NAME + " TEXT," + KEY_CREATED_AT
            + " DATETIME" + ")";

    // CRUD STATEMENTS
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

        //Assigning grocery to a grocery_list
        //createGrocery_List(grocery_id);
        Log.d(TAG,"grocery created");

        return grocery_id;
    }

    //method to destroy a grocery item
    public void deleteGrocery(String groceryName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GROCERIES, GROCERY_NAME + " = " + groceryName, null);
    }

    // returning a list of groceries by grocery_list name
    // SELECT*FROM groceries WHERE name = listName;
    public List<GroceryItem> getGroceryList(String listName){
        SQLiteDatabase db = this.getReadableDatabase();
        List<GroceryItem> groceryItems = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_GROCERIES + "WHERE " + GROCERY_ITEM_LIST_NAME +
                " = " + listName;
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

}
