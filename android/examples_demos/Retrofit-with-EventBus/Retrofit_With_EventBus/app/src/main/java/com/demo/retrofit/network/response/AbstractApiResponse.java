package com.demo.retrofit.network.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * The abstract base class for all possible API responses. It contains all elements which are common
 * to all API responses.
 */
public class AbstractApiResponse implements Serializable {

    // 0 or 1
    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("message")
    @Expose
    private String message;

    /**
     * Identifies the request which was executed to receive this response. The tag is used to make
     * sure that a class which executes a requests only handles the response which is meant for it.
     * This implies that the tag is unique.
     */
    private String requestTag;

    /**
     * @return The status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public void setRequestTag(String requestTag) {
        this.requestTag = requestTag;
    }

    public String getRequestTag() {
        return requestTag;
    }

}
