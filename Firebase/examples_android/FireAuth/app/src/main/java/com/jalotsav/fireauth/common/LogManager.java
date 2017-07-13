/*
 * Copyright 2016 Jalotsav
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jalotsav.fireauth.common;

import android.util.Log;

/**
 * Created by jalotsav on 17/10/16.
 */

public class LogManager implements AppConstants {

    public static void printLog(int logType, String logMessage){

        switch (logType) {
            case LOGTYPE_VERBOSE:
                Log.v(LOG_TAG, logMessage);
                break;
            case LOGTYPE_DEBUG:
                Log.d(LOG_TAG, logMessage);
                break;
            case LOGTYPE_INFO:
                Log.i(LOG_TAG, logMessage);
                break;
            case LOGTYPE_WARN:
                Log.w(LOG_TAG, logMessage);
                break;
            case LOGTYPE_ERROR:
                Log.e(LOG_TAG, logMessage);
                break;

            default:
                Log.i(LOG_TAG, logMessage);
                break;
        }
    }
}
