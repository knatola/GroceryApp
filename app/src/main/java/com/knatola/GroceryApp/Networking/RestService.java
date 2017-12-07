package com.knatola.GroceryApp.Networking;

import com.knatola.GroceryApp.Data_Models.GroceryItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by knatola on 3.12.2017.
 */

public interface RestService {

    @Headers("Accept: Application/json")
    @GET("groceries/{listName}")
    Call <List<GroceryItem>> getGroceryList(@Path("listName") String listName);

    @Headers("Content-Type: Application/json")
    @POST("groceries")
    Call <GroceryItem> setGroceryItem(@Body GroceryItem groceryItem);

    @Headers("Content-Type: Application/json")
    @POST("groceries/{listName")
    Call <List<GroceryItem>> setGroceryList(@Path("listName") String listName);
}
