package com.stock.service;

import com.stock.dao.UserDao;
import com.stock.dao.UserDaoImpl;
import com.stock.model.ServiceResponse;
import com.stock.model.StockUserModel;

/**
 * Created by xiangleiliu on 2017/9/6.
 */
public class UserService {
    UserDao dao;

    public static UserService userService;

    private UserService() {
        dao = new UserDaoImpl();
    }

    public static synchronized UserService getInstance() {
        if (userService == null) {
            userService = new UserService();
        }
        return userService;
    }

    /**
     * 注册用户
     */
    public StockUserModel registerUser(String moblie, String clientId) throws Exception {
        //首先查询这个手机号是否有注册用户
        StockUserModel selectModel = dao.selectStockUserModel(moblie);
        if (selectModel != null) {
            return selectModel;
        }
        selectModel = new StockUserModel();
        selectModel.mMoblie = moblie;
        selectModel.mClientId = clientId;
        int userId = dao.insertStockUserModel(selectModel);
        if (userId > 0) {
            selectModel.mUserId = userId;
            return selectModel;
        }
        //如果插入失败的话，则抛出异常
        throw new Exception("create user fail");
    }

    /**
     * 更新用户信息
     */
    public boolean updateUser(String userid, String moblie, String nickname, String area, String age) throws Exception {

        int userId = 0;
        try {
            userId = Integer.parseInt(userid);
        } catch (Exception e) {
        }
        if (userId < 10000000 || userId > 99999999) {
            throw new Exception("userId异常");
        }
        StockUserModel model = new StockUserModel();
        model.mUserId = Integer.parseInt(userid);
        model.mMoblie = moblie;
        model.mNickName = nickname;
        model.mArea = area;
        model.mAge = Integer.parseInt(age);
        return dao.updateStockUserModel(model);
    }

    public ServiceResponse result2Response(int code, String resultMessage, String result) {
        ServiceResponse response = new ServiceResponse();

        return response;
    }


}
