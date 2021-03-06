package com.stock.model.response;

import com.stock.model.ServiceResponse;
import com.stock.model.model.StockEventsDataList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/10/29.
 */
public class StockEventsDataResponse extends ServiceResponse{
    public int serviceCode = 2010; //服务号
    public String stockCode = "";//股票代码
    public String moduleName = "重大事件";
    public int type  ;//模块类型
    public List<StockEventsDataList> stockEventsDataLists = new ArrayList<>();
}
