package com.rocky.rxretrofit.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rocky.rxretrofit.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openListActivity(View view) {
        startActivity(new Intent(this, ListActivity.class));
    }

    public void openSingleActivity(View view) {
        startActivity(new Intent(this, SingleActivity.class));
    }
}
