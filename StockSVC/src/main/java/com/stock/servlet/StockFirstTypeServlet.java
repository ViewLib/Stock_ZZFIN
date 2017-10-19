package com.stock.servlet;

import com.stock.model.ServiceRequest;
import com.stock.model.ServiceResponse;
import com.stock.model.model.StockFirstTypeModel;
import com.stock.model.model.StockRankResultModel;
import com.stock.model.request.StockFirstTypeRequest;
import com.stock.model.request.StockRankDetailResquest;
import com.stock.model.response.StockFirstTypeResponse;
import com.stock.model.response.StockRankDetailResponse;
import com.stock.service.StockService;
import com.stock.servlet.base.BaseServlet;

import javax.servlet.annotation.WebServlet;
import java.util.List;

/**
 * Created by hp on 2017/9/28.
 */
@WebServlet(name = "StockFirstTypeServlet")
public class StockFirstTypeServlet extends BaseServlet {

    StockService stockService;

    public StockFirstTypeServlet() {
        stockService = StockService.getInstance();
    }

    @Override
    protected Class getActionRequestClass() {
        return StockFirstTypeRequest.class;
    }

    @Override
    protected Class getActionResponseClass() {
        boolean flag = true;
        Class c = null;
        if(flag){
            c =  StockFirstTypeResponse.class;
        }
        return c;
    }

    @Override
    protected void servletAction(ServiceRequest serviceRequest, ServiceResponse serviceResponse) throws Exception {

        StockFirstTypeRequest  stockFirstTypeRequest = (StockFirstTypeRequest) serviceRequest;
        StockFirstTypeResponse   stockFirstTypeResponse= (StockFirstTypeResponse) serviceResponse;
        List<StockFirstTypeModel> stockFirstTypeModels = stockService.stockFirstTypeModels(stockFirstTypeRequest, stockFirstTypeResponse);
        stockFirstTypeResponse.stockFirstTypeModels = stockFirstTypeModels;

    }
}
