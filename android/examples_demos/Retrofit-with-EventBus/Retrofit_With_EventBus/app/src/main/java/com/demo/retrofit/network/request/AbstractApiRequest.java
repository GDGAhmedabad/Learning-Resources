package com.demo.retrofit.network.request;


import android.content.Context;

import com.demo.retrofit.RetroFitApp;
import com.demo.retrofit.network.ApiService;
import com.demo.retrofit.utils.Helper;

/**
 * Base class for the API requests. Provides functionality for cancelling ApiService requests.
 */
public abstract class AbstractApiRequest {
    /**
     * The endpoint for executing the calls.
     */
    protected final ApiService apiService;

    /**
     * Identifies the request.
     */
    protected final String tag;

    protected Context context;

    /**
     * Initialize the request with the passed values.
     *
     * @param apiService The {@link ApiService} used for executing the calls.
     * @param tag        Identifies the request.
     */
    protected AbstractApiRequest(ApiService apiService, String tag) {
        this.apiService = apiService;
        this.tag = tag;
        context = RetroFitApp.getApp();
    }

    /**
     * Cancels the running request. The response will still be delivered but will be ignored. The
     * implementation should call invalidate on the callback which is used for the request.
     */
    public abstract void cancel();

    /**
     * Check for active internet connection
     *
     * @return boolean
     */
    boolean isInternetActive() {
        return Helper.isInternetActive(context);
    }

    public abstract void execute();

}
