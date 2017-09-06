package com.xt.lxl.stock.config;

/**
 * Created by xiangleiliu on 2017/8/7.
 */
public class StockConfig {
    public static final String STOCK_SAVE_DB_NAME = "STOCK_SAVE_DB_NAME";

    public static final String STOCK_SAVE_DATA_NAME = "STOCK_SAVE_DATA_NAME";


    //url地址
    public static boolean isTest = true;

    //test
    public static String URL_REGISTER_TEST = "";

    //release
    public static String URL_REGISTER = isTest ? URL_REGISTER_TEST : "";

}
