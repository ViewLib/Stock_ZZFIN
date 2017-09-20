package com.stock.dao;

import com.stock.model.model.StockSearchModel;

import java.util.List;

public interface StockDao {
    /**
     * 查询排行列表
     * @return
     */
    public List<StockSearchModel> selectSerchModelRankList(int showType,int limit);

}
