package com.xt.lxl.stock.util;

import java.text.DecimalFormat;

/**
 * Created by xiangleiliu on 2017/8/5.
 */
public class DataShowUtil {

    static DecimalFormat df = new DecimalFormat("######0.00");

    public static String getDisplayChangeStr(double change) {
        StringBuilder builder = new StringBuilder();
        if (change < 0) {
            builder.append("-");
            change = change * -1.0;
        }
        builder.append(df.format(change * 100));
        builder.append("%");
        return builder.toString();
    }
}
