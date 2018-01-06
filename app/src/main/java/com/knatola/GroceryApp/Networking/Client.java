package com.knatola.GroceryApp.Networking;

/*
 * Created by knatola on 2.12.2017.
 */

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.knatola.GroceryApp.Data_Models.GroceryItem;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class Client  {

    private static final String LOG = "CLIENT: ";
    private static final AsyncHttpClient asyncClient = new AsyncHttpClient();
    private static boolean returnType;

    public Client(){
    }

    public static void getGroceryList(String url, RequestParams params, final String groceryListName,
                                      final GetCallback<ArrayList<GroceryItem>> callback){
        Log.d(LOG, "GET: " + url);
        String finalUrl = url + groceryListName;
        asyncClient.get(finalUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                final ArrayList<GroceryItem> groceryList = new ArrayList<>();
                Log.d(LOG, "onSuccess");
                String responseString = new String (responseBody);
                Log.d(LOG, responseString);
                try {
                    JSONArray array = null;
                    try {
                        array = new JSONArray(responseString);
                        for (int i = 0; i <array.length(); i++){
                            JSONObject jsonObject = array.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String name  = jsonObject.getString("name");
                            String price = jsonObject.getString("price");
                            String amount = jsonObject.getString("amount");
                            GroceryItem item = new GroceryItem(name, price, amount, Integer.getInteger(id),
                                    groceryListName);
                            groceryList.add(item);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(LOG, "JSON exception");
                    }
                }finally {
                    Log.d(LOG, "success");
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(LOG, "GET Failed");
            }
        });
    }
    /*todo
    * authentication here
    * */
    public static boolean postGroceryList(String url, Context context, RequestParams params, ArrayList<GroceryItem> list){
        Log.d(LOG, "TRYING POST");
            Gson gson = new Gson();
            String json = gson.toJson(list);
            try{
                StringEntity entity = new StringEntity(json);
                entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                asyncClient.post(context, url, entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d(LOG, "POST SUCCESS");
                        setReturnType(true);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d(LOG,"POST FAILED");
                        setReturnType(false);
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.d(LOG,"UnsupportedEncodingException");
            }
        return getReturnType();
    }

    public interface GetCallback<T>{
        void onResponse(T response);
    }

    public static boolean getReturnType() {
        return returnType;
    }

    public static void setReturnType(boolean returnType) {
        Client.returnType = returnType;
    }
}
