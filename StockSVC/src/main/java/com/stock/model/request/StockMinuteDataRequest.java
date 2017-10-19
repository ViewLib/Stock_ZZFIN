package com.stock.model.request;

import com.stock.model.ServiceRequest;

/**
 * Created by hp on 2017/10/18.
 */
public class StockMinuteDataRequest extends ServiceRequest {
    public int serviceCode = 5001;//服务号
    public String stockCode = "";//股票代码
    public String stockName = "";//股票名

}
