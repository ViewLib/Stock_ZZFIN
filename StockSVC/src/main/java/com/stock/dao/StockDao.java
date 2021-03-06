package com.stock.dao;

import com.stock.model.model.*;
import com.stock.viewmodel.SQLViewModel;

import java.util.List;
import java.util.Map;

public interface StockDao {
    /**
     * 查询排行列表
     *
     * @return
     */
    public List<StockSearchModel> selectSerchModelRankList(int showType, int limit);

    List<StockSyncModel> selectSyncModelList(int version);

    public List<StockRankResultModel> selectRankDetailModelList(int version, String strsql);

    public List<StockDateDataModel> selectStocDetailkDataList(String stoclCode, String sqlCode);

    public int getHoliday(String p_date);

    public StockDetailCompanyModel getCompanyInfo(String stockCode);

    public List<StockDetailStockHolder> getStockHolder(String stockCode);

    public List<StockRankFilterGroupModel> getStockRankFilterGroup();//父级节点

    public List<StockRankFilterGroupModel> getStockRankFilterSubGroup(int parentFilterId);//二级节点

    public List<StockRankFilterItemModel> getAllStockRankFilterItem();//筛选节点

    public String getListSql(int filter_id);

    public List<StockDetailFinanceItem> getFinalList(String stockCode,int  FinanceType);
    public List<StockDetailFinanceItem> getYearlList(String stockCode,int  FinanceType);

    public List<SQLViewModel> getStockEventSQL(int type);

}
