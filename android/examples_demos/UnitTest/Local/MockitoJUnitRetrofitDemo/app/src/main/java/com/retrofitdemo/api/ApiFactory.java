package com.retrofitdemo.api;


import com.retrofitdemo.App;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {

    private static String API_BASE_URL = "https://api.stackexchange.com/2.2/";

    private ApiFactory() {
    }

    private static Retrofit provideRestAdapter() {
        return new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(App.getInstance().getOkHttpClient())
                .build();
    }


    public static StackOverflowUserRequest provideSOUserDataRequest() {
        return provideRestAdapter().create(StackOverflowUserRequest.class);
    }
}
