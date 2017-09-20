package com.stock.servlet;

import com.alibaba.fastjson.JSON;
import com.stock.model.request.StockUserCompletionRequest;
import com.stock.model.request.StockUserRegisterRequest;
import com.stock.model.response.StockUserCompletionResponse;
import com.stock.service.UserService;
import com.stock.util.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by xiangleiliu on 2017/9/7.
 * 用户资料补全修改
 */
@WebServlet(name = "UserCompletionServlet")
public class UserCompletionServlet extends HttpServlet {
    UserService userService;

    public UserCompletionServlet() {
        userService = UserService.getInstance();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String data = request.getParameter("data");
        StockUserCompletionRequest stockUserCompletionRequest = JSON.parseObject(data, StockUserCompletionRequest.class);
        Logger.getLogger().showMessage("doGet area:" + stockUserCompletionRequest.area);

        PrintWriter writer = response.getWriter();
        response.setStatus(500);//默认值
        StockUserCompletionResponse stockUserCompletionResponse = new StockUserCompletionResponse();
        try {
            userService.updateUser(stockUserCompletionRequest, stockUserCompletionResponse);
            response.setStatus(200);
            stockUserCompletionResponse.resultMessage = "sucess";
        } catch (Exception e) {
            response.setStatus(500);
        }
        writer.write(JSON.toJSONString(stockUserCompletionResponse));
        writer.flush();
    }
}
