package com.stock.dao;


import com.stock.model.model.StockUserModel;
import com.stock.model.viewmodel.StockRankSQLViewModel;
import com.stock.model.viewmodel.StockSearchRankViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/5/25.
 */
public interface UserDao {

    //用户
    int insertStockUserModel(StockUserModel stockUserModel);

    boolean updateStockUserModel(StockUserModel stockUserModel);//以userId为准

    boolean deleteStockUserModel(int userId);

    StockUserModel selectStockUserModel(int userId);

    StockUserModel selectStockUserModel(String moblie);

    ArrayList<StockUserModel> selectUserInfoList(int startIndex, int endIndex);

    int getUserTotalCount();

    /**
     * 返回search_id
     *
     * @param stockSearchRankViewModel
     * @return
     */
    int insertSearchRankSetting(StockSearchRankViewModel stockSearchRankViewModel);

    /**
     * 返回search_relation
     *
     * @param stockRankSQLViewModel
     * @return
     */
    int insertSearchRankSql(StockRankSQLViewModel stockRankSQLViewModel);

    boolean updateSearchRankSetting(StockSearchRankViewModel stockSearchRankViewModel);

    boolean updateSearchRankSql(StockRankSQLViewModel stockRankSQLViewModel);

    StockSearchRankViewModel selectSearchRankSetting(int searchRelation);

    StockRankSQLViewModel selectSearchRankSql(int searchRelation);

    List<StockSearchRankViewModel> selectSearchRankSettingByCount(int count);
}
