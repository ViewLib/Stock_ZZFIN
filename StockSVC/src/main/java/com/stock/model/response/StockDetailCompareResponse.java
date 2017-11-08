package com.stock.model.response;

import com.stock.model.ServiceResponse;
import com.stock.model.model.StockDetailCompareModel;
import com.stock.model.model.StockViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/11/8.
 */
public class StockDetailCompareResponse extends ServiceResponse {
    public List<StockDetailCompareModel> compareList = new ArrayList<>();
}
