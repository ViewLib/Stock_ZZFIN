package com.xt.lxl.stock.model.reponse;


import com.xt.lxl.stock.model.ServiceResponse;
import com.xt.lxl.stock.model.model.StockSearchModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/20 0020.
 * 热门搜索推荐
 */

public class StockHotSearchResponse extends ServiceResponse {

    public int serviceCode = 1001;//服务号

    public List<StockSearchModel> hotSearchList = new ArrayList<>();

}
