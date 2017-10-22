package com.stock.model.request;


import com.stock.model.ServiceRequest;
import com.stock.model.response.StockGetDateDataResponse;

/**
 * Created by xiangleiliu on 2017/10/16.
 * 分时数据
 */
public class StockGetDateDataRequest extends ServiceRequest {

    public int serviceCode = 5001;//服务号

    public String stockCode = "";//股票代码

    public String stockKData = StockGetDateDataResponse.TYPE_DAY;//类型

}
