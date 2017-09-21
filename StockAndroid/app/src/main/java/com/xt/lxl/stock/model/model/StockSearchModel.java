package com.xt.lxl.stock.model.model;

/**
 * Created by Administrator on 2017/9/19 0019.
 * 搜索的model
 * 包含
 * StockFoundRankModel 排行
 * StockViewModel 股票
 */

public class StockSearchModel {
    public final static int STOCK_FOUND_TYPE_STOCK = 1;//股票
    public final static int STOCK_FOUND_TYPE_RNAK = 2;//事件性排行

    public final static int STOCK_SHOW_TYPE_TOP10 = 1;//top10
    public final static int STOCK_SHOW_TYPE_HOTSEARCH = 2;//热搜事件

    public int searchType = STOCK_FOUND_TYPE_STOCK;
    public int showType = STOCK_SHOW_TYPE_TOP10;//展示类型
    public int searchWeight = 0;//权重
    public StockViewModel stockViewModel = new StockViewModel();//股票
    public StockFoundRankModel rankModel = new StockFoundRankModel();
}
