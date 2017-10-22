package com.demo.retrofit.network;

import android.content.Context;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

class MyOkHttpBuilder {
    // Cache size for the OkHttpClient
    private static final int HTTP_DISK_CACHE_SIZE = 30 * 1024 * 1024; // 30 MB

    static OkHttpClient.Builder getOkHttpBuilder(Context context) {
//        Install an HTTP cache in the application cache directory.
        File cacheDir = new File(context.getCacheDir(), "http");
        Cache cache = new Cache(cacheDir, HTTP_DISK_CACHE_SIZE);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cache(cache);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor).addInterceptor(new MockInterceptor(context));

        return builder;
    }

}
