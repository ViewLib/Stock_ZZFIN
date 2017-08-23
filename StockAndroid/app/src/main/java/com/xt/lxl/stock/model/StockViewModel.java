package com.xt.lxl.stock.model;

/**
 * Created by xiangleiliu on 2017/8/5.
 */
public class StockViewModel implements Cloneable {

    //展示状态 头部 股票行 底部
    public static final int STOCK_SHOW_TYPE_NORMAL = 0;
    public static final int STOCK_SHOW_TYPE_FIRST = 1;
    public static final int STOCK_SHOW_TYPE_BOTTOM = 2;

    //股票状态
    public static final int STOCK_STATE_NORMAL = 0;
    public static final int STOCK_STATE_SUSPENSION = 1;

    public static final String STOCK_TYPE_CHINA = "China";
    public static final String STOCK_TYPE_US = "US";

    public int mShowType = STOCK_SHOW_TYPE_NORMAL;
    public String mStockName = "";
    public String mStockPirce = "";
    public String mStockPriceUS = "";//当前价格，美元
    public String mStockCode = "";//股票代码
    public String mStockType = "";//股票类型 美股orA股
    public String mStockSubType = "";//股票类型， 沪主板/深主板/创业板/中小板
    public double mStockChange = 0;//当前股票涨跌幅
    public int mStockState = STOCK_STATE_NORMAL;//股票状态


    public StockViewModel() {

    }

    public StockViewModel(int showType) {
        mShowType = showType;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new StockViewModel();
    }
}
