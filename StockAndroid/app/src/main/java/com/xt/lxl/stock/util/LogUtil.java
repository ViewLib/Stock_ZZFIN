package com.xt.lxl.stock.util;

import android.util.Log;

/**
 * Created by xiangleiliu on 2017/8/23.
 */
public class LogUtil {
    final static String TAG = "lxltest";

    public static void LogE(String message) {
        Log.e(TAG, "E_" + message);
    }

    public static void LogI(String message) {
        Log.e(TAG, "I_" + message);
    }

}
