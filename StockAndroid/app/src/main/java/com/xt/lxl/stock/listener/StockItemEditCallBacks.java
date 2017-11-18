package com.xt.lxl.stock.listener;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by xiangleiliu on 2017/8/6.
 */
public class StockItemEditCallBacks {

    //搜索添加股票，以及历史记录添加股票
    public View.OnClickListener mActionCallBack;//搜索的股票点击添加

    public View.OnClickListener mHotSearchCallBack;//热门搜索的点击

    public AdapterView.OnItemClickListener mSearchHistoryItemCallBack;//历史搜索结果的单个点击

    public AdapterView.OnItemClickListener mSearchResultItemCallBack;//搜索结果的单个点击

}
