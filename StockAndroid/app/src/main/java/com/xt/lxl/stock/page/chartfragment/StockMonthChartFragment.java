package com.xt.lxl.stock.page.chartfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.model.model.StockViewModel;

/**
 * Created by xiangleiliu on 2017/10/15.
 */
public class StockMonthChartFragment extends StockBaseChartFragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stock_detail_chart_month_layout, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void refreshAllData(StockViewModel stockViewModel) {

    }
}
