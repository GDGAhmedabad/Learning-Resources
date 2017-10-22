package com.demo.retrofit.network.event;

/**
 * When any network error happens, Event is used to send to particular class from where the Network API call generated.
 */
public class ApiErrorEvent {
    private final String requestTag;
    private Throwable throwable;

    /**
     * Create an error event without any further error information.
     *
     * @param requestTag Identifies the request that failed.
     */
    public ApiErrorEvent(String requestTag) {
        this.requestTag = requestTag;
    }

    /**
     * Create an error event with detailed error information in the form of a RetrofitError.
     *
     * @param requestTag Identifies the request that failed.
     * @param t          An error object with detailed information.
     */
    public ApiErrorEvent(String requestTag, Throwable t) {
        this.requestTag = requestTag;
        this.throwable = t;
    }

    public String getRequestTag() {
        return requestTag;
    }

    public Throwable getRetrofitError() {
        return throwable;
    }
}
