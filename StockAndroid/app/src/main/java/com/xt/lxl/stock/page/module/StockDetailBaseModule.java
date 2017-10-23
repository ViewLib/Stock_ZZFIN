package com.xt.lxl.stock.page.module;

import android.view.View;

import com.xt.lxl.stock.viewmodel.StockDetailCacheBean;

/**
 * Created by xiangleiliu on 2017/10/20.
 */
public abstract class StockDetailBaseModule {

    protected View mContainer;
    protected StockDetailCacheBean mCacheBean;

    public StockDetailBaseModule(StockDetailCacheBean cacheBean) {
        mCacheBean = cacheBean;
    }

    public void setModuleView(View view) {
        mContainer = view;
        initModuleView(view);
    }

    protected abstract void initModuleView(View view);

    public abstract void bindData();
}
