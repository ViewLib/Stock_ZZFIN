package com.stock.dao;

import com.stock.model.model.StockRankDetailModel;

import java.util.List;
import com.stock.model.model.StockRankResultModel;

/**
 * Created by hp on 2017/9/26.
 */
public interface StockLinkDao {
    public List<StockRankResultModel> selectRankDetailModelList(int version);
}
