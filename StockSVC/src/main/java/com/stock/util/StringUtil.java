package com.stock.util;

/**
 * Created by xiangleiliu on 2017/8/5.
 */
public class StringUtil {

    public static boolean emptyOrNull(String str) {
        return str == null || str.length() == 0;
    }

    public static int toInt(String s) {
        boolean i = false;

        int i1;
        try {
            i1 = Integer.parseInt(s);
        } catch (Exception var3) {
            i1 = -1;
        }
        return i1;
    }

    public static Float toFloat(String floatStr) {
        try {
            return Float.parseFloat(floatStr);
        } catch (Exception e) {

        }
        return 0f;
    }

    public static Long toLong(String floatStr) {
        if (floatStr.contains(".")) {
            floatStr = floatStr.split("\\.")[0];
        }
        try {
            return Long.parseLong(floatStr);
        } catch (Exception e) {

        }
        return 0l;
    }

}
