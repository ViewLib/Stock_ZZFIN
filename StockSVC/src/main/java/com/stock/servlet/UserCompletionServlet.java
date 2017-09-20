package com.stock.servlet;

import com.stock.model.ServiceRequest;
import com.stock.model.ServiceResponse;
import com.stock.model.model.StockUserModel;
import com.stock.model.request.StockUserCompletionRequest;
import com.stock.model.response.StockUserCompletionResponse;
import com.stock.service.UserService;
import com.stock.servlet.base.BaseServlet;

import javax.servlet.annotation.WebServlet;

/**
 * Created by xiangleiliu on 2017/9/7.
 * 用户资料补全修改
 */
@WebServlet(name = "UserCompletionServlet")
public class UserCompletionServlet extends BaseServlet {
    UserService userService;

    public UserCompletionServlet() {
        userService = UserService.getInstance();
    }

    @Override
    protected Class getActionRequestClass() {
        return StockUserCompletionRequest.class;
    }

    @Override
    protected Class getActionResponseClass() {
        return StockUserCompletionResponse.class;
    }

    @Override
    protected void servletAction(ServiceRequest serviceRequest, ServiceResponse serviceResponse) throws Exception {
        StockUserCompletionRequest completionRequest = (StockUserCompletionRequest) serviceRequest;
        StockUserCompletionResponse completionResponse = (StockUserCompletionResponse) serviceResponse;
        StockUserModel stockUserModel = userService.updateUser(completionRequest, completionResponse);
        completionResponse.userModel = stockUserModel;
    }
}
