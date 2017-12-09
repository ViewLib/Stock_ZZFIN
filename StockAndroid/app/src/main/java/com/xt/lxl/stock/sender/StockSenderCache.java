package com.xt.lxl.stock.sender;

import android.util.LruCache;

import com.xt.lxl.stock.model.ServiceResponse;
import com.xt.lxl.stock.model.reponse.StockDetailCompanyInfoResponse;
import com.xt.lxl.stock.model.reponse.StockDetailCompareResponse;
import com.xt.lxl.stock.model.reponse.StockDetailFinanceResponse;
import com.xt.lxl.stock.model.reponse.StockDetailGradeResponse;
import com.xt.lxl.stock.model.reponse.StockEventsDataResponse;
import com.xt.lxl.stock.model.reponse.StockGetDateDataResponse;
import com.xt.lxl.stock.page.module.StockDetailImportEventModule;


/**
 * Created by xiangleiliu on 2017/12/9.
 */

public class StockSenderCache {
    private static StockSenderCache cache;

    public LruCache<String, StockResponseModel> responseModelMap = new LruCache<>(5);

    public static synchronized StockSenderCache getCache() {
        if (cache == null) {
            cache = new StockSenderCache();
        }
        return cache;
    }

    private StockSenderCache() {

    }

    public <T extends ServiceResponse> void putStockResponseModel(String stockCode, T response) {
        putStockResponseModel(stockCode, 0, response);
    }

    public <T extends ServiceResponse> void putStockResponseModel(String stockCode, int type, T response) {
        StockResponseModel stockResponseModel = responseModelMap.get(stockCode);
        if (stockResponseModel == null) {
            stockResponseModel = new StockResponseModel();
            responseModelMap.put(stockCode, stockResponseModel);
        }
        if (response instanceof StockEventsDataResponse) {
            if (type == 2) {
                stockResponseModel.stockEventsDataResponse2 = (StockEventsDataResponse) response;
            } else if (type == 3) {
                stockResponseModel.stockEventsDataResponse3 = (StockEventsDataResponse) response;
            }
        } else if (response instanceof StockDetailCompareResponse) {
            stockResponseModel.stockDetailCompareResponse = (StockDetailCompareResponse) response;
        } else if (response instanceof StockDetailCompanyInfoResponse) {
            stockResponseModel.stockDetailCompanyInfoResponse = (StockDetailCompanyInfoResponse) response;
        } else if (response instanceof StockDetailGradeResponse) {
            stockResponseModel.stockDetailGradeResponse = (StockDetailGradeResponse) response;
        } else if (response instanceof StockDetailFinanceResponse) {
            stockResponseModel.stockDetailFinanceResponse = (StockDetailFinanceResponse) response;
        }
    }

    public <T extends ServiceResponse> T getStockResponseModel(String stockCode, Class<T> c) {
        return getStockResponseModel(stockCode, 0, c);
    }

    public <T extends ServiceResponse> T getStockResponseModel(String stockCode, int type, Class<T> c) {
        StockResponseModel stockResponseModel = responseModelMap.get(stockCode);
        if (stockResponseModel == null) {
            return null;
        }
        ServiceResponse response = null;
        if (c.getName().equals(StockEventsDataResponse.class.getName())) {
            if (type == 2) {
                response = stockResponseModel.stockEventsDataResponse2;
            } else if (type == 3) {
                response = stockResponseModel.stockEventsDataResponse3;
            }
        } else if (c.getName().equals(StockDetailCompareResponse.class.getName())) {
            response = stockResponseModel.stockDetailCompareResponse;
        } else if (c.getName().equals(StockDetailCompanyInfoResponse.class.getName())) {
            response = stockResponseModel.stockDetailCompanyInfoResponse;
        } else if (c.getName().equals(StockDetailGradeResponse.class.getName())) {
            response = stockResponseModel.stockDetailGradeResponse;
        } else if (c.getName().equals(StockDetailFinanceResponse.class.getName())) {
            response = stockResponseModel.stockDetailFinanceResponse;
        }
        return (T) response;
    }

}

class StockResponseModel {
    public StockEventsDataResponse stockEventsDataResponse2;//重大事件
    public StockEventsDataResponse stockEventsDataResponse3;//重大消息
    public StockDetailCompareResponse stockDetailCompareResponse;
    public StockDetailCompanyInfoResponse stockDetailCompanyInfoResponse;
    public StockDetailGradeResponse stockDetailGradeResponse;
    public StockDetailFinanceResponse stockDetailFinanceResponse;
}
