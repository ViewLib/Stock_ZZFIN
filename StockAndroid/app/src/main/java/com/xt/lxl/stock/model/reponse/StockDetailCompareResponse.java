package com.xt.lxl.stock.model.reponse;


import com.xt.lxl.stock.model.ServiceResponse;
import com.xt.lxl.stock.model.model.StockDetailCompareModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/11/8.
 */
public class StockDetailCompareResponse extends ServiceResponse {
    public List<StockDetailCompareModel> compareList = new ArrayList<>();//返回的是几个对比股票的数据
}
