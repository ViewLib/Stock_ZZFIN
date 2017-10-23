package com.xt.lxl.stock.model.reponse;


import com.xt.lxl.stock.model.ServiceResponse;
import com.xt.lxl.stock.model.model.StockDetailCompanyModel;
import com.xt.lxl.stock.model.model.StockDetailStockHolder;

import java.util.ArrayList;
import java.util.List;

public class StockDetailCompanyInfoResponse extends ServiceResponse {
    public StockDetailCompanyModel companyModel = new StockDetailCompanyModel();
    public List<StockDetailStockHolder> stockHolderList = new ArrayList<>();//公司股东
}
