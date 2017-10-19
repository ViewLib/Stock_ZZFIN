package com.stock.servlet;

import com.stock.model.ServiceRequest;
import com.stock.model.ServiceResponse;
import com.stock.model.model.StockDetailDataModel;
import com.stock.model.model.StockFirstTypeModel;
import com.stock.model.request.StockDetailDataRequest;
import com.stock.model.request.StockFirstTypeRequest;
import com.stock.model.request.StockRankDetailFilterlRequest;
import com.stock.model.response.StockDetailDataResponse;
import com.stock.model.response.StockFirstTypeResponse;
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
 * Created by hp on 2017/10/9.
 */
@WebServlet(name = "StockDetailDataServlet")
public class StockDetailDataServlet extends BaseServlet {
    StockService stockService;

    public StockDetailDataServlet() {
        stockService = StockService.getInstance();
    }

    @Override
    protected Class getActionRequestClass() {
        return StockDetailDataRequest.class;
    }

    @Override
    protected Class getActionResponseClass() {
        boolean flag = true;
        Class c = null;
        if(flag){
            c =  StockDetailDataResponse.class;
        }
        return c;
    }

    @Override
    protected void servletAction(ServiceRequest serviceRequest, ServiceResponse serviceResponse) throws Exception {
        StockDetailDataRequest  stockDetailDataRequest = (StockDetailDataRequest) serviceRequest;
        StockDetailDataResponse   stockDetailDataResponse= (StockDetailDataResponse) serviceResponse;
        List<StockDetailDataModel> stockDetailDataModels = stockService.stockDetailDataModels(stockDetailDataRequest, stockDetailDataResponse);
        stockDetailDataResponse.stockDetailDataModels = stockDetailDataModels;

    }
}
