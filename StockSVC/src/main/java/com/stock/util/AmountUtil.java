package com.stock.util;

import java.math.BigDecimal;

/**
 * Created by xiangleiliu on 2017/10/29.
 */
public class AmountUtil {

    public Float roundedFor(Float value, int rounded) {
        BigDecimal b = new BigDecimal(value);
        value = b.setScale(rounded, BigDecimal.ROUND_HALF_UP).floatValue();//小数点后保留4位
        //异常情况
        return value;
    }

    public static String transHandFromAmount(String amout) {
        Long aLong = StringUtil.toLong(amout);
        if (aLong > 100000000L) {
            return aLong / 100000000 + "亿股";
        }
        if (aLong > 10000L) {
            return aLong / 10000 + "万股";
        }
        return aLong + "股";
    }

    public static String transRatioFromHave(String amout) {
        Float aFloat = StringUtil.toFloat(amout);
        if (aFloat >= 1) {
            return "100%";
        }
        return aFloat * 100 + "%";
    }

}
