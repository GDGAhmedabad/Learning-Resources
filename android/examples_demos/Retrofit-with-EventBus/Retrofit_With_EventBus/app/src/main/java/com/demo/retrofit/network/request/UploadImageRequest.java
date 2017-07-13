package com.demo.retrofit.network.request;

import com.demo.retrofit.R;
import com.demo.retrofit.network.ApiCallback;
import com.demo.retrofit.network.ApiService;
import com.demo.retrofit.network.response.AbstractApiResponse;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by deardhruv on 26/03/17.
 */
public class UploadImageRequest extends AbstractApiRequest {

    private String imageFile;

    /**
     * + The callback used for this request. Declared globally for cancellation. See {@link
     * #cancel()}.
     */
    ApiCallback<AbstractApiResponse> callback;

    /**
     * To cancel REST API call from Retrofit. See {@link #cancel()}.
     */
    private Call<AbstractApiResponse> call;

    /**
     * Initialize the request with the passed values.
     *
     * @param apiService
     * @param tag
     * @param imageFile
     */
    public UploadImageRequest(ApiService apiService,
                              String tag,
                              String imageFile) {
        super(apiService, tag);
        this.imageFile = imageFile;
    }

    public void execute() {
        callback = new ApiCallback<>(tag);
        if (!isInternetActive()) {
            callback.postUnexpectedError(context.getString(R.string.error_no_internet));
            return;
        }

        MediaType mediaType = MediaType.parse("multipart/form-data");
        MultipartBody.Part body;
        RequestBody requestFile;
        File file = new File(imageFile);

        requestFile = RequestBody.create(mediaType, file);
        body = MultipartBody.Part.createFormData("file",
                file.getName(),
                requestFile);

        call = apiService.uploadFile(context.getResources().getString(R.string.api_upload), "", "", body);
        call.enqueue(callback);
    }

    @Override
    public void cancel() {
        callback.invalidate();
        call.cancel();
    }

}
