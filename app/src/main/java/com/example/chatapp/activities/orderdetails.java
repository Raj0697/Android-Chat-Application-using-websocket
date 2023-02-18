package com.example.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.HelperClasses.CardHelper;
import com.example.chatapp.HelperClasses.CardAdapter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.example.chatapp.Presenter.ILoginPresenter;
import com.example.chatapp.Presenter.LoginPresenter;
import com.example.chatapp.R;
import com.example.chatapp.View.ILoginView;

public class orderdetails extends AppCompatActivity implements CardAdapter.ListItemClickListener, ILoginView {

    RecyclerView itemRecycler;
    RecyclerView.Adapter adapter;
    Button next;
    EditText name, mobile;
    TextView datetime;
    RelativeLayout relativeLayout;
    //boolean isallfields = false;
    //Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String dateandtime;
    ILoginPresenter loginPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);

        loginPresenter = new LoginPresenter(this);

        itemRecycler = findViewById(R.id.my_recycler);
        name = findViewById(R.id.edittext_name);
        mobile = findViewById(R.id.edittext_mob);
        datetime = findViewById(R.id.textview_date);
        relativeLayout = findViewById(R.id.relative);
        next = findViewById(R.id.nextbtn);
        cardRecycler();

        LocalDate localDate = LocalDate.now().plusDays(3);
        LocalDateTime localDateTime = LocalDateTime.now().plusHours(3);

        simpleDateFormat = new SimpleDateFormat(localDate+"", Locale.getDefault());

        dateandtime = simpleDateFormat.format(new Date());
        //datetime.setText(dateandtime);
        datetime.setText(dateandtime+ " And time slot between 10am - 4pm");

        next.setOnClickListener(v->{
            loginPresenter.onLogin(name.getText().toString(), mobile.getText().toString(), CardAdapter.increasecount, isConnected());
        });
    }

    // method to fetch the data connectivity of the device and notify the user incase they forgot to switch on the data
    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wificon = connectivityManager.getActiveNetworkInfo();
        NetworkInfo mobilecon = connectivityManager.getActiveNetworkInfo();

        //if(wificon != null && wificon.isConnected() ||(mobilecon != null && mobilecon.isConnected())) {
        if(wificon != null && wificon.getType() == ConnectivityManager.TYPE_WIFI ||(mobilecon != null && mobilecon.getType() == ConnectivityManager.TYPE_MOBILE)) {
            return true;
        }
        else {
            return false;
        }
    }

    // method to hide the keyboard when clicking outside the widget
    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if(getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    private void cardRecycler() {

        //All Gradients
        GradientDrawable gradient2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffd4cbe5, 0xffd4cbe5});
        GradientDrawable gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff7adccf, 0xff7adccf});
        GradientDrawable gradient3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xfff7c59f, 0xFFf7c59f});
        GradientDrawable gradient4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffb8d7f5, 0xffb8d7f5});

        itemRecycler.setHasFixedSize(true);
        itemRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<CardHelper> carddetails = new ArrayList<>();
        carddetails.add(new CardHelper(gradient1, R.drawable.nothing, "Nothing",0));
        carddetails.add(new CardHelper(gradient2, R.drawable.iphone, "Apple",0));
        carddetails.add(new CardHelper(gradient3, R.drawable.oppo, "Oppo",0));
        carddetails.add(new CardHelper(gradient4, R.drawable.pixel, "Pixel",0));
        adapter = new CardAdapter(carddetails,this);
        itemRecycler.setAdapter(adapter);

    }

    @Override
    public void oncardListClick(int clickedItemIndex) {
//        switch (clickedItemIndex){
//            case 0:
//                itemname = "nothing";
//                Toast.makeText(this,"you have selected "+itemname+ " mobile",Toast.LENGTH_SHORT).show();
//                break;
//            case 1:
//                itemname = "apple";
//                Toast.makeText(this,"you have selected "+itemname+ " mobile",Toast.LENGTH_SHORT).show();
//                break;
//            case 2:
//                itemname = "oppo";
//                Toast.makeText(this,"you have selected "+itemname+ " mobile",Toast.LENGTH_SHORT).show();
//                break;
//            case 3:
//                itemname = "pixel";
//                Toast.makeText(this,"you have selected "+itemname+ " mobile",Toast.LENGTH_SHORT).show();
//                break;
//        }
    }

    @Override
    public void onLoginSuccess(String message) {
        Intent i = new Intent(this, chatroom.class);
        i.putExtra("item", CardAdapter.itemname);
        i.putExtra("name",name.getText().toString());
        i.putExtra("mobile",mobile.getText().toString());
        i.putExtra("date",datetime.getText().toString());
        startActivity(i);
        //onButtonShowPopupWindowClick();
    }

    @Override
    public void onLoginError(String message) {
        if(message.contains("name"))
            name.setError(message);
        if(message.contains("mobile"))
            mobile.setError(message);
        if(message.contains("order"))
            Toast.makeText(this,message+" 1️⃣",Toast.LENGTH_SHORT).show();
        if(message.contains("internet"))
            Toast.makeText(this,message+" 😞",Toast.LENGTH_SHORT).show();
    }
}