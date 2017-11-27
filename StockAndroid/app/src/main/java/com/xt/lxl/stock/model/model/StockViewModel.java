package com.xt.lxl.stock.model.model;

/**
 * Created by xiangleiliu on 2017/8/5.
 */
public class StockViewModel extends StockBaseModel {

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
    public String stockName = "";
    public String stockPirce = "";
    public String stockCode = "";//股票代码
    public String stockType = STOCK_TYPE_CHINA;//股票类型 美股orA股
    public String stockChangeValue = "";//涨跌幅 绝对值 string类型
    public String stockChange = "";//涨跌幅% string类型
    public String ratio = "";//市盈率
    public String turnover = "";//换手率
    public String valueAll = "";//总市值
    public String maxPrice = "";//今日最高价
    public String minPrice = "";//今日最低价
    public String amplitude = "";//振幅
    public String volume = "";//成交量 手
    public String dealValue = "";//成交值 万
    public String stockBasePirce = "";//昨日收盘价
    public boolean isSuspension = false;//是否停牌

    /**
     * 以下信息init方法生成
     */
    public String stockPriceUS = "";//当前价格，美元
    public int showType = STOCK_SHOW_TYPE_NORMAL;
    public double stockChangeD = 0;//当前股票涨跌幅
    public int stockState = STOCK_STATE_NORMAL;//股票状态
    public String stockSubType = "";//股票类型， 沪主板/深主板/创业板/中小板

    public StockViewModel() {

    }

    public StockViewModel(int showType) {
        this.showType = showType;
    }

    public StockViewModel(String stockCode, String stockName) {
        stockCode = stockCode;
        this.stockName = stockName;
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
        this.stockChangeD = Double.parseDouble(stockChange) / 100;
        if (isSuspension) {

        }
        //板块类型
        if (stockCode.startsWith("6")) {
            stockSubType = STOCK_SUB_TYPE_SH_MAIN;
        } else if (stockCode.startsWith("000")) {
            stockSubType = STOCK_SUB_TYPE_SZ_MAIN;
        } else if (stockCode.startsWith("3")) {
            stockSubType = STOCK_SUB_TYPE_SZ_GEM;
        } else {
            stockSubType = STOCK_SUB_TYPE_SZ_LITTLE;
        }
    }

    public String getRequestStockCode() {
        if (stockCode.startsWith("6")) {
            return "sh" + stockCode;
        }
        return "sz" + stockCode;
    }


}
