package com.retrofitdemo.app;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.retrofitdemo.adapter.StackOverflowUserAdapter;
import com.retrofitdemo.api.ApiFactory;
import com.retrofitdemo.api.StackOverflowUserRequest;
import com.retrofitdemo.api.response.StackOverflowUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager lLayout = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(lLayout);
        
        //Call here
        getSOUserData();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Call for getting StackOverflow user data
    private void getSOUserData() {
        StackOverflowUserRequest userDataRequest = ApiFactory.provideSOUserDataRequest();
        Call<StackOverflowUser> getUserCall = userDataRequest.
                getUserList("desc", "reputation", "stackoverflow");

        getUserCall.enqueue(new Callback<StackOverflowUser>() {
            @Override
            public void onResponse(Call<StackOverflowUser> call,
                                   Response<StackOverflowUser> response) {
                if (response.isSuccess()) {
                    setUserData(response.body().getItems());
                }
            }

            @Override
            public void onFailure(Call<StackOverflowUser> call, Throwable t) {

            }
        });
    }

    private void setUserData(List<StackOverflowUser.Item> body) {
        StackOverflowUserAdapter mAdapter = new StackOverflowUserAdapter(MainActivity.this, body);
        recycler_view.setAdapter(mAdapter);
    }

}
