package com.stock.dao;

<<<<<<< 5f862bbdc97978a49add55ac544af87a46844980
import com.stock.model.model.StockRankResultModel;
import com.stock.model.model.StockSearchModel;
import com.stock.model.model.StockSyncModel;
=======
import com.stock.model.model.*;
>>>>>>> filter

import java.util.List;

public interface StockDao {
    /**
     * 查询排行列表
     * @return
     */
    public List<StockSearchModel> selectSerchModelRankList(int showType,int limit);

    List<StockSyncModel> selectSyncModelList(int version);

    public List<StockRankResultModel> selectRankDetailModelList(int version);

    public List<StockRankFilterModel> selectStockFilterList(int filter_type);
}
