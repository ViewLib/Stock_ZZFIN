package com.xt.lxl.stock.page.module;

import android.content.Context;
import android.view.View;

import com.xt.lxl.stock.viewmodel.StockDetailCacheBean;

/**
 * Created by xiangleiliu on 2017/10/20.
 */
public abstract class StockDetailBaseModule {

    protected View mContainer;
    protected StockDetailCacheBean mCacheBean;
    protected Context mContext;

    public StockDetailBaseModule(StockDetailCacheBean cacheBean) {
        mCacheBean = cacheBean;
    }

    public void setModuleView(View view) {
        mContainer = view;
        this.mContext = mContainer.getContext();
        initModuleView(view);
    }

    protected abstract void initModuleView(View view);

    public abstract void bindData();
}
