package com.demo.retrofit.network;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

class MyOkHttpBuilder {
    // Cache size for the OkHttpClient
    private static final int HTTP_DISK_CACHE_SIZE = 30 * 1024 * 1024; // 30 MB

    @NonNull
    static OkHttpClient.Builder getOkHttpBuilder(@NonNull Context context) {
//        Install an HTTP cache in the application cache directory.
        File cacheDir = new File(context.getCacheDir(), "http");
        Cache cache = new Cache(cacheDir, HTTP_DISK_CACHE_SIZE);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cache(cache);
//        builder.followRedirects(false);

        return builder;
    }

}
