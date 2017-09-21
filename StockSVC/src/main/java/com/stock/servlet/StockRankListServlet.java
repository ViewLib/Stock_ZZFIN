package com.stock.servlet;

import com.stock.model.ServiceRequest;
import com.stock.model.ServiceResponse;
import com.stock.model.model.StockSearchModel;
import com.stock.model.request.StockRankListRequest;
import com.stock.model.response.StockRankListResponse;
import com.stock.service.StockService;
import com.stock.servlet.base.BaseServlet;

import javax.servlet.annotation.WebServlet;
import java.util.List;

@WebServlet(name = "StockRankListServlet")
public class StockRankListServlet extends BaseServlet {

    StockService stockService;

    public StockRankListServlet() {
        stockService = StockService.getInstance();
    }

    @Override
    protected Class getActionRequestClass() {
        return StockRankListRequest.class;
    }

    @Override
    protected Class getActionResponseClass() {
        return StockRankListResponse.class;
    }

    @Override
    protected void servletAction(ServiceRequest serviceRequest, ServiceResponse serviceResponse) throws Exception {
        StockRankListRequest stockRankListResquest = (StockRankListRequest) serviceRequest;
        StockRankListResponse stockRankListResponse = (StockRankListResponse) serviceResponse;
        List<StockSearchModel> rankSearchList = stockService.handleRankList(stockRankListResquest, stockRankListResponse);
        stockRankListResponse.rankSearchList = rankSearchList;
    }
}
