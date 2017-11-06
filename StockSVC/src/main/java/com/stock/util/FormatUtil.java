package com.stock.util;

/**
 * Created by xiangleiliu on 2017/11/6.
 * 针对不同的数据格式化
 */
public class FormatUtil {

    //去除日期中的空格后面的
    public static String forDataStr(String date) {
        if (StringUtil.emptyOrNull(date)) {
            return "";
        }
        if (date.contains(" ")) {
            return date.split(" ")[0];
        }
        return date;
    }

    //去除股票中.后面的，比如机构持股数量为：281593528.0000股转换为281593528
    public static String formatStockNum(String stockNum) {
        if (StringUtil.emptyOrNull(stockNum)) {
            return "";
        }
        if (stockNum.contains(".")) {
            return stockNum.split("\\.")[0];
        }
        return stockNum;
    }
}
