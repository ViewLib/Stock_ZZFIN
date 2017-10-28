package com.xt.lxl.stock.viewmodel;

import com.xt.lxl.stock.model.model.StockDetailCompanyModel;
import com.xt.lxl.stock.model.model.StockDetailGradleModel;
import com.xt.lxl.stock.model.model.StockDetailStockHolder;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.model.reponse.StockDetailFinanceResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/21 0021.
 */

public class StockDetailCacheBean {

    public StockViewModel mStockViewModel = new StockViewModel();
    public boolean isAdd;//是否添加过该股票
    public StockDetailCompanyModel stockDetailCompanyModel = new StockDetailCompanyModel();//是否添加过该股票
    public List<StockDetailStockHolder> stockHolderList = new ArrayList<>();//十大股东

    public List<StockDetailGradleModel> gradleModelList = new ArrayList<>();//券商评级列表

    public StockDetailFinanceResponse financeResponse = new StockDetailFinanceResponse();//财务信息

}

