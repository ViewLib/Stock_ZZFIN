package com.stock.servlet;

import com.stock.model.ServiceRequest;
import com.stock.model.ServiceResponse;
import com.stock.model.model.StockRankResultModel;
import com.stock.model.model.StockSearchModel;
import com.stock.model.request.StockHotSearchRequest;
import com.stock.model.request.StockRankListResquest;
import com.stock.model.response.StockHotSearchResponse;
import com.stock.model.response.StockRankListResponse;
import com.stock.service.StockService;
import com.stock.servlet.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "StockRankListServlet")
public class StockRankListServlet extends BaseServlet {

    StockService stockService;

    public StockRankListServlet() {
        stockService = StockService.getInstance();
    }

    @Override
    protected Class getActionRequestClass() {
        return StockRankListResquest.class;
    }

    @Override
    protected Class getActionResponseClass() {
        return StockRankListResponse.class;
    }

    @Override
    protected void servletAction(ServiceRequest serviceRequest, ServiceResponse serviceResponse) throws Exception {
        StockRankListResquest stockRankListResquest = (StockRankListResquest) serviceRequest;
        StockRankListResponse stockRankListResponse = (StockRankListResponse) serviceResponse;
        List<StockSearchModel> rankSearchList = stockService.handleRankList(stockRankListResquest, stockRankListResponse);
        stockRankListResponse.rankSearchList = rankSearchList;
    }
}
