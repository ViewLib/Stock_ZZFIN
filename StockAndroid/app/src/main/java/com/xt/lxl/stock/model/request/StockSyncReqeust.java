package com.xt.lxl.stock.model.request;


import com.xt.lxl.stock.model.ServiceRequest;

public class StockSyncReqeust extends ServiceRequest {
    public int serviceCode = 4001;//服务号

    public int version = 1;//用户本地股票增量版本
}
