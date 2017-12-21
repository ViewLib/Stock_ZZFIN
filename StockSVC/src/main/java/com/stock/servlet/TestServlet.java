package com.stock.servlet;

import com.stock.controller.user.model.SearchRankPageCountRequest;
import com.stock.controller.user.model.SearchRankUpdateRequest;
import com.stock.model.viewmodel.StockSearchRankViewModel;
import com.stock.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class TestServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        UserService userService = UserService.getInstance();
        SearchRankUpdateRequest request = new SearchRankUpdateRequest();
        request.search_id = 0;
        request.show_type = 1;
        request.search_type = 2;
        request.search_title = "测试";
        request.search_desc = "测试描述";
        request.search_relation = 200;
        request.search_weight = 79;
        request.rank_sql = "select * from test";
        userService.updateStockSearchRank(request);
        resp.setStatus(200);
        resp.getWriter().write("success");
        resp.getWriter().flush();
    }
}
