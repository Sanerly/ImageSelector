package com.imgselector.uitl;

import android.util.Log;


public class LogUtil {

    private static final String TAG = "SANER_LOG";

    public static void logd(String str) {
        Log.d(TAG, str);
    }

    public static void loge(String str) {
        Log.e(TAG, str);
    }

    public static void logi(String str) {
        Log.i(TAG, str);
    }
}
