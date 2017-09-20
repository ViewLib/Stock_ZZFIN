package com.stock.servlet;

import com.alibaba.fastjson.JSON;
import com.stock.model.ServiceRequest;
import com.stock.model.ServiceResponse;
import com.stock.model.model.StockUserModel;
import com.stock.model.request.StockUserRegisterRequest;
import com.stock.model.response.StockUserRegisterResponse;
import com.stock.service.UserService;
import com.stock.servlet.base.BaseServlet;
import org.omg.CORBA.ServerRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by xiangleiliu on 2017/9/6.
 * 用户注册
 */
@WebServlet(name = "UserRegisterServlet")
public class UserRegisterServlet extends BaseServlet {
    UserService userService;

    public UserRegisterServlet() {
        super();
        userService = UserService.getInstance();
    }

    @Override
    protected Class getActionRequestClass() {
        return StockUserRegisterRequest.class;
    }

    @Override
    protected Class getActionResponseClass() {
        return StockUserRegisterResponse.class;
    }

    @Override
    protected void servletAction(ServiceRequest registerRequest, ServiceResponse registerResponse) throws Exception {
        StockUserRegisterRequest registerRequest1 = (StockUserRegisterRequest) registerRequest;
        StockUserRegisterResponse registerResponse1 = (StockUserRegisterResponse) registerResponse;
        StockUserModel stockUserModel = userService.registerUser(registerRequest1, registerResponse1);
        registerResponse1.userModel = stockUserModel;

    }


}
