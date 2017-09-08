package com.stock.servlet;

import com.stock.model.StockUserModel;
import com.stock.service.UserService;
import com.stock.util.StringUtil;

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
public class UserRegisterServlet extends HttpServlet {
    UserService userService;

    public UserRegisterServlet() {
        userService = UserService.getInstance();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //处理注册
        String moblie = request.getParameter("moblie");
        String clientId = request.getParameter("clientId");
        PrintWriter writer = response.getWriter();
        try {
            //对moblie做个处理
            if (!moblie.contains("_")) {
                moblie = "86_" + moblie;
            }
            StockUserModel userModel = userService.registerUser(moblie, clientId);
            String userJson = userModel.toString();
            writer.write(userJson);
            response.setStatus(200);
        } catch (Exception e) {
            response.setStatus(501);
            writer.write(e.getMessage());
        }
        writer.flush();
    }
}
