package com.stock.servlet;

import com.stock.model.ServiceRequest;
import com.stock.model.ServiceResponse;
import com.stock.model.model.StockMinuteDataModel;
import com.stock.model.request.StockMinuteDataRequest;
import com.stock.model.response.StockMinuteDataResponse;
import com.stock.service.StockService;
import com.stock.servlet.base.BaseServlet;

import javax.servlet.annotation.WebServlet;
import java.util.List;

/**
 * Created by hp on 2017/10/18.StockMinuteDataServlet
 */
@WebServlet(name = "StockMinuteDataServlet")
public class StockMinuteDataServlet extends BaseServlet {
    StockService stockService;

    public StockMinuteDataServlet() {
        stockService = StockService.getInstance();
    }

    @Override
    protected Class getActionRequestClass() {
        return StockMinuteDataRequest.class;
    }

    @Override
    protected Class getActionResponseClass() {
        boolean flag = true;
        Class c = null;
        if(flag){
            c =  StockMinuteDataResponse.class;
        }
        return c;
    }

    @Override
    protected void servletAction(ServiceRequest serviceRequest, ServiceResponse serviceResponse) throws Exception {

        StockMinuteDataRequest  stockMinuteDataRequest = (StockMinuteDataRequest) serviceRequest;
        StockMinuteDataResponse   stockMinuteDataResponse= (StockMinuteDataResponse) serviceResponse;
        List<StockMinuteDataModel> stockMinuteDataModels = stockService.stockMinuteDataModels(stockMinuteDataRequest, stockMinuteDataResponse);
        stockMinuteDataResponse.stockCode=stockMinuteDataRequest.stockCode;
        stockMinuteDataResponse.stockName=stockMinuteDataRequest.stockName;
        stockMinuteDataResponse.stockMinuteDataModels = stockMinuteDataModels;

    }
}

