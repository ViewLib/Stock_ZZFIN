package com.stock.service;

import com.stock.controller.user.model.SearchRankPageCountRequest;
import com.stock.controller.user.model.SearchRankUpdateRequest;
import com.stock.dao.UserDao;
import com.stock.dao.UserDaoImpl;
import com.stock.model.model.StockSearchModel;
import com.stock.model.model.StockUserModel;
import com.stock.model.request.StockUserCompletionRequest;
import com.stock.model.request.StockUserRegisterRequest;
import com.stock.model.response.StockUserCompletionResponse;
import com.stock.model.response.StockUserRegisterResponse;
import com.stock.model.viewmodel.StockRankSQLViewModel;
import com.stock.model.viewmodel.StockSearchRankViewModel;
import com.stock.util.DateUtil;
import org.jsoup.helper.DataUtil;

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

    public void updateStockSearchRank(SearchRankUpdateRequest request) {
        //更新逻辑
        StockSearchRankViewModel stockSearchRankViewModel;
        if (request.search_id > 0) {
            stockSearchRankViewModel = dao.selectSearchRankSetting(request.search_id);
        } else {
            stockSearchRankViewModel = new StockSearchRankViewModel();
        }
        stockSearchRankViewModel.show_type = request.show_type;
        stockSearchRankViewModel.search_type = request.search_type;
        stockSearchRankViewModel.search_title = request.search_title;
        stockSearchRankViewModel.search_desc = request.search_desc;
        stockSearchRankViewModel.search_relation = request.search_relation;
        stockSearchRankViewModel.search_weight = request.search_weight;
        //存在配置，则更新search_rank的配置
        if (stockSearchRankViewModel != null && stockSearchRankViewModel.search_id > 0) {
            dao.updateSearchRankSetting(stockSearchRankViewModel);
        } else {
            dao.insertSearchRankSetting(stockSearchRankViewModel);
        }
        //更新sql
        StockRankSQLViewModel stockRankSQLViewModel = dao.selectSearchRankSql(stockSearchRankViewModel.search_relation);
        stockRankSQLViewModel.rank_title = request.search_title;
        stockRankSQLViewModel.rank_sql = request.rank_sql;
        stockRankSQLViewModel.search_relation = request.search_relation;
        stockRankSQLViewModel.submission_date = DateUtil.getCurrentDate();
        //存在rank_sql，则更新
        if (stockRankSQLViewModel.rank_id > 0) {
            dao.updateSearchRankSql(stockRankSQLViewModel);
        } else {
            dao.insertSearchRankSql(stockRankSQLViewModel);
        }
    }

    public List<StockSearchRankViewModel> getSearchRankList(SearchRankPageCountRequest request) {
        List<StockSearchRankViewModel> stockSearchRankViewModelList = dao.selectSearchRankSettingByCount(request.startIndex, request.count);
        for (StockSearchRankViewModel stockSearchRankViewModel : stockSearchRankViewModelList) {
            if (stockSearchRankViewModel.show_type == StockSearchModel.STOCK_SHOW_TYPE_UNSHOW) {
                continue;
            }
            if (stockSearchRankViewModel.search_type == StockSearchModel.STOCK_SEARCH_TYPE_RNAK) {
                StockRankSQLViewModel stockRankSQLViewModel = dao.selectSearchRankSql(stockSearchRankViewModel.search_relation);
                stockSearchRankViewModel.stockRankSQLViewModel = stockRankSQLViewModel;
            }
        }
        return stockSearchRankViewModelList;
    }

    public int selectStockSearchRankCount() {
        int i = dao.selectStockSearchRankCount();
        return i;
    }

}
