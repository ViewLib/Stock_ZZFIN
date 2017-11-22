package com.stock.controller.user;

import com.stock.controller.user.model.*;
import com.stock.dao.UserDao;
import com.stock.model.model.StockUserModel;
import com.stock.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杨蕾 on 2017/11/12.
 */
@RestController
@RequestMapping({"/user"})
public class UserManagerRestController {
    Logger logger = LoggerFactory.getLogger(UserManagerRestController.class);

    @Autowired
    private UserDao userDao;

    @RequestMapping({"/search"})
    @ResponseBody
    public SearchUserResponse searchUserPost(@RequestBody SearchUserRequest request) {
        SearchUserResponse result = new SearchUserResponse();
        List<StockUserModel> users = new ArrayList<>();
        if(StringUtil.isNotBlank(request.getUserid())){
            int userId = Integer.parseInt(request.getUserid());
            users.add(this.userDao.selectStockUserModel(userId));
            result.setRows(users);
            result.setTotal(1);
            return result;
        }

        if(StringUtil.isNotBlank(request.getMobile())){
            users.add(this.userDao.selectStockUserModel(request.getMobile()));
            result.setRows(users);
            result.setTotal(1);
            return result;
        }

        int pageIndex = request.getOffset() / request.getLimit() + 1;
        users = this.userDao.selectUserInfoList(pageIndex, request.getLimit());
        result.setRows(users);
        result.setTotal(this.userDao.getUserTotalCount());
        return result;
    }

    @RequestMapping({"/update"})
    @ResponseBody
    public Object updateUser(@RequestBody UserRequest request) {
        UserResponse response = new UserResponse();
        response.setResultCode(500);
        response.setShowMessage("更新失败，请稍后重试");

        if (request == null || request.getUserId() <= 0) {
            response.setResultCode(400);
            response.setShowMessage("请求不合法,UserId不正确");
            return response;
        }

        try {
            StockUserModel stockUserModel = this.getStockUserModel(request);
            boolean isSuccess = this.userDao.updateStockUserModel(stockUserModel);
            if (isSuccess) {
                response.setResultCode(200);
                response.setShowMessage("更新成功");
            }
        } catch (Exception ex) {
            response.setResultMessage(ex.getMessage());
            logger.warn(ex.getMessage(), ex);
        }

        return response;
    }

    @RequestMapping({"/insert"})
    @ResponseBody
    public Object insertUser(@RequestBody UserRequest request) {
        UserResponse response = new UserResponse();
        response.setResultCode(500);
        response.setShowMessage("新增失败，请稍后重试");

        if (request == null || !StringUtil.isNotBlank(request.getNickName()) || !StringUtil.isNotBlank(request.getMobile())) {
            response.setResultCode(400);
            response.setShowMessage("请求不合法，请填写必要的用户信息");
            return response;
        }

        try {
            StockUserModel stockUserModel = this.getStockUserModel(request);
            int userId = this.userDao.insertStockUserModel(stockUserModel);
            if (userId > 0) {
                response.setResultCode(200);
                response.setShowMessage("新增成功");
                response.setUserId(userId);
            }
        } catch (Exception ex) {
            response.setResultMessage(ex.getMessage());
            logger.warn(ex.getMessage(), ex);
        }

        return response;
    }

    @RequestMapping({"/delete"})
    @ResponseBody
    public Object deleteUser(@RequestBody UserRequest request) {
        UserResponse response = new UserResponse();
        response.setResultCode(500);
        response.setShowMessage("删除失败，请稍后重试");

        if (request == null || request.getUserId() <= 0) {
            response.setResultCode(400);
            response.setShowMessage("请求不合法，UserId不正确");
            return response;
        }

        try {
            boolean isSuccess = this.userDao.deleteStockUserModel(request.getUserId());
            if (isSuccess) {
                response.setResultCode(200);
                response.setShowMessage("删除成功");
            }
        } catch (Exception ex) {
            response.setResultMessage(ex.getMessage());
            logger.warn(ex.getMessage(), ex);
        }

        return response;
    }

    @RequestMapping({"/getpagecount"})
    @ResponseBody
    public Object getPageCount(@RequestBody PageCountRequest request) {
        if (request == null) {
            return 0;
        }

        int userTotalCount = this.userDao.getUserTotalCount();
        return (int) Math.ceil(userTotalCount / request.getPageSize());
    }

    private StockUserModel getStockUserModel(UserRequest request) {
        StockUserModel stockUserModel = new StockUserModel();
        stockUserModel.setUserId(request.getUserId());
        stockUserModel.setAge(request.getAge());
        stockUserModel.setArea(request.getArea());
        stockUserModel.setMoblie(request.getMobile());
        stockUserModel.setNickName(request.getNickName());

        return stockUserModel;
    }
}
