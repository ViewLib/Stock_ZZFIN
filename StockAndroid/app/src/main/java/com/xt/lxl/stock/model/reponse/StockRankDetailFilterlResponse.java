package com.xt.lxl.stock.model.reponse;


import com.xt.lxl.stock.model.ServiceResponse;
import com.xt.lxl.stock.model.model.StockRankFilterGroupModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19 0019.
 * 排行详情筛选项response
 */

public class StockRankDetailFilterlResponse extends ServiceResponse {

    public int serviceCode = 2002;//服务号
    public List<StockRankFilterGroupModel> rankFilterList = new ArrayList<>();//界面筛选项列表

}
