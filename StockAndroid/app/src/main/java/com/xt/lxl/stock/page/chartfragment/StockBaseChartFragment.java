package com.xt.lxl.stock.page.chartfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.xt.lxl.stock.model.model.StockViewModel;

/**
 * Created by xiangleiliu on 2017/10/15.
 */
public abstract class StockBaseChartFragment extends Fragment {

    public static final String StockViewModel_TAG = "StockViewModel";


    public abstract void refreshAllData(StockViewModel stockViewModel);


}
