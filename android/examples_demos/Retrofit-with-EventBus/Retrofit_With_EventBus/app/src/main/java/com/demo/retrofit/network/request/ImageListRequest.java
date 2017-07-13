package com.demo.retrofit.network.request;

import com.demo.retrofit.R;
import com.demo.retrofit.network.ApiCallback;
import com.demo.retrofit.network.ApiService;
import com.demo.retrofit.network.response.ImageListResponse;

import retrofit2.Call;

/**
 * Use this request to retrieve the image list via the ImageListResponse api call.
 */
public class ImageListRequest extends AbstractApiRequest {

    /**
     * The callback used for this request. Declared globally for cancellation. See {@link
     * #cancel()}.
     */
    private ApiCallback<ImageListResponse> callback;
    /**
     * To cancel REST API call from Retrofit. See {@link #cancel()}.
     */
    private Call<ImageListResponse> call;

    /**
     * See super constructor {@link AbstractApiRequest#AbstractApiRequest(ApiService, String)}.
     */
    public ImageListRequest(ApiService apiService, String tag) {
        super(apiService, tag);
    }

    /**
     * Executes the request asynchronously using the built-in mechanism of Retrofit. The api
     * response is posted on the EventBus.
     */
    public void execute() {
        callback = new ApiCallback(tag);

        if (!isInternetActive()) {
            callback.postUnexpectedError(context.getString(R.string.error_no_internet));
            return;
        }
        call = apiService.getImageList(context.getResources().getString(R.string.api_image_list));
        call.enqueue(callback);
    }

    @Override
    public void cancel() {
        callback.invalidate();
        call.cancel();
    }

}