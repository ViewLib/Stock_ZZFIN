package com.stock.model.response;

import com.stock.model.ServiceResponse;
import com.stock.model.model.StockDetailCompanyModel;
import com.stock.model.model.StockDetailStockHolder;

import java.util.ArrayList;
import java.util.List;

public class StockDetailCompanyInfoResponse extends ServiceResponse {
    public StockDetailCompanyModel companyModel = new StockDetailCompanyModel();
    public List<StockDetailStockHolder> stockHolderList = new ArrayList<>();//公司股东
}
