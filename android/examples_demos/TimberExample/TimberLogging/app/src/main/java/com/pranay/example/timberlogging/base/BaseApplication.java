package com.pranay.example.timberlogging.base;

import android.app.Application;

import com.pranay.example.timberlogging.BuildConfig;
import com.pranay.example.timberlogging.logutil.ReleaseTimberTree;

import timber.log.Timber;

/**
 * Created by Pranay.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            /**
             * Simple init Timber for Logging
             */
            //For Debug Build
            Timber.plant(new Timber.DebugTree());

            /**
             Customized Timber for Logging
             */
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element)
                            + ":" +
                            element.getLineNumber()// Add Line Number in Log
                            //+ " :" + element.getFileName()// Add File Name in Log
                            + " MethodName-> " + element.getMethodName()//Add Method Name in log
                            ;
                }
            });
        } else {
            // For Release Build
            // You can init your crash reporting tools here like:
            // Fabric.with(this, new Crashlytics());
            Timber.plant(new ReleaseTimberTree());
        }
    }
}
