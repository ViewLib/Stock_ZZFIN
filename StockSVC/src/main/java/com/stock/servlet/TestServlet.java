package com.stock.servlet;

import com.stock.controller.user.model.SearchRankPageCountRequest;
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
        SearchRankPageCountRequest request = new SearchRankPageCountRequest();
        List<StockSearchRankViewModel> searchRankList = userService.getSearchRankList(request);

        System.out.println(searchRankList.size());
    }
}
