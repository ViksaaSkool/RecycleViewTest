package com.recycleview.tests.utils;

import android.util.Log;

/**
 * Created by varsovski on 16-Mar-15.
 */
public class LogUtil {

    private static final boolean LOGGING = true; //set this to false if release version


    public static void dLog(String TAG, String message) {
        if (LOGGING)
            Log.d(TAG, message);
    }

    public static void eLog(String TAG, String message) {
        if (LOGGING)
            Log.e(TAG, message);
    }

    public static void wLog(String TAG, String message) {
        if (LOGGING)
            Log.w(TAG, message);
    }

    public static void iLog(String TAG, String message) {
        if (LOGGING)
            Log.i(TAG, message);
    }

}
