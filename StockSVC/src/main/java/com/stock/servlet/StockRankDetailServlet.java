package com.stock.servlet;

import com.stock.model.ServiceRequest;
import com.stock.model.ServiceResponse;
import com.stock.model.request.StockHotSearchRequest;
import com.stock.model.response.StockHotSearchResponse;
import com.stock.service.StockService;
import com.stock.servlet.base.BaseServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "StockRankListServlet")
public class StockRankDetailServlet extends BaseServlet {

    StockService stockService;

    public StockRankDetailServlet() {
        stockService = StockService.getInstance();
    }

    @Override
    protected Class getActionRequestClass() {
        return StockHotSearchRequest.class;
    }

    @Override
    protected Class getActionResponseClass() {
        return StockHotSearchResponse.class;
    }

    @Override
    protected void servletAction(ServiceRequest registerRequest, ServiceResponse registerResponse) throws Exception {

    }
}
