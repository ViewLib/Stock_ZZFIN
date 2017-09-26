package com.stock.dao;

import com.stock.model.model.StockRankDetailModel;
import com.stock.model.model.StockRankResultModel;
import com.stock.model.model.StockSearchModel;
import com.stock.model.model.StockSyncModel;

import java.util.List;

public interface StockDao {
    /**
     * 查询排行列表
     * @return
     */
    public List<StockSearchModel> selectSerchModelRankList(int showType,int limit);

    List<StockSyncModel> selectSyncModelList(int version);

    public List<StockRankResultModel> selectRankDetailModelList(int version);
}
