package com.xt.lxl.stock.model.reponse;

import com.xt.lxl.stock.model.model.StockRankFilterModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19 0019.
 * 排行详情response
 */

public class StockRankDetailFilterlResponse {
    public int resultCode = 0;
    public List<StockRankFilterModel> mRankFilterList = new ArrayList<>();//界面筛选项列表

}
