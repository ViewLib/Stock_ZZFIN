package com.stock.servlet;

import com.alibaba.fastjson.JSON;
import com.stock.dao.UserDao;
import com.stock.dao.UserDaoImpl;
import com.stock.model.model.StockUserModel;
import com.stock.model.response.UserListResponse;
import com.stock.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/11/3.
 */
@WebServlet(name = "UserGetInfoListServlet")
public class UserGetInfoListServlet extends HttpServlet {

    UserService userService;

    public UserGetInfoListServlet() {
        userService = UserService.getInstance();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageStr = request.getParameter("page");
        ArrayList<StockUserModel> userList = userService.selectUserInfo(pageStr);

//        UserListResponse userListResponse = new UserListResponse();
//        userListResponse.userList.addAll(userList);
//        userListResponse.userNum = userList.size();

        String uerListJson = JSON.toJSONString(userList);
        PrintWriter writer = response.getWriter();
        writer.write(uerListJson);
        writer.flush();
    }
}
