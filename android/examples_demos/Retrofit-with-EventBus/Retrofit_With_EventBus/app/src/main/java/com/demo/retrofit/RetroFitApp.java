package com.demo.retrofit;

import android.app.Application;

import com.demo.retrofit.network.ApiClient;

/**
 * Application class
 * Initialization of {@link ApiClient} etc.
 */
public class RetroFitApp extends Application {

    private final String TAG = "RetroFitApp";
    private ApiClient mApiClient;
    private static RetroFitApp retroFitApp;

    @Override
    public void onCreate() {
        super.onCreate();
        retroFitApp = this;
    }

    public ApiClient getApiClient() {
        return mApiClient != null ? mApiClient : initApiClient();
    }

    /**
     * Initialize the {@link ApiClient}
     */
    private ApiClient initApiClient() {
        mApiClient = new ApiClient(this);
        return mApiClient;
    }

    public static RetroFitApp getApp() {
        return retroFitApp;
    }

}
