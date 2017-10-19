package com.stock.model.response;

import com.stock.model.ServiceResponse;
import com.stock.model.model.StockMinuteDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2017/10/18.
 */
public class StockMinuteDataResponse extends ServiceResponse {
    public int serviceCode = 5001;//服务号

    public String stockCode = "";//股票代码
    public String stockName = "";//股票名称
    public List<StockMinuteDataModel> stockMinuteDataModels = new ArrayList<>();//分时数据
}
