package com.stock.model.request;

import com.stock.model.ServiceRequest;
import com.stock.model.ServiceResponse;
import com.stock.model.model.StockSearchModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/20 0020.
 * 热门搜索推荐
 */

public class StockHotSearchRequest extends ServiceRequest {

    final public int serviceCode = 2002;//服务号
    public int versionCode = 1;

    //不需要基本参数

}
