package com.stock.model.response;

import com.stock.model.ServiceResponse;
import com.stock.model.model.StockDetailDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2017/10/9.
 */
public class StockDetailDataResponse extends ServiceResponse {
    public int serviceCode = 2005;//服务号
    public String stockCode;//类型
    public String stockKData;//类型
    public List<StockDetailDataModel> stockDetailDataModels = new ArrayList<>();//界面筛选项列表
}
