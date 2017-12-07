package com.knatola.GroceryApp.Networking;

import android.os.AsyncTask;
import android.util.Log;

import com.knatola.GroceryApp.Data_Models.GroceryItem;
import com.knatola.GroceryApp.Data_Models.GroceryList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by knatola on 2.12.2017.
 */

public class Client  {

    private static final String LOG = "CLIENT: ";
    /*private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.101:8080/")
                .client(client)
                .build();
        return retrofit;
    }*/

    public Client(){

    }

    /*private static final String LOG = "CLIENT:";

    private ArrayList<GroceryItem> groceryItems = new ArrayList<>();
    private ArrayList<GroceryList> groceryLists = new ArrayList<>();

    public ArrayList<GroceryList>getGroceryLists(Request req){
       // ArrayList<GroceryList> lists = new ArrayList<>();



        return this.groceryLists;
    }

    public ArrayList<GroceryItem> getGroceryList(Request req){
        //ArrayList<GroceryItem> list = new ArrayList<>();

        new Worker(req).execute();

        return this.groceryItems;
    }
    public class Worker extends AsyncTask<Void, Void, Void >{

        private Request request;

        public Worker(Request req){
            this.request = req;
        }

        public void setRequest(Request request) {
            this.request = request;
        }

        public Request getRequest() {
            return request;
        }

        @Override
        protected Void doInBackground(Void...params) {
            Request req = getRequest();
            try {
                URL url = new URL(req.getUrl());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod(req.getType());
                con.connect();

                BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String jsonStr = (bf.readLine());
                Log.d(LOG, jsonStr);

                //code to create array
                ArrayList<GroceryItem> theList = new ArrayList<>();

                setGroceryItems(theList);
            } catch (MalformedURLException e) {
                Log.e(LOG, "URL PROBLEM");
            } catch (IOException e) {
                Log.e(LOG, "CONNECTION PROBLEM");
            }

            return null;
        }
    }

    public void setGroceryItems(ArrayList<GroceryItem> groceryItems) {
        this.groceryItems = groceryItems;
    }

    public void setGroceryLists(ArrayList<GroceryList> groceryLists) {
        this.groceryLists = groceryLists;
    }*/

    public ArrayList <GroceryItem> getGroceryList(Request request){
        ArrayList<GroceryItem> items = new ArrayList<>();

        try {
            URL url = new URL(request.getUrl());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(request.getType());
            con.setConnectTimeout(2000);
            con.connect();

            BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String jsonStr = (bf.readLine());
            Log.d("network:", "IT WORKS:"+ jsonStr);

            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String price = jsonObject.getString("price");
                int amount = jsonObject.getInt("count");
                String groceryListName = jsonObject.getString("groceryListName");
                GroceryItem item = new GroceryItem(id, name, price, amount, groceryListName);
                items.add(item);
            }
            bf.close();
            con.disconnect();

        } catch (MalformedURLException e) {
            Log.e(LOG, "Failure with URL");
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOG, "Failure with network");
        } catch (JSONException e) {
            Log.e(LOG, "JSON ERROR");
        }

    return items;
    }
}
