package com.xt.lxl.stock.model.reponse;

import com.xt.lxl.stock.model.model.StockSearchModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/20 0020.
 * 热门搜索推荐
 */

public class StockHotSearchResponse {

    public int resultCode = 200;
    public String resultMessage = "";
    public List<StockSearchModel> mHotSearchList = new ArrayList<>();

}
