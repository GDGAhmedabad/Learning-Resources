package com.rocky.rxretrofit.support.retrofit;

import android.text.TextUtils;

import com.rocky.rxretrofit.support.utils.AppLog;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MultiPartUtil {
    /**
     * Converts File into Multipart Object
     *
     * @param fileParameter parameter name which goes into APIs
     * @return {@link okhttp3.MultipartBody.Part}
     */
    public static MultipartBody.Part getMultiPartImage(String fileParameter, File file) {
        if (file != null)
            return MultipartBody.Part.createFormData(fileParameter, file.getName(),
                    RequestBody.create(MediaType.parse("image/*"), file));
        return null;
    }

    /**
     * Converts File into Multipart Object
     *
     * @param fileParameter parameter name which goes into APIs
     * @param filepath      Absolute path of the file
     * @return {@link okhttp3.MultipartBody.Part}
     */
    public static MultipartBody.Part getMultiPartImage(String fileParameter, String filepath) {
        if (!TextUtils.isEmpty(filepath)) {
            File file = new File(filepath);
            try {
                return getMultiPartImage(fileParameter, file);
            } catch (Exception e) {
                AppLog.log(false, "MultiPartUtil " + "getMultiPartImage: ", e);
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * @return RequestBody of given String.
     */
    public static RequestBody getMultipartString(String string) {
        return RequestBody.create(MediaType.parse("text/plain"), string);
    }
}
