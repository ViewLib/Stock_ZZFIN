package com.xt.lxl.stock.model.reponse;


import com.xt.lxl.stock.model.ServiceResponse;
import com.xt.lxl.stock.model.model.StockSyncModel;

import java.util.ArrayList;
import java.util.List;

public class StockSyncResponse extends ServiceResponse {
    public int serviceCode = 4001;//服务号
    public int version = 1;//数据库股票增量版本
    public List<StockSyncModel> syncModelList = new ArrayList<>();
}
