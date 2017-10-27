package com.stock.model.response;

import com.stock.model.ServiceResponse;
import com.stock.model.model.StockDetailFinanceGroup;

import java.util.ArrayList;
import java.util.List;

public class StockDetailFinanceResponse extends ServiceResponse {
    public int serviceCode = 2007;//服务号
    public String stockCode = "";//股票代码
    public String moduleName = "财务信息";//模块名称
    public List<StockDetailFinanceGroup> groupList = new ArrayList<>();
}
