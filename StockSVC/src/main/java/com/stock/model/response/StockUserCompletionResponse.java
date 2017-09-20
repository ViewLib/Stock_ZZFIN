package com.stock.model.response;

import com.stock.model.model.StockUserModel;

public class StockUserCompletionResponse {
    public final int serviceCode = 3002;//服务号
    public int resultCode = 200;
    public String resultMessage = "";
    public StockUserModel userModel = new StockUserModel();
}
