package com.xt.lxl.stock.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xiangleiliu on 2017/9/7.
 */
public class FormatUtil {

    private final static String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    private final static String FORMAT_YYYY_MM_DD_HH_MM = "yyyyMM-dd HH:mm";
    private final static String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static String DATE_2_YYYY_MM_DD(Date data) {
        if (data == null || data.getTime() == 0) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_YYYY_MM_DD, Locale.CHINA);
        return formatter.format(data);
    }

    public static double[] getLongAndLat(String input_coordinate) {
        double[] coordinate = new double[2];
        try {
            String[] split = input_coordinate.split("_");
            if (split != null && split.length >= 2) {
                coordinate[0] = Double.parseDouble(split[0]);
                coordinate[1] = Double.parseDouble(split[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coordinate;
    }

    public static int string2Int(String str) {
        try {
            int i = Integer.parseInt(str.trim());
            return i;
        } catch (Exception e) {

        }
        return 0;
    }
}
