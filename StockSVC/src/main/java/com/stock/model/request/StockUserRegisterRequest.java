package com.stock.model.request;

import com.stock.model.ServiceRequest;

/**
 * 用户注册
 */
public class StockUserRegisterRequest extends ServiceRequest {
    public final int serviceCode = 3001;//服务号

    public String moblie = "";
    public String clientId = "";
}
