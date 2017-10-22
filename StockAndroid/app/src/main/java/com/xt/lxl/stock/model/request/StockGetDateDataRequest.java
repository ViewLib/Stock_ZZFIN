package com.xt.lxl.stock.model.request;

import com.xt.lxl.stock.model.ServiceRequest;

/**
 * Created by xiangleiliu on 2017/10/16.
 * 分时数据
 */
public class StockGetDateDataRequest extends ServiceRequest {

    public int serviceCode = 5001;//服务号

    public String stockCode = "";//股票代码

    public String stockKData = "";//day日先  week周 month月

}
