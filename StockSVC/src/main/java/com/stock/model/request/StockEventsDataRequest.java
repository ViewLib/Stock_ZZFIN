package com.stock.model.request;

import com.stock.model.ServiceRequest;

/**
 * Created by hp on 2017/10/29.
 */
public class StockEventsDataRequest  extends ServiceRequest {
    public int serviceCode = 2010;//服务号
    public String stockCode = "";//股票代码
    public int type ;//区分重大消息，重大事件
}
