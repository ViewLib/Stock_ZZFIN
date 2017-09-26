package com.stock.servlet;

import com.stock.model.ServiceRequest;
import com.stock.model.ServiceResponse;
import com.stock.model.model.StockRankResultModel;
import com.stock.model.model.StockSearchModel;
import com.stock.model.request.StockHotSearchRequest;
import com.stock.model.request.StockRankDetailResquest;
import com.stock.model.response.StockHotSearchResponse;
import com.stock.model.response.StockRankDetailResponse;
import com.stock.service.StockService;
import com.stock.servlet.base.BaseServlet;

import javax.servlet.annotation.WebServlet;
import java.util.List;

@WebServlet(name = "StockRankListServlet")
public class StockRankDetailServlet extends BaseServlet {

    StockService stockService;

    public StockRankDetailServlet() {
        stockService = StockService.getInstance();
    }

    @Override
    protected Class getActionRequestClass() {
        return StockRankDetailResquest.class;
    }

    @Override
    protected Class getActionResponseClass() {
        return StockRankDetailResquest.class;
    }

    @Override
    protected void servletAction(ServiceRequest serviceRequest, ServiceResponse serviceResponse) throws Exception {

        StockRankDetailResquest  rankDetailResquest = (StockRankDetailResquest) serviceRequest;
        StockRankDetailResponse   rankDetailResponse= (StockRankDetailResponse) serviceResponse;
        List<StockRankResultModel>  resultModelList = stockService.getStockDetail(rankDetailResquest, rankDetailResponse);
        rankDetailResponse.mRankResultList = resultModelList;

    }
}
