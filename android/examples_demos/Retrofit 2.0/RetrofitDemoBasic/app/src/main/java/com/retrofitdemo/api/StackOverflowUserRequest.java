package com.retrofitdemo.api;

import com.retrofitdemo.api.response.StackOverflowUser;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StackOverflowUserRequest {
    @GET("users")
    Call<StackOverflowUser> getUserList(@Query("order") String order,@Query("sort") String sort,@Query("site") String site);
}
