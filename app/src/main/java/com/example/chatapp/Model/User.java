package com.example.chatapp.Model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.example.chatapp.activities.orderdetails;

public class User implements IUser{

    private String name;
    private String mobile;
    private int quantity;
    private boolean isconnection;

    public User(String name, String mobile, int quantity, boolean isconnection)
    {
        this.name = name;
        this.mobile = mobile;
        this.quantity = quantity;
        this.isconnection = isconnection;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getMobile() {
        return mobile;
    }

    @Override
    public int getQuantity() { return quantity;}

    @Override
    public boolean isconnection() {
        return isconnection;
    }

    @Override
    public int isValidData() {
        //return !TextUtils.isEmpty(getName()) && Patterns.PHONE.matcher(getMobile()).matches() && getMobile().length() == 10;
        if(TextUtils.isEmpty(getName()))
            return 0;
        else if(getMobile().length() != 10)
            return 1;
        else if(getQuantity() == 0)
            return 2;
        else if(!isconnection())
            return 3;
        else
            return -1;
    }
}
