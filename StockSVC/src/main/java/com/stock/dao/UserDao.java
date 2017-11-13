package com.stock.dao;


import com.stock.model.model.StockUserModel;

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
}
