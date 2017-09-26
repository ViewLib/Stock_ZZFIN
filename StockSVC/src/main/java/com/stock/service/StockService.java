package com.stock.service;

import com.stock.dao.StockDao;
import com.stock.dao.StockDaoImpl;
import com.stock.model.model.StockRankResultModel;
import com.stock.model.model.StockSearchModel;
import com.stock.model.model.StockSyncModel;
import com.stock.model.request.StockHotSearchRequest;
import com.stock.model.request.StockRankDetailResquest;
import com.stock.model.request.StockRankListRequest;
import com.stock.model.request.StockSyncReqeust;
import com.stock.model.response.StockHotSearchResponse;
import com.stock.model.response.StockRankDetailResponse;
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


    public List<StockSearchModel> handleRankList(StockRankListRequest stockRankListResquest, StockRankListResponse stockRankListResponse) {
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

    public List<StockRankResultModel> getStockDetail(StockRankDetailResquest rankDetailResquest, StockRankDetailResponse rankDetailResponse) {
        List<StockRankResultModel> rankResultModelList = new ArrayList<>();
        List<StockRankResultModel> rankResultModels = dao.selectRankDetailModelList(rankDetailResquest.serch_relation);
        if (rankResultModels.size() > 11) {
            rankResultModelList.addAll(rankResultModels.subList(0, 11));
        } else {
            rankResultModelList.addAll(rankResultModels);
        }
        return rankResultModelList;
    }

    public List<StockSyncModel> getStockSyncList(StockSyncReqeust syncReqeust, StockSyncResponse syncResponse) {
        int version = syncReqeust.version;
        List<StockSyncModel> syncModelList = dao.selectSyncModelList(version);
        return syncModelList;
    }
}
