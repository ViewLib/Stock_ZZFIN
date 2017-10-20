package com.xt.lxl.stock.page.module;

import android.view.View;

import com.xt.lxl.stock.model.model.StockViewModel;

/**
 * Created by xiangleiliu on 2017/10/20.
 */
public abstract class StockDetailBaseModule {

    protected StockViewModel mStockViewModel;

    public StockDetailBaseModule(StockViewModel stockViewModel) {
        mStockViewModel = stockViewModel;
    }

    public abstract void setModuleView(View view);

    public abstract void bindData(StockViewModel stockViewModel);
}
