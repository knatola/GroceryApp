package com.knatola.GroceryApp.Data_Models;


/*
 * Created by Juho on 9.1.2018.
 * Java Pojo class for the user account.
 * Accounts are identified with a SHA-256 token.
 */

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Account {
    private String userName;
    private String stringHash;

    public Account(String userName, String password){
        this.userName = userName;
        this.stringHash = createHash(userName, password);
    }

    public Account(){}

    public String createHash(String userName, String password){
        String concated = userName + password;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] h = digest.digest(concated.getBytes(StandardCharsets.UTF_8));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < h.length; i++) {
                String hex = Integer.toHexString(0xff & h[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStringHash() {
        return stringHash;
    }

    public void setStringHash(String stringHash) {
        this.stringHash = stringHash;
    }
}
