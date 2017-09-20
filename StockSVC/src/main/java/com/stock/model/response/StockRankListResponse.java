package com.stock.model.response;


import com.stock.model.model.StockRankResultModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19 0019.
 * top10排行列表
 */

public class StockRankListResponse {
    final public int serviceCode = 2001;//服务号
    public int resultCode = 0;
    public String mTitle = "";//界面标题，比如 本日融资融券的前十家公司
    public List<StockRankResultModel> mRankResultList = new ArrayList<>();//界面筛选项列表

}
