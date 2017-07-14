package com.demo.retrofit.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.demo.retrofit.network.event.RequestFinishedEvent;
import com.demo.retrofit.network.request.AbstractApiRequest;
import com.demo.retrofit.network.request.ImageListRequest;
import com.demo.retrofit.network.request.UploadImageRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Provides request functions for all api calls. This class maintains a map of running requests to
 * provide request cancellation.
 */

public class ApiClient {

    private static TimeUnit timeUnit = TimeUnit.SECONDS;
    private Retrofit retrofit;

    private static int HTTP_TIMEOUT = 30; // Seconds by default

    private static final String WS_SCHEME = "http://";
    private static final String WS_PREFIX_DOMAIN = "deardhruv.com";
    private static final String WS_HOSTNAME = "/";
    private static final String WS_SUFFIX_FOLDER = "api/";

    private static String API_BASE_URL = WS_SCHEME
            + WS_PREFIX_DOMAIN
            + WS_HOSTNAME
            + WS_SUFFIX_FOLDER;

    /**
     * Makes the ApiService calls.
     */
    private ApiService mApiService;

    /**
     * The list of running requests. Used to cancel requests.
     */
    private final Map<String, AbstractApiRequest> requests;

    private Context context;

    public ApiClient(Context context) {

        this.context = context;
        requests = new HashMap<>();
        EventBus.getDefault().register(this);

        initAPIClient();
    }

    private void initAPIClient() {

        OkHttpClient.Builder okBuilder = MyOkHttpBuilder.getOkHttpBuilder(context);

//		okBuilder.retryOnConnectionFailure(true);
//      okBuilder.followRedirects(false);

        OkHttpClient httpClient = okBuilder.connectTimeout(HTTP_TIMEOUT, timeUnit)
                .writeTimeout(HTTP_TIMEOUT, timeUnit)
                .readTimeout(HTTP_TIMEOUT, timeUnit)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder();

        retrofit = builder
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(API_BASE_URL)
                .client(httpClient)
                .build();

        mApiService = retrofit.create(ApiService.class);
    }

    private void changeApiBaseUrl(String newApiBaseUrl) {
        API_BASE_URL = newApiBaseUrl;
        initAPIClient();
    }

    public static int getHttpTimeout() {
        return HTTP_TIMEOUT;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public ApiService getApiService() {
        return mApiService;
    }

    @NonNull
    public Map<String, AbstractApiRequest> getRequests() {
        return requests;
    }

    // ============================================================================================
    // Request functions
    // ============================================================================================


    /**
     * Execute a request to retrieve the update message. See {@link ApiService#getImageList(String)} for
     * parameter details.
     *
     * @param requestTag The tag for identifying the request.
     */
    public void getImageList(String requestTag) {
        ImageListRequest request = new ImageListRequest(mApiService, requestTag);
        requests.put(requestTag, request);
        request.execute();
    }

    public void uploadImage(String requestTag, String file) {
        UploadImageRequest request = new UploadImageRequest(mApiService, requestTag, file);
        requests.put(requestTag, request);
        request.execute();
    }


    // ============================================================================================
    // Public functions
    // ============================================================================================

    /**
     * Look up the event with the passed tag in the event list. If the request is found, cancel it
     * and remove it from the list.
     *
     * @param requestTag Identifies the request.
     * @return True if the request was cancelled, false otherwise.
     */
    public boolean cancelRequest(String requestTag) {
        System.gc();
        AbstractApiRequest request = requests.get(requestTag);

        if (request != null) {
            request.cancel();
            requests.remove(requestTag);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if the request with the passed tag is in the list of running requests, false
     * otherwise.
     */
    public boolean isRequestRunning(String requestTag) {
        return requests.containsKey(requestTag);
    }


    /**
     * A request has finished. Remove it from the list of running requests.
     *
     * @param event The event posted on the EventBus.
     */
    @Subscribe
    public void onEvent(RequestFinishedEvent event) {
        System.gc();
        requests.remove(event.getRequestTag());

    }
}
