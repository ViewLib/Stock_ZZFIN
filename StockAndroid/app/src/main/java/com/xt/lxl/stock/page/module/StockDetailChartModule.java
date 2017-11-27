package com.xt.lxl.stock.page.module;

import android.view.View;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.listener.StockDetailListener;
import com.xt.lxl.stock.viewmodel.StockDetailCacheBean;
import com.xt.lxl.stock.widget.view.StockDetailChartView;

/**
 * Created by Administrator on 2017/10/19 0019.
 */

public class StockDetailChartModule extends StockDetailBaseModule {
    StockDetailChartView detailChartView;

<<<<<<< HEAD
    public StockDetailChartModule(StockDetailCacheBean cacheBean, StockDetailListener listener) {
        super(cacheBean,listener);
=======
    public StockDetailChartModule(StockDetailCacheBean cacheBean) {
        super(cacheBean, null);
>>>>>>> 详情页删除股票功能
    }

    @Override
    public void initModuleView(View view) {
        detailChartView = (StockDetailChartView) view.findViewById(R.id.stock_kline);
        detailChartView.setStockViewModel(mCacheBean.mStockViewModel);
    }

    @Override
    public void bindData() {


    }

}
