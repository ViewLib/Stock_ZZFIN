package com.xt.lxl.stock.model.model;

/**
 * Created by Administrator on 2017/9/19 0019.
 * 搜索的model
 * 包含
 * StockFoundRankModel 排行
 * StockViewModel 股票
 */

public class StockSearchViewModel {
    public final static int STOCK_FOUND_TYPE_STOCK = 1;//股票
    public final static int STOCK_FOUND_TYPE_RNAK = 2;//事件性排行

    public int mSearchType = 1;
    public StockViewModel stockViewModel=new StockViewModel();//股票
    public StockFoundRankModel rankModel=new StockFoundRankModel("");
}
