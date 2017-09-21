package com.xt.lxl.stock.model.request;


import com.xt.lxl.stock.model.ServiceRequest;

/**
 * 用户注册
 */
public class StockUserRegisterRequest extends ServiceRequest {
    public int serviceCode = 3001;//服务号

    public String moblie = "";
    public String clientId = "";
}
