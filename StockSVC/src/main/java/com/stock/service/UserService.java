package com.stock.service;

import com.stock.dao.UserDao;
import com.stock.dao.UserDaoImpl;
import com.stock.model.model.StockUserModel;
import com.stock.model.request.StockUserCompletionRequest;
import com.stock.model.request.StockUserRegisterRequest;
import com.stock.model.response.StockUserCompletionResponse;
import com.stock.model.response.StockUserRegisterResponse;

import java.util.ArrayList;
import java.util.List;

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
    public StockUserModel registerUser(StockUserRegisterRequest stockUserRegisterRequest, StockUserRegisterResponse registerResponse) throws Exception {
        if (!stockUserRegisterRequest.moblie.contains("_")) {
            stockUserRegisterRequest.moblie = "86_" + stockUserRegisterRequest.moblie;
        }
        String moblie = stockUserRegisterRequest.moblie;
        //首先查询这个手机号是否有注册用户
        StockUserModel selectModel = dao.selectStockUserModel(moblie);
        if (selectModel != null) {
            return selectModel;
        }
        selectModel = new StockUserModel();
        selectModel.setMoblie(moblie);
        selectModel.setClientId(stockUserRegisterRequest.clientId);
        int userId = dao.insertStockUserModel(selectModel);
        if (userId > 0) {
            selectModel.setUserId(userId);
            return selectModel;
        }
        //如果插入失败的话，则抛出异常
        throw new Exception("create user fail");
    }

    /**
     * 更新用户信息
     */
    public StockUserModel updateUser(StockUserCompletionRequest completionRequest, StockUserCompletionResponse completionResponse) throws Exception {
//        String userid, String moblie, String nickname, String area, String age
        int userId = 0;
        userId = completionRequest.userid;
        if (userId < 10000000 || userId > 99999999) {
            throw new Exception("userId异常");
        }
        StockUserModel model = new StockUserModel();
        model.setUserId(userId);
        model.setMoblie(completionRequest.moblie);
        model.setNickName(completionRequest.nickname);
        model.setArea(completionRequest.area);
        model.setAge(completionRequest.age);

        if (dao.updateStockUserModel(model)) {
            return model;
        } else {
            throw new Exception("修改数据失败");
        }
    }

    public ArrayList<StockUserModel> selectUserInfo(String pageStr) {
        //每页20条
        int page = Integer.parseInt(pageStr);
        //int startIndex = (page - 1) * 20;
        ArrayList<StockUserModel> stockUserModels = dao.selectUserInfoList(page, 5);
        return stockUserModels;
    }
}
