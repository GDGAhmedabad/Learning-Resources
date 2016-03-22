package com.retrofitdemo;

import android.app.Application;

import com.retrofitdemo.api.OkClientFactory;

import okhttp3.OkHttpClient;

public class App extends Application {

    private static App mInstance = null;
    private static OkHttpClient mOkHttpClient;

    public static App getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        mOkHttpClient = OkClientFactory.provideOkHttpClient(this);

    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }
}
