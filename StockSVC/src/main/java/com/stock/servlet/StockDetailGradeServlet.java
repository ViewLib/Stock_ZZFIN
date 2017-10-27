package com.stock.servlet;

import com.stock.model.ServiceRequest;
import com.stock.model.ServiceResponse;
import com.stock.model.model.StockDetailCompanyModel;
import com.stock.model.model.StockDetailGradleModel;
import com.stock.model.model.StockDetailStockHolder;
import com.stock.model.request.StockDetailCompanyInfoRequest;
import com.stock.model.request.StockDetailGradeRequest;
import com.stock.model.response.StockDetailCompanyInfoResponse;
import com.stock.model.response.StockDetailGradeResponse;
import com.stock.service.StockService;
import com.stock.servlet.base.BaseServlet;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2017/10/22.
 */
@WebServlet(name = "StockDetailGradleServlet")
public class StockDetailGradeServlet extends BaseServlet {
    StockService stockService;

    public StockDetailGradeServlet() {
        stockService = StockService.getInstance();
    }

    @Override
    protected Class getActionRequestClass() {
        return StockDetailGradeRequest.class;
    }

    @Override
    protected Class getActionResponseClass() {
        boolean flag = true;
        Class c = null;
        if (flag) {
            c = StockDetailGradeResponse.class;
        }
        return c;
    }

    @Override
    protected void servletAction(ServiceRequest serviceRequest, ServiceResponse serviceResponse) throws Exception {
        StockDetailGradeRequest request = (StockDetailGradeRequest) serviceRequest;
        StockDetailGradeResponse response = (StockDetailGradeResponse) serviceResponse;
        List<StockDetailGradleModel> gradleModelList = stockService.getStockGrade(request, response);
        response.gradleModelList = gradleModelList;
    }
}