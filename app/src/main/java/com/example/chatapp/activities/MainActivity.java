package com.example.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.chatapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Handlers are basically background threads which allows you to communicate with the UI thread
        new Handler().postDelayed(() -> {
            Intent i = new Intent(MainActivity.this,orderdetails.class);
            startActivity(i);
            finish();
        }, 2500);
    }
}