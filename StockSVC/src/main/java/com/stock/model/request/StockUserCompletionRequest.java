package com.stock.model.request;

import com.stock.model.ServiceRequest;

/**
 * 用户信息补全
 */
public class StockUserCompletionRequest extends ServiceRequest {
    public final int serviceCode = 3002;//服务号
    public int versionCode = 1;

    public int userid;
    public String moblie;
    public String nickname;
    public String area;
    public int age;
}
