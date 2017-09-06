package com.stock.service;

import com.stock.dao.UserDao;
import com.stock.dao.UserDaoImpl;
import com.stock.model.StockUserModel;

/**
 * Created by xiangleiliu on 2017/9/6.
 */
public class UserService {
    UserDao dao;

    public UserService() {
        dao = new UserDaoImpl();
    }

    public boolean registerUser(String moblie, String clientId) {
        StockUserModel model = new StockUserModel();
        model.mMoblie = moblie;
        model.mClientId = clientId;
        return dao.insertStockUserModel(model);
    }
}
