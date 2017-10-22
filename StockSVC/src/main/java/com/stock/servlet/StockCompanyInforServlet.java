package com.stock.servlet;

import com.stock.model.ServiceRequest;
import com.stock.model.ServiceResponse;
import com.stock.model.model.StockDateDataModel;
import com.stock.model.model.StockDetailCompanyModel;
import com.stock.model.request.StockDetailCompanyInfoRequest;
import com.stock.model.request.StockDetailDataRequest;
import com.stock.model.response.StockDetailCompanyInfoResponse;
import com.stock.model.response.StockDetailDataResponse;
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
 * Created by hp on 2017/10/22.
 */
@WebServlet(name = "StockCompanyInforServlet")
public class StockCompanyInforServlet extends BaseServlet {
    StockService stockService;

    public StockCompanyInforServlet() {
        stockService = StockService.getInstance();
    }

    @Override
    protected Class getActionRequestClass() {
        return StockDetailCompanyInfoRequest.class;
    }

    @Override
    protected Class getActionResponseClass() {
        boolean flag = true;
        Class c = null;
        if(flag){
            c =  StockDetailCompanyInfoResponse.class;
        }
        return c;
    }

    @Override
    protected void servletAction(ServiceRequest serviceRequest, ServiceResponse serviceResponse) throws Exception {
        StockDetailCompanyInfoRequest  stockDetailCompanyInfoRequest = (StockDetailCompanyInfoRequest) serviceRequest;
        StockDetailCompanyInfoResponse   stockDetailCompanyInfoResponse= (StockDetailCompanyInfoResponse) serviceResponse;
        List<StockDetailCompanyModel> stockDetailCompanyModels = stockService.stockDetailDataModels(stockDetailDataRequest, stockDetailDataResponse);
        stockDetailCompanyInfoResponse.companyModel = stockDetailCompanyModels;

    }
}