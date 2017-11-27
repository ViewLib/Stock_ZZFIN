package com.xt.lxl.stock.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import java.math.BigDecimal;

/**
 * author：ajiang
 * mail：1025065158@qq.com
 * blog：http://blog.csdn.net/qqyanjiang
 */
public class StockUtil {

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

    public static Float roundedFor(Float value, int rounded) {
        BigDecimal b = new BigDecimal(value);
        value = b.setScale(rounded, BigDecimal.ROUND_HALF_UP).floatValue();//小数点后保留4位
        //异常情况
        return value;
    }

    public static String getVolUnit(float num) {
        int e = (int) Math.floor(Math.log10(num));
        if (e >= 8) {
            return "亿手";
        } else if (e >= 4) {
            return "万手";
        } else {
            return "手";
        }
    }

    /**
     * 输入单位万
     *
     * @param value
     * @return
     */
    public static String getDealValue(String value) {
        try {
            float i = Float.parseFloat(value);
            if (i > 9999) {
                return roundedFor((i / 10000f), 2) + "亿";
            }
            return value + "万";
        } catch (Exception e) {

        }
        return value + "万";
    }

    public static float culcMaxscale(float count) {
        float max = 1;
        max = count / 127 * 5;
        return max;
    }

    public static String calculationValue(String baseStr, String addStr) {
        float v = StringUtil.toFloat(baseStr) + StringUtil.toFloat(addStr);
        return String.valueOf(roundedFor(v, 2));
    }
}
