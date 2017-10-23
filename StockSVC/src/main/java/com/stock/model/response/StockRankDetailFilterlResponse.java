package com.stock.model.response;

import com.stock.model.ServiceResponse;
import com.stock.model.model.StockRankFilterGroupModel;
import com.stock.model.model.StockRankFilterModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19 0019.
 * 排行详情筛选项response
 */

public class StockRankDetailFilterlResponse extends ServiceResponse {
    final public int serviceCode = 2002;//服务号
    public int resultCode = 200;//返回结果值
    public String resultMessage = "";//结果描述

    public List<StockRankFilterGroupModel> rankFilterList = new ArrayList<>();//界面筛选项列表

}
