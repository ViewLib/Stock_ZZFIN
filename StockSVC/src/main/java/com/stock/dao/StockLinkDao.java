package com.stock.dao;

import com.stock.model.model.StockDetailGradleModel;
import com.stock.model.model.StockRankDetailModel;

import java.util.List;
import java.util.Map;

import com.stock.model.model.StockRankResultModel;
import com.stock.model.model.StockViewModel;
import com.stock.viewmodel.SQLViewModel;
import com.stock.viewmodel.StoctEventSQLResultModel;

/**
 * Created by hp on 2017/9/26.
 */
public interface StockLinkDao {
    public List<StockRankResultModel> selectRankDetailModelList(int version);

    public List<StockDetailGradleModel> selectStockGradle(String stockCode);

    public List<String> selectCompareStockCodeList(String stockCode, String tradeDate);

    public List<StoctEventSQLResultModel> getStockEventBySQLModel(SQLViewModel sqlViewModel, String stockCode);

    /**
     * 查询最近交易日
     */
    public String selectLastTradeDate();

    /**
     * 查询市盈率
     */
    public Map<String, String> selectRatioByCodeList(List<String> stockInfoList, String tradeStr);

    /**
     * 收入增长
     */
    public Map<String, String> selectIncomeGrowthByCodeList(List<String> stockInfoList, String tradeStr);

    /**
     * 查询市盈率
     */
    public Map<String, String> selectStockPriceByCodeList(List<String> stockInfoList, String tradeStr);

    /**
     * 查询分红比例
     */
    public Map<String, String> selectShareOutByCodeList(List<String> stockInfoList, String tradeStr);

    /**
     * 获取年度第一个交易日
     */
    public String selectFirstTradeDate(String lastTradeDate);

    public Map<String, StockViewModel> selectStockByCode(List<String> stockInfoList);
}
