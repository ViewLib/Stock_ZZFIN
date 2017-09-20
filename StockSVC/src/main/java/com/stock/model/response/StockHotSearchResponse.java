package com.stock.model.response;

import com.stock.model.ServiceResponse;
import com.stock.model.model.StockSearchModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/20 0020.
 * 热门搜索推荐
 */

public class StockHotSearchResponse extends ServiceResponse {

    final public int serviceCode = 1001;//服务号
    public int resultCode = 200;//返回结果值
    public String resultMessage = "";//结果描述

    public List<StockSearchModel> hotSearchList = new ArrayList<>();

}
