package com.rocky.rxretrofit.support.utils;

import android.support.annotation.NonNull;
import android.util.Log;

public class AppLog {
    public final static int V = 4, D = 1, E = 2, I = 3;
    public static final String TAG = AppLog.class.getSimpleName();
    private static boolean isDebug = true;

    public static void log(int logLevel, boolean isLocal, String tag, @NonNull String message) {
        if (tag == null || tag.equals("")) {
            tag = TAG;
        }
        switch (logLevel) {
            case D:
//                if (!isLocal) Crashlytics.log(Log.DEBUG, tag, message);
                if (isDebug) Log.d(tag, message);
                break;
            case V:
//                if (!isLocal) Crashlytics.log(Log.VERBOSE, tag, message);
                if (isDebug) Log.v(tag, message);
                break;
            case E:
//                if (!isLocal) Crashlytics.log(Log.ERROR, tag, message);
                if (isDebug) Log.e(tag, message);
                break;
            case I:
//                if (!isLocal) Crashlytics.log(Log.INFO, tag, message);
                if (isDebug) Log.i(tag, message);
                break;
            default:
//                if (!isLocal) Crashlytics.log(Log.WARN, tag, message);
                if (isDebug) Log.v(tag, message);
        }
    }

    public static void log(boolean isLocal, @NonNull String message) {
        log(D, isLocal, TAG, message);
    }

    public static void log(boolean isLocal, String location, @NonNull Exception exception) {
        log(E, isLocal, TAG, location + exception.getLocalizedMessage());
    }

    public static void log(boolean isLocal, String location, @NonNull Throwable throwable) {
        log(E, isLocal, TAG, location + throwable.getLocalizedMessage());
    }
}