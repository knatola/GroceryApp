package com.knatola.GroceryApp.Networking;

/**
 * Created by knatola on 2.12.2017.
 */

public class Request {
    private String type;
    private String url;
    private String listName;

    public Request(String type, String url, String listName){
        this.type = type;
        this.url = url;
        this.listName = listName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
