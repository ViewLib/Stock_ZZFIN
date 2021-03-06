package com.stock.model.response;


import com.stock.model.ServiceResponse;
import com.stock.model.model.StockRankResultModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19 0019.
 * 排行详情的response
 */

public class StockRankDetailResponse extends ServiceResponse{
    final public int serviceCode = 2003;//服务号
    public int resultCode = 200;
    public String title = "";//界面标题，比如 本日融资融券的前十家公司
    public List<StockRankResultModel> rankResultList = new ArrayList<>();//界面筛选项列表

}
