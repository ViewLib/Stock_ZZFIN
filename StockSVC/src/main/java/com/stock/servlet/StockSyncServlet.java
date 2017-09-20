package com.stock.servlet;

import com.stock.dao.StockDao;
import com.stock.dao.StockDaoImpl;
import com.stock.model.ServiceRequest;
import com.stock.model.ServiceResponse;
import com.stock.model.model.StockSyncModel;
import com.stock.model.request.StockSyncReqeust;
import com.stock.model.response.StockSyncResponse;
import com.stock.service.StockService;
import com.stock.servlet.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "StockSyncServlet")
public class StockSyncServlet extends BaseServlet {
    StockService service;

    public StockSyncServlet() {
        service = StockService.getInstance();
    }

    @Override
    protected Class getActionRequestClass() {
        return StockSyncReqeust.class;
    }

    @Override
    protected Class getActionResponseClass() {
        return StockSyncResponse.class;
    }

    @Override
    protected void servletAction(ServiceRequest serviceRequest, ServiceResponse serviceResponse) throws Exception {
        StockSyncReqeust syncReqeust = (StockSyncReqeust) serviceRequest;
        StockSyncResponse syncResponse = (StockSyncResponse) serviceResponse;
        List<StockSyncModel> stockSyncList = service.getStockSyncList(syncReqeust, syncResponse);
        int maxVersion = 2;
        for (StockSyncModel model : stockSyncList) {
            if (model.version > maxVersion) {
                maxVersion = model.version;
            }
        }
        syncResponse.syncModelList.addAll(stockSyncList);
        syncResponse.version = maxVersion;
    }

    public boolean isVerification(ServiceRequest baseRequest) {
        StockSyncReqeust syncReqeust = (StockSyncReqeust) baseRequest;
//        if (syncReqeust.version == 1) {
//            syncReqeust.version = 2;
//        }
        return true;
    }

}
