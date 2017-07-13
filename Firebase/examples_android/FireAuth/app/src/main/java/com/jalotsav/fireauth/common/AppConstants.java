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

/**
 * Created by jalotsav on 17/10/16.
 */

public interface AppConstants {

    // Log Tag key
    String LOG_TAG = "Jalotsav_FireAuth";

    // Log Type
    int LOGTYPE_VERBOSE = 1;
    int LOGTYPE_DEBUG = 2;
    int LOGTYPE_INFO = 3;
    int LOGTYPE_WARN = 4;
    int LOGTYPE_ERROR = 5;

    // Key for onActivityResult
    int REQUESTCODE_MAINACTVTY = 101;
    int REQUESTCODE_SIGN_IN_GOOGLE = 102;
}
