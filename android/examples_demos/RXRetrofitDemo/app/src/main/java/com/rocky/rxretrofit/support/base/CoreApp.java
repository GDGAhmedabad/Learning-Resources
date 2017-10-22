package com.rocky.rxretrofit.support.base;

import android.app.Application;


public class CoreApp extends Application {
    private static CoreApp mInstance;

    public static synchronized CoreApp getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}
