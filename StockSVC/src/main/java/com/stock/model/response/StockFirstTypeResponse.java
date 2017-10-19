package com.stock.model.response;

import com.stock.model.ServiceResponse;
import com.stock.model.model.StockFirstTypeModel;
import com.stock.model.model.StockRankFilterModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2017/9/28.
 */
public class StockFirstTypeResponse extends ServiceResponse {

    final public int serviceCode = 2004;//服务号
    public int resultCode = 200;//返回结果值
    public String resultMessage = "";//结果描述
    public List<StockFirstTypeModel> stockFirstTypeModels = new ArrayList<>();//界面筛选项列表
}
