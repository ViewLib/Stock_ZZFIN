package com.xt.lxl.stock.model.reponse;

import com.xt.lxl.stock.model.ServiceResponse;
import com.xt.lxl.stock.model.model.StockMinuteDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/10/16.
 * 分时数据
 */
public class StockGetMinuteDataResponse extends ServiceResponse {

    public int serviceCode = 5001;//服务号

    public String stockCode = "";//股票代码
    public String stockName = "";//股票名称
    public List<StockMinuteDataModel> minuteDataList = new ArrayList<>();//分时数据

}
