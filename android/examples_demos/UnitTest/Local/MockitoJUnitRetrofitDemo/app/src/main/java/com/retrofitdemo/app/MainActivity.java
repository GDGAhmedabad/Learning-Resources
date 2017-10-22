package com.retrofitdemo.app;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

public class MainActivity extends AppCompatActivity implements MainActivityView{

    private RecyclerView recycler_view;

    MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager lLayout = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(lLayout);

        presenter = new MainActivityPresenter(this);

        presenter.getSOUserData();
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

    @Override
    public void getSOUserData(String order, String sortBy, String site) {
        StackOverflowUserRequest userDataRequest = ApiFactory.provideSOUserDataRequest();
        Call<StackOverflowUser> getUserCall = userDataRequest.
                getUserList(order, sortBy, site);

        getUserCall.enqueue(new Callback<StackOverflowUser>() {
            @Override
            public void onResponse(Call<StackOverflowUser> call,
                                   Response<StackOverflowUser> response) {
                presenter.proceedUserResponse(response);
            }

            @Override
            public void onFailure(Call<StackOverflowUser> call, Throwable t) {
                presenter.proceedUserResponse(null);
            }
        });
    }

    @Override
    public void setUserData(List<StackOverflowUser.Item> body) {
        StackOverflowUserAdapter mAdapter = new StackOverflowUserAdapter(MainActivity.this, body);
        recycler_view.setAdapter(mAdapter);
    }

    @Override
    public String getOrder() {
        return "desc";
    }

    @Override
    public void showEmptyOrderError(int error_empty_order) {
        Snackbar.make(recycler_view, getString(error_empty_order), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public String getSortBy() {
        return "reputation";
    }

    @Override
    public void showEmptySortByError(int error_empty_sortby) {
        Snackbar.make(recycler_view, getString(error_empty_sortby), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public String getSite() {
        return "stackoverflow";
    }

    @Override
    public void showEmptySiteError(int error_empty_site) {
        Snackbar.make(recycler_view, getString(error_empty_site), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showUserDataError(int error_no_response) {
        Snackbar.make(recycler_view, getString(error_no_response), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyUserList(int error_empty_user_list) {
        Snackbar.make(recycler_view, getString(error_empty_user_list), Snackbar.LENGTH_SHORT).show();
    }
}
