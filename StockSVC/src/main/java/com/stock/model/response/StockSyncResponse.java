package com.stock.model.response;

import com.stock.model.ServiceRequest;
import com.stock.model.ServiceResponse;
import com.stock.model.model.StockSyncModel;

import java.util.ArrayList;
import java.util.List;

public class StockSyncResponse extends ServiceResponse {
    public int version = 1;//数据库股票增量版本
    public List<StockSyncModel> syncModelList = new ArrayList<>();
}
