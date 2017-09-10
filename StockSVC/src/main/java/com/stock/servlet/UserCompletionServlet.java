package com.stock.servlet;

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
        String userid = request.getParameter("userid");
        String moblie = request.getParameter("moblie");
        String nickname = request.getParameter("nickname");
        String area = request.getParameter("area");
        String age = request.getParameter("age");

        Logger.getLogger().showMessage("doGet area:" + area);

        PrintWriter writer = response.getWriter();
        response.setStatus(500);//默认值
        try {
            boolean result = userService.updateUser(userid, moblie, nickname, area, age);
            if (result) {
                response.setStatus(200);
                writer.write("update user success");
            } else {
                writer.write("update user fail");
            }
        } catch (Exception e) {
            writer.write(e.getMessage());
        }
        writer.flush();
    }
}
