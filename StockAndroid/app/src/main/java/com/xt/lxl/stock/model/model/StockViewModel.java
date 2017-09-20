package com.xt.lxl.stock.model.model;

import java.io.Serializable;

/**
 * Created by xiangleiliu on 2017/8/5.
 */
public class StockViewModel implements Cloneable, Serializable {

    //展示类型  头部 股票行 底部
    public static final int STOCK_SHOW_TYPE_NORMAL = 0;
    public static final int STOCK_SHOW_TYPE_FIRST = 1;
    public static final int STOCK_SHOW_TYPE_BOTTOM = 2;

    //股票状态
    public static final int STOCK_STATE_NORMAL = 0;//正常
    public static final int STOCK_STATE_SUSPENSION = 1;//停牌

    public static final String STOCK_TYPE_CHINA = "China";//A股
    public static final String STOCK_TYPE_US = "US";//美股

    public static final String STOCK_SUB_TYPE_SH_MAIN = "sh_main";//上海主板
    public static final String STOCK_SUB_TYPE_SZ_MAIN = "sz_main";//深圳主板
    public static final String STOCK_SUB_TYPE_SZ_LITTLE = "sz_little";//深圳主板
    public static final String STOCK_SUB_TYPE_SZ_GEM = "sz_main";//创业板


    /**
     * 以下信息直接录入
     */
    public String mStockName = "";
    public String mStockPirce = "";
    public String mStockCode = "";//股票代码
    public String mStockType = STOCK_TYPE_CHINA;//股票类型 美股orA股
    public String mStockChange = "";//涨跌幅 string类型
    public String mRatio = "";//市盈率
    public String mTurnover = "";//换手率
    public String mValueAll = "";//总市值
    public boolean isSuspension = false;//是否停牌

    /**
     * 以下信息init方法生成
     */
    public String mStockPriceUS = "";//当前价格，美元
    public int mShowType = STOCK_SHOW_TYPE_NORMAL;
    public double mStockChangeD = 0;//当前股票涨跌幅
    public int mStockState = STOCK_STATE_NORMAL;//股票状态
    public String mStockSubType = "";//股票类型， 沪主板/深主板/创业板/中小板

    public StockViewModel() {

    }

    public StockViewModel(int showType) {
        mShowType = showType;
    }

    public StockViewModel(String stockCode, String stockName) {
        mStockCode = stockCode;
        mStockName = stockName;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new StockViewModel();
    }

    public void init() {
        //涨跌幅
        this.mStockChangeD = Double.parseDouble(mStockChange) / 100;
        if (isSuspension) {

        }
        //板块类型
        if (mStockCode.startsWith("6")) {
            mStockSubType = STOCK_SUB_TYPE_SH_MAIN;
        } else if (mStockCode.startsWith("000")) {
            mStockSubType = STOCK_SUB_TYPE_SZ_MAIN;
        } else if (mStockCode.startsWith("3")) {
            mStockSubType = STOCK_SUB_TYPE_SZ_GEM;
        } else {
            mStockSubType = STOCK_SUB_TYPE_SZ_LITTLE;
        }
    }
}
