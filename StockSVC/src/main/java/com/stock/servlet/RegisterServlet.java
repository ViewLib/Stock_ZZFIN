package com.stock.servlet;

import com.stock.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by xiangleiliu on 2017/9/6.
 */
@WebServlet(name = "RegisterServlet")
public class RegisterServlet extends HttpServlet {
    UserService service = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //处理注册
        String moblie = request.getParameter("moblie");
        String clientId = request.getParameter("clientId");
        boolean result = service.registerUser(moblie, clientId);

        PrintWriter writer = response.getWriter();
        writer.write(result ? "插入成功" : "插入失败");
        writer.flush();
    }
}
