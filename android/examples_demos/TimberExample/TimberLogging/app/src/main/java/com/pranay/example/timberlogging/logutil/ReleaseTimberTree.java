package com.pranay.example.timberlogging.logutil;

import android.util.Log;

import timber.log.Timber.Tree;

/**
 * Created by Pranay.
 */

public class ReleaseTimberTree extends Tree {
    private static final int MAX_LOG_LENGTH = 4000;


    @Override
    protected boolean isLoggable(String tag, int priority) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return false;
        }

        // In Case of Release Build User can only get Log for : Log.WARN, Log.ERROR, wtf

        return true;
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if (isLoggable(tag, priority)) {


            //Report to Caught Exception to Crash Reporting tools Like Crashlytics ( Or other Tools)
            if (priority == Log.ERROR && tag != null) {
                //Crashlytics.log(priority,tag,message);
                //Crashlytics.log(message);
            }


            //Message is short enough, no need to be broken in chunk
            if (message.length() < MAX_LOG_LENGTH) {
                if (priority == Log.ASSERT) {
                    Log.wtf(tag, message);
                } else {
                    Log.println(priority, tag, message);
                }
                return;
            }

            //Split by line then ensure each line can  fit into Log's Maximum length
            for (int i = 0, length = message.length(); i < length; i++) {
                int newLine = message.indexOf('\n', i);
                newLine = newLine != -1 ? newLine : length;
                do {
                    int end = Math.min(newLine, i + MAX_LOG_LENGTH);
                    String messagePart = message.substring(i, end);
                    if (priority == Log.ASSERT) {
                        Log.wtf(tag, messagePart);
                    } else {
                        Log.println(priority, tag, messagePart);
                    }
                    i = end;
                } while (i < newLine);
            }
        }
    }


}
