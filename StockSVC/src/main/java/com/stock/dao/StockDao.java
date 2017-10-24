package com.stock.dao;

import com.stock.model.model.*;

import java.util.List;

public interface StockDao {
    /**
     * 查询排行列表
     * @return
     */
    public List<StockSearchModel> selectSerchModelRankList(int showType, int limit);

    List<StockSyncModel> selectSyncModelList(int version);

    public List<StockRankResultModel> selectRankDetailModelList(int version);

    public List<StockRankFilterModel> selectStockFilterList(int first_type);

    public List<StockFirstTypeModel> selectStockFirstTypList(int version);

    public List<StockDateDataModel> selectStocDetailkDataList(String stoclCode, String sqlCode);

    public int getHoliday(String p_date);

    public StockDetailCompanyModel getCompanyInfo(String stockCode);

    public List<StockDetailStockHolder> getStockHolder(String stockCode);

    public List<StockRankFilterGroupModel> getStockRankFilterGroup(int first_type);
}
