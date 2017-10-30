package com.stock.servlet;

import com.stock.model.ServiceRequest;
import com.stock.model.ServiceResponse;
import com.stock.model.model.StockDetailFinanceGroup;
import com.stock.model.model.StockEventsDataList;
import com.stock.model.request.StockDetailFinanceRequest;
import com.stock.model.request.StockEventsDataRequest;
import com.stock.model.response.StockDetailFinanceResponse;
import com.stock.model.response.StockEventsDataResponse;
import com.stock.service.StockService;
import com.stock.servlet.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by hp on 2017/10/29.
 */
@WebServlet(name = "StockEventsDataServlet")
public class StockEventsDataServlet extends BaseServlet {
    StockService stockService;

    public StockEventsDataServlet() {
        stockService = StockService.getInstance();
    }

    @Override
    protected Class getActionRequestClass() {
        return StockEventsDataRequest.class;
    }

    @Override
    protected Class getActionResponseClass() {
        return StockEventsDataResponse.class;
    }

    @Override
    protected void servletAction(ServiceRequest serviceRequest, ServiceResponse serviceResponse) throws Exception {
        StockEventsDataRequest stockEventsDataRequest = (StockEventsDataRequest) serviceRequest;
        StockEventsDataResponse stockEventsDataResponse = (StockEventsDataResponse) serviceResponse;
        List<StockEventsDataList> stockEventsDataLists = stockService.getStockEventsList(stockEventsDataRequest, stockEventsDataResponse);
        stockEventsDataResponse.stockEventsDataLists = stockEventsDataLists;
    }
}
