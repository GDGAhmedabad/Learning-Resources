package com.rocky.rxretrofit.support.retrofit.network;

import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rocky.rxretrofit.BuildConfig;
import com.rocky.rxretrofit.support.base.CoreApp;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ApiClient {

    private static final int DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB
    private static final int OKHTTP_TIMEOUT = 60; // seconds
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient;

    /**
     * You can create multiple methods for different BaseURL
     *
     * @return {@link Retrofit} object
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(WebService.BaseLink + WebService.Version)
                    .client(getOKHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }


    /**
     * settings like caching, Request Timeout, Logging can be configured here.
     *
     * @return {@link OkHttpClient}
     */
    private static OkHttpClient getOKHttpClient() {
        if (okHttpClient == null) {
            Cache cache = new Cache(new File(CoreApp.getInstance().getCacheDir(), "http")
                    , DISK_CACHE_SIZE);
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(OKHTTP_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(OKHTTP_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(OKHTTP_TIMEOUT, TimeUnit.SECONDS)
                    .cache(cache);

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(loggingInterceptor);
            }

//            Code for basic Authentication
            if (!TextUtils.isEmpty(WebService.API_USERNAME) && !TextUtils.isEmpty(WebService.API_PASSWORD)) {
                String credentials = WebService.API_USERNAME + ":" + WebService.API_PASSWORD;
                final String basic =
                        "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

                builder.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Authorization", basic)
                                .header("Accept", "application/json")
                                .method(original.method(), original.body());

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                });
            }
            okHttpClient = builder.build();
        }
        return okHttpClient;
    }

    public interface WebService {
        String BaseLink = "http://api.androidhive.info";
        String Version = "";
        String API_USERNAME = null;
        String API_PASSWORD = null;
    }
}