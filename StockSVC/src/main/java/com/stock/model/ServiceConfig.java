package com.stock.model;

import com.stock.model.request.StockHotSearchRequest;
import com.stock.model.request.StockRankDetailFilterlRequest;
import com.stock.model.request.StockRankListRequest;
import com.stock.model.request.StockUserRegisterRequest;
import com.stock.model.response.StockHotSearchResponse;
import com.stock.model.response.StockRankDetailFilterlResponse;
import com.stock.model.response.StockRankDetailResponse;
import com.stock.model.response.StockRankListResponse;

import java.util.HashMap;
import java.util.Map;

public class ServiceConfig {

    public Map<Integer, Class> map = new HashMap<>();

    ServiceConfig() {
        /**
         * 搜索
         * 热门搜索
         */
        map.put(1001, StockHotSearchRequest.class);//热门搜索服务
        map.put(1001, StockHotSearchResponse.class);//热门搜索服务

        /**
         * 发现界面
         */
        map.put(2001, StockRankListRequest.class);//top10排行界面
        map.put(2001, StockRankListResponse.class);//top10排行界面
        map.put(2002, StockRankDetailFilterlResponse.class);//股票筛选项下发
        map.put(2002, StockRankDetailFilterlRequest.class);//top10排行界面
        map.put(2003, StockRankDetailResponse.class);//股票排行详情界面
        map.put(2003, StockRankListResponse.class);//top10排行界面

        /**
         * 用户界面
         */
        map.put(3001, StockUserRegisterRequest.class);//用户注册
        map.put(3002, StockRankListResponse.class);//用户资料补全
    }


}

