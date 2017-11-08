package com.stock.servlet;

import com.stock.model.ServiceRequest;
import com.stock.model.ServiceResponse;
import com.stock.model.model.StockDetailCompanyModel;
import com.stock.model.model.StockDetailCompareModel;
import com.stock.model.model.StockDetailStockHolder;
import com.stock.model.model.StockViewModel;
import com.stock.model.request.StockDetailCompareRequest;
import com.stock.model.request.StockDetailCompanyInfoRequest;
import com.stock.model.response.StockDetailCompanyInfoResponse;
import com.stock.model.response.StockDetailCompareResponse;
import com.stock.service.StockService;
import com.stock.servlet.base.BaseServlet;

import javax.servlet.annotation.WebServlet;
import java.util.List;

/**
 * Created by hp on 2017/10/22.
 */
@WebServlet(name = "StockDetailCompareServlet")
public class StockDetailCompareServlet extends BaseServlet {
    StockService stockService;

    public StockDetailCompareServlet() {
        stockService = StockService.getInstance();
    }

    @Override
    protected Class getActionRequestClass() {
        return StockDetailCompareRequest.class;
    }

    @Override
    protected Class getActionResponseClass() {
        boolean flag = true;
        Class c = null;
        if (flag) {
            c = StockDetailCompareResponse.class;
        }
        return c;
    }

    @Override
    protected void servletAction(ServiceRequest serviceRequest, ServiceResponse serviceResponse) throws Exception {
        StockDetailCompareRequest request = (StockDetailCompareRequest) serviceRequest;
        StockDetailCompareResponse response = (StockDetailCompareResponse) serviceResponse;
        List<StockDetailCompareModel> compareListInfo = stockService.getCompareListInfo(request);
        response.compareList.clear();
        response.compareList.addAll(compareListInfo);
    }
}