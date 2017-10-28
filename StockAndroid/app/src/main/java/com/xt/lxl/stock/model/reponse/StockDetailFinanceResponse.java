package com.xt.lxl.stock.model.reponse;


import com.xt.lxl.stock.model.ServiceResponse;
import com.xt.lxl.stock.model.model.StockDetailFinanceGroup;

import java.util.ArrayList;
import java.util.List;

public class StockDetailFinanceResponse extends ServiceResponse {
    public int serviceCode = 2007;//服务号
    public String stockCode = "";//股票代码
    public String financeName = "";//模块名称 财务信息
    public List<StockDetailFinanceGroup> groupList = new ArrayList<>();
}
