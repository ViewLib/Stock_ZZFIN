package com.stock.servlet;

import com.stock.model.ServiceRequest;
import com.stock.model.ServiceResponse;
import com.stock.model.model.StockDetailFinanceGroup;
import com.stock.model.request.StockDetailFinanceRequest;
import com.stock.model.request.StockHotSearchRequest;
import com.stock.model.response.StockDetailFinanceResponse;
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
 * Created by hp on 2017/10/27.
 */
@WebServlet(name = "StockFinanceServlet")
public class StockFinanceServlet extends BaseServlet {
    StockService stockService;

    public StockFinanceServlet() {
        stockService = StockService.getInstance();
    }

    @Override
    protected Class getActionRequestClass() {
        return StockDetailFinanceRequest.class;
    }

    @Override
    protected Class getActionResponseClass() {
        return StockDetailFinanceResponse.class;
    }

    @Override
    protected void servletAction(ServiceRequest serviceRequest, ServiceResponse serviceResponse) throws Exception {
        StockDetailFinanceRequest stockDetailFinanceRequest = (StockDetailFinanceRequest) serviceRequest;
        StockDetailFinanceResponse stockDetailFinanceResponse = (StockDetailFinanceResponse) serviceResponse;
        List<StockDetailFinanceGroup> stockDetailFinanceGroups = stockService.getFinicilaGroup(stockDetailFinanceRequest, stockDetailFinanceResponse);
        stockDetailFinanceResponse.groupList = stockDetailFinanceGroups;
    }
}
