package com.xt.lxl.stock.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiangleiliu on 2017/9/14.
 */
public class MatchUtil {

    /**
     * 匹配是否全是数字
     * @param line
     * @return
     */
    public static boolean matchAllNumber(String line) {
        Pattern compile = Pattern.compile("^(\\d)*&");
        Matcher matcher = compile.matcher(line);
        return matcher.matches();
    }



}
