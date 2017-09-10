package com.stock.util;

/**
 * Created by xiangleiliu on 2017/2/13.
 */
public class Logger {
    private static Logger logger;

    private Logger() {
    }

    public static synchronized Logger getLogger() {
        if (logger == null) {
            logger = new Logger();
        }
        return logger;
    }

    public void showMessage(String string) {
        System.out.println(string);
    }

}
