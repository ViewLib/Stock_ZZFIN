package com.stock.model.response;

import com.stock.model.ServiceResponse;
import com.stock.model.model.StockUserModel;

public class StockUserCompletionResponse extends ServiceResponse {
    public final int serviceCode = 3002;//服务号
    public StockUserModel userModel = new StockUserModel();
}
