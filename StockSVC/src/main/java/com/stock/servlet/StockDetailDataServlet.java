package com.stock.servlet;

import com.stock.model.ServiceRequest;
import com.stock.model.ServiceResponse;
import com.stock.model.model.StockDateDataModel;
import com.stock.model.request.StockGetDateDataRequest;
import com.stock.model.response.StockGetDateDataResponse;
import com.stock.service.StockService;
import com.stock.servlet.base.BaseServlet;

import javax.servlet.annotation.WebServlet;
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
        return StockGetDateDataRequest.class;
    }

    @Override
    protected Class getActionResponseClass() {
        boolean flag = true;
        Class c = null;
        if(flag){
            c =  StockGetDateDataResponse.class;
        }
        return c;
    }

    @Override
    protected void servletAction(ServiceRequest serviceRequest, ServiceResponse serviceResponse) throws Exception {
        StockGetDateDataRequest  stockDetailDataRequest = (StockGetDateDataRequest) serviceRequest;
        StockGetDateDataResponse stockDetailDataResponse= (StockGetDateDataResponse) serviceResponse;
        List<StockDateDataModel> stockDetailDataModels = stockService.stockDetailDataModels(stockDetailDataRequest, stockDetailDataResponse);
        stockDetailDataResponse.dateDataList = stockDetailDataModels;

    }
}
