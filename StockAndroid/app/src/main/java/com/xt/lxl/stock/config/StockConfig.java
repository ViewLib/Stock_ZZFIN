package com.xt.lxl.stock.config;

/**
 * Created by xiangleiliu on 2017/8/7.
 */
public class StockConfig {
    public static final String STOCK_SAVE_DB_NAME = "STOCK_SAVE_DB_NAME";
    public static final String STOCK_SAVE_DATA_NAME = "STOCK_SAVE_DATA_NAME";//用户存储的股票代码集合
    public static final String STOCK_SAVE_DATA_HISTORY = "STOCK_SAVE_DATA_HISTORY";//用户历史搜索的关键词集合

    public static final String STOCK_USER_DB_NAME = "STOCK_USER_DB_NAME";
    public static final String STOCK_USER_DATA_USERID = "STOCK_SAVE_USERID";
    public static final String STOCK_USER_DATA_MOBLIE = "STOCK_USER_DATA_MOBLIE";
    public static final String STOCK_USER_DATA_NICKNAME = "STOCK_USER_DATA_NICKNAME";
    public static final String STOCK_USER_DATA_AREA = "STOCK_USER_DATA_AREA";
    public static final String STOCK_USER_DATA_AGE = "STOCK_USER_DATA_AGE";
    public static final String STOCK_USER_DATA_CREATETIME = "STOCK_USER_DATA_CREATETIME";


    //url地址
    public static boolean isTest = true;

    //test
    public static String URL_REGISTER_TEST = "";

    //release
    public static String URL_REGISTER = isTest ? URL_REGISTER_TEST : "";

    public static int INTERVAL_TIME = 10000;

}
