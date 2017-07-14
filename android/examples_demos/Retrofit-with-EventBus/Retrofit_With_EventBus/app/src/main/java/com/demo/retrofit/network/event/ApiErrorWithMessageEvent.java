package com.demo.retrofit.network.event;

/**
 * When Server error happens, Event is used to send to particular class from where the Network API call generated.
 */
public class ApiErrorWithMessageEvent {

    private final String requestTag;
    private final String resultMsgUser;

    public ApiErrorWithMessageEvent(String requestTag, String resultMsgUser) {
        this.requestTag = requestTag;
        this.resultMsgUser = resultMsgUser;
    }

    public String getRequestTag() {
        return requestTag;
    }

    public String getResultMsgUser() {
        return resultMsgUser;
    }
}
