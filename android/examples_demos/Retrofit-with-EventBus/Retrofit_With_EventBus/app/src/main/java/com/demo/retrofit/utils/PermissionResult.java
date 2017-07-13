package com.demo.retrofit.utils;

/**
 * Created by deardhruv on 26/03/17.
 * <p>
 * Result of the requested Application Permission to User
 */
public interface PermissionResult {

    void permissionGranted();

    void permissionDenied();

    void permissionForeverDenied();
}
