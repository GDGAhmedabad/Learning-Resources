package com.demo.retrofit.network;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

class MockInterceptor implements Interceptor {

    private final Context mContext;

    MockInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        final Response response;
        String responseString;
        // Get Request URL pathSegments.
        List<String> paths = chain.request().url().pathSegments();
        final String endPoint = paths.get(paths.size() - 1);

        try {
            responseString = RestServiceMockUtils.getStringFromFile(mContext, endPoint);
        } catch (IOException | Resources.NotFoundException e) {
            responseString = "File is missing or failed to open it."; // this is malformed json string.
            e.printStackTrace();
        }
        response = new Response.Builder()
                .code(200)
                .message(responseString)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
                .addHeader("content-type", "application/json")
                .build();
        return response;
    }
}
