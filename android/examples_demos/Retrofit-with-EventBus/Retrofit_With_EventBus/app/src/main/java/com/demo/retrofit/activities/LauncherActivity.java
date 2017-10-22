package com.demo.retrofit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by deardhruv on 26/03/17.
 */

public class LauncherActivity extends AppCompatActivity {

    // launcher screen timer
    private static int SPLASH_TIME_OUT = 1000;

    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler();
        runnable = () -> {
            Intent home_activity = new Intent(LauncherActivity.this, MainActivity.class);
            startActivity(home_activity);
            finish();
        };
        handler.postDelayed(runnable, SPLASH_TIME_OUT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(runnable);
    }
}