package com.stock.service;

import com.stock.dao.StockDao;
import com.stock.dao.StockDaoImpl;
import com.stock.model.model.StockRankResultModel;
import com.stock.model.model.StockSearchModel;
import com.stock.model.model.StockSyncModel;
import com.stock.model.request.StockHotSearchRequest;
import com.stock.model.request.StockRankListResquest;
import com.stock.model.request.StockSyncReqeust;
import com.stock.model.response.StockHotSearchResponse;
import com.stock.model.response.StockRankListResponse;
import com.stock.model.response.StockSyncResponse;

import java.util.ArrayList;
import java.util.List;

public class StockService {

    StockDao dao;

    public static StockService stockService;

    private StockService() {
        dao = StockDaoImpl.getDao();
    }

    public static synchronized StockService getInstance() {
        if (stockService == null) {
            stockService = new StockService();
        }
        return stockService;
    }


    public List<StockSearchModel> handleRankList(StockRankListResquest stockRankListResquest, StockRankListResponse stockRankListResponse) {
        List<StockSearchModel> stockSearchModelList = new ArrayList<>();
        List<StockSearchModel> searchModels = dao.selectSerchModelRankList(StockSearchModel.STOCK_SHOW_TYPE_TOP10, 10);
        stockSearchModelList.addAll(searchModels);
        return stockSearchModelList;
    }

    public List<StockSearchModel> handleHotSearchList(StockHotSearchRequest hotSearchRequest, StockHotSearchResponse hotSearchResponse) {
        List<StockSearchModel> stockSearchModelList = new ArrayList<>();
        List<StockSearchModel> searchModels = dao.selectSerchModelRankList(StockSearchModel.STOCK_SHOW_TYPE_HOTSEARCH, 3);
        stockSearchModelList.addAll(searchModels);
        return stockSearchModelList;
    }

    public List<StockSyncModel> getStockSyncList(StockSyncReqeust syncReqeust, StockSyncResponse syncResponse) {
        int version = syncReqeust.version;
        List<StockSyncModel> syncModelList = dao.selectSyncModelList(version);
        return syncModelList;
    }
}
