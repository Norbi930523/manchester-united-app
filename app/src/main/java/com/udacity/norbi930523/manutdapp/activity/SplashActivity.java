package com.udacity.norbi930523.manutdapp.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/*
* Based on https://android.jlelse.eu/right-way-to-create-splash-screen-on-android-e7f1709ba154
* */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Runnable startApp = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, NewsActivity.class));

                finish();
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(startApp, 1000);

    }
}
