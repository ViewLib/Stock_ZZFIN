package com.xt.lxl.stock.model.reponse;


import com.xt.lxl.stock.model.ServiceResponse;
import com.xt.lxl.stock.model.model.StockUserModel;

public class StockUserCompletionResponse extends ServiceResponse {
    public int serviceCode = 3002;//服务号
    public StockUserModel userModel = new StockUserModel();
}
