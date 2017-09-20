package com.stock.servlet;

import com.stock.model.ServiceRequest;
import com.stock.model.ServiceResponse;
import com.stock.model.model.StockSearchModel;
import com.stock.model.request.StockHotSearchRequest;
import com.stock.model.request.StockRankListResquest;
import com.stock.model.response.StockHotSearchResponse;
import com.stock.model.response.StockRankListResponse;
import com.stock.service.StockService;
import com.stock.servlet.base.BaseServlet;

import javax.servlet.annotation.WebServlet;
import java.util.List;

@WebServlet(name = "StockRankListServlet")
public class StockHotSearchServlet extends BaseServlet {

    StockService stockService;

    public StockHotSearchServlet() {
        stockService = StockService.getInstance();
    }

    @Override
    protected Class getActionRequestClass() {
        return StockHotSearchRequest.class;
    }

    @Override
    protected Class getActionResponseClass() {
        return StockHotSearchResponse.class;
    }

    @Override
    protected void servletAction(ServiceRequest serviceRequest, ServiceResponse serviceResponse) throws Exception {
        StockHotSearchRequest hotSearchRequest = (StockHotSearchRequest) serviceRequest;
        StockHotSearchResponse hotSearchResponse = (StockHotSearchResponse) serviceResponse;
        List<StockSearchModel> hotSearchList = stockService.handleHotSearchList(hotSearchRequest, hotSearchResponse);
        hotSearchResponse.hotSearchList = hotSearchList;
    }
}
