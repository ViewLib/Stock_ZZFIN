package com.xt.lxl.stock.page.module;

import android.view.View;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.widget.view.StockDetailChartView;

/**
 * Created by Administrator on 2017/10/19 0019.
 */

public class StockDetailChartModule extends StockDetailBaseModule {
    StockDetailChartView detailChartView;

    public StockDetailChartModule(StockViewModel stockViewModel) {
        super(stockViewModel);
    }

    @Override
    public void setModuleView(View view) {
        detailChartView = (StockDetailChartView) view.findViewById(R.id.stock_kline);
        detailChartView.setStockViewModel(mStockViewModel);
    }

    @Override
    public void bindData(StockViewModel stockViewModel) {

    }

}
