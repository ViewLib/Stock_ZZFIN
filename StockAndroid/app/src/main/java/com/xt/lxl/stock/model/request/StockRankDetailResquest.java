package com.xt.lxl.stock.model.request;


import com.xt.lxl.stock.model.ServiceRequest;
import com.xt.lxl.stock.model.model.StockRankResultModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19 0019.
 * 排行详情的response
 */

public class StockRankDetailResquest  extends ServiceRequest {
    final public int serviceCode = 2003;//服务号
    public String mTitle = "";//界面标题，比如 本日融资融券的前十家公司
    public List<StockRankResultModel> mRankResultList = new ArrayList<>();//界面筛选项列表

}
