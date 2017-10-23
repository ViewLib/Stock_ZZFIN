package com.stock.servlet;

import com.stock.model.ServiceRequest;
import com.stock.model.ServiceResponse;
import com.stock.model.model.StockRankFilterModel;
import com.stock.model.model.StockSearchModel;
import com.stock.model.request.StockHotSearchRequest;
import com.stock.model.request.StockRankDetailFilterlRequest;
import com.stock.model.request.StockRankDetailResquest;
import com.stock.model.request.StockRankListRequest;
import com.stock.model.response.StockHotSearchResponse;
import com.stock.model.response.StockRankDetailFilterlResponse;
import com.stock.model.response.StockRankDetailResponse;
import com.stock.model.response.StockRankListResponse;
import com.stock.service.StockService;
import com.stock.servlet.base.BaseServlet;

import javax.servlet.annotation.WebServlet;
import java.util.List;

@WebServlet(name = "StockRankListServlet")
public class StockRankFilterServlet extends BaseServlet {

    StockService stockService;

    public StockRankFilterServlet() {
        stockService = StockService.getInstance();
    }

    @Override
    protected Class getActionRequestClass() {
        return StockRankDetailFilterlRequest.class;
    }

    @Override
    protected Class getActionResponseClass() {
        return StockRankDetailFilterlResponse.class;
    }

    @Override
    protected void servletAction(ServiceRequest serviceRequest, ServiceResponse serviceResponse) throws Exception {
   //StockRankFilterModel
        StockRankDetailFilterlRequest stockRankDetailFilterlRequest= (StockRankDetailFilterlRequest) serviceRequest;
        StockRankDetailFilterlResponse stockRankDetailFilterlResponse=(StockRankDetailFilterlResponse) serviceResponse;
        List<StockRankFilterModel> stockRankFilterModels = stockService.getStockFilterList(stockRankDetailFilterlRequest, stockRankDetailFilterlResponse);
        stockRankDetailFilterlResponse.rankFilterList = stockRankFilterModels;
    }
}
