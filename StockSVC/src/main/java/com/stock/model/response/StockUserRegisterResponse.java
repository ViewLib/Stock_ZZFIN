package com.stock.model.response;

import com.stock.model.ServiceResponse;
import com.stock.model.model.StockUserModel;

public class StockUserRegisterResponse extends ServiceResponse{
    public final int serviceCode = 3001;//服务号
    public StockUserModel userModel = new StockUserModel();
}
