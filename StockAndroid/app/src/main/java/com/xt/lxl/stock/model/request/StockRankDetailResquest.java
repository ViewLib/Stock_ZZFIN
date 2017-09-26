package com.xt.lxl.stock.model.request;


import com.xt.lxl.stock.model.ServiceRequest;

/**
 * Created by Administrator on 2017/9/19 0019.
 * 排行详情的response
 */

public class StockRankDetailResquest  extends ServiceRequest {
    final public int serviceCode = 2003;//服务号
    public String title = "";//界面标题，比如 本日融资融券的前十家公司
    public int serch_relation;//
}
