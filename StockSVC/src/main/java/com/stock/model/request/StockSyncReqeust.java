package com.stock.model.request;

import com.stock.model.ServiceRequest;

public class StockSyncReqeust extends ServiceRequest {
    public int version = 1;//用户本地股票增量版本
}
