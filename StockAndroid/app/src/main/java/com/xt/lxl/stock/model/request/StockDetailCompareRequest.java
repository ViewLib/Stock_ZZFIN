package com.xt.lxl.stock.model.request;


import com.xt.lxl.stock.model.ServiceRequest;

/**
 * Created by xiangleiliu on 2017/11/8.
 */
public class StockDetailCompareRequest extends ServiceRequest {
    public int serviceCode = 2005;//服务号
    public String stockCode;//股票代码
}
