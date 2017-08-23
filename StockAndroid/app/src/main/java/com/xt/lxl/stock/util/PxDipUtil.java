package com.xt.lxl.stock.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by xiangleiliu on 2017/8/5.
 */
public class PxDipUtil {

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }

    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5F);
    }

    public static int getPixelFromDip(DisplayMetrics dm, float dip) {
        return (int) (TypedValue.applyDimension(1, dip, dm) + 0.5F);
    }

    public static float getDesity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }

}
