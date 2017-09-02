package com.xt.lxl.stock.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.Toast;

/**
 * Created by xiangleiliu on 2017/8/5.
 */
public class StockShowUtil {

    public static void showToastOnMainThread(final Context context, final CharSequence msg) {
        if (TextUtils.isEmpty(msg) || context == null) {
            return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static int getPixelFromDip(Context context, float f) {
        return getPixelFromDip(context.getResources().getDisplayMetrics(), f);
    }

    public static int getPixelFromDip(DisplayMetrics dm, float dip) {
        return (int) (TypedValue.applyDimension(1, dip, dm) + 0.5F);
    }


}
