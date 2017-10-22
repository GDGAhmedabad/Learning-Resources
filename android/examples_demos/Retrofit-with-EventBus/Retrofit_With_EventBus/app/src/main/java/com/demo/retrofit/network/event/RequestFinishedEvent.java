package com.demo.retrofit.network.event;


import com.demo.retrofit.network.ApiClient;

/**
 * Post this event on the EventBus to tell the {@link ApiClient}
 * that a request has finished. This will remove the request from the list of running requests.
 */
public class RequestFinishedEvent {
    /**
     * Identifies the request.
     */
    private final String requestTag;

    /**
     * Initialize the event with the passed tag.
     *
     * @param requestTag Identifies the request.
     */
    public RequestFinishedEvent(String requestTag) {
        this.requestTag = requestTag;
    }

    public String getRequestTag() {
        return requestTag;
    }

}
