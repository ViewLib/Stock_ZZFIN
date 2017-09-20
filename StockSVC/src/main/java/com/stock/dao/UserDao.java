package com.stock.dao;


import com.stock.model.model.StockUserModel;

/**
 * Created by xiangleiliu on 2017/5/25.
 */
public interface UserDao {

    //用户
    public int insertStockUserModel(StockUserModel stockUserModel);

    public boolean updateStockUserModel(StockUserModel stockUserModel);//以userId为准

    public boolean deleteStockUserModel(int userId);

    public StockUserModel selectStockUserModel(int userId);

    public StockUserModel selectStockUserModel(String moblie);


}
