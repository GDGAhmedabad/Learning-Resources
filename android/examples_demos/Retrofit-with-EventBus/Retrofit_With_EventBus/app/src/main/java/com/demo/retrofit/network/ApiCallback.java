package com.demo.retrofit.network;


import android.text.TextUtils;

import com.demo.retrofit.network.event.ApiErrorEvent;
import com.demo.retrofit.network.event.ApiErrorWithMessageEvent;
import com.demo.retrofit.network.event.RequestFinishedEvent;
import com.demo.retrofit.network.response.AbstractApiResponse;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Use this class to have a callback which can be used for the api calls in {@link ApiService}.
 * Such a callback can be invalidated to not notify its caller about the api response.
 * Furthermore it handles finishing the request after the caller has handled the response.
 */
public class ApiCallback<T extends AbstractApiResponse> implements Callback<T> {

    /**
     * Indicates if the callback was invalidated.
     */
    private boolean isInvalidated;

    /**
     * The tag of the request which uses this callback.
     */
    private final String requestTag;

    /**
     * Creates an {@link ApiCallback} with the passed request tag. The tag is used to finish
     * the request after the response has been handled. See {@link #finishRequest}.
     *
     * @param requestTag The tag of the request which uses this callback.
     */
    public ApiCallback(String requestTag) {
        isInvalidated = false;
        this.requestTag = requestTag;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (isInvalidated || call.isCanceled()) {
            finishRequest();
            return;
        }
        T result = response.body();
        if (response.isSuccessful() && result != null) {
            if (0 == result.getStatus()) {
                // Error occurred. Check for error message from api.
                String resultMsgUser = result.getMessage();

                if (!TextUtils.isEmpty(resultMsgUser)) {
                    EventBus.getDefault()
                            .post(new ApiErrorWithMessageEvent(requestTag, resultMsgUser));

                } else {
                    EventBus.getDefault().post(new ApiErrorEvent(requestTag));
                }
            } else {
//			modifyResponseBeforeDelivery(result); // Enable when needed.
                result.setRequestTag(requestTag);
                EventBus.getDefault().post(result);
            }
        } else {
            // TODO: Move hardcode string
            EventBus.getDefault().post(
                    new ApiErrorWithMessageEvent(requestTag, "Server not available."));

/*

			//TODO: If the Network response code is not between (200..300) and error body is
			//similar to {@link AbstractApiResponse} then use below commented code.
            try {
                AbstractApiResponse abstractApiResponse = (AbstractApiResponse) ApiClient.getRetrofit().responseBodyConverter(
                        AbstractApiResponse.class,
                        AbstractApiResponse.class.getAnnotations())
                        .convert(response.errorBody());
                // Do error handling here

                EventBus.getDefault().post(
                        new ApiErrorWithMessageEvent(requestTag, abstractApiResponse.getMessage()));
//                return;

            } catch (IOException e) {
                e.printStackTrace();
            }
*/
        }
        finishRequest();
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (!call.isCanceled() && !isInvalidated) {
            EventBus.getDefault().post(new ApiErrorEvent(requestTag, t));
        }

        finishRequest();
    }

    /**
     * Invalidates this callback. This means the caller doesn't want to be called back anymore.
     */
    public void invalidate() {
        isInvalidated = true;
    }

    /**
     * Posts a {@link RequestFinishedEvent} on the EventBus to tell the {@link ApiClient}
     * to remove the request from the list of running requests.
     */
    private void finishRequest() {
        EventBus.getDefault().post(new RequestFinishedEvent(requestTag));
    }

    /**
     * This is for callbacks which extend ApiCallback and want to modify the response before it is
     * delivered to the caller. It is bit different from the interceptors as it allows to implement
     * this method and change the response.
     *
     * @param result The api response.
     */
    @SuppressWarnings("UnusedParameters")
    protected void modifyResponseBeforeDelivery(T result) {
        // Do nothing here. Only for subclasses.
    }

    /**
     * Call this method if No internet connection or other use.
     *
     * @param resultMsgUser User defined messages.
     */
    public void postUnexpectedError(String resultMsgUser) {
        EventBus.getDefault().post(new ApiErrorWithMessageEvent(requestTag, resultMsgUser));
        finishRequest();
    }
}
