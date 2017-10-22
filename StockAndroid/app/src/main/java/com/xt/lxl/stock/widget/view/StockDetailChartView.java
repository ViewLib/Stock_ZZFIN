package com.xt.lxl.stock.widget.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.page.chartfragment.StockBaseChartFragment;
import com.xt.lxl.stock.page.chartfragment.StockDayChartFragment;
import com.xt.lxl.stock.page.chartfragment.StockMinuteChartFragment;
import com.xt.lxl.stock.page.chartfragment.StockMonthChartFragment;
import com.xt.lxl.stock.page.chartfragment.StockWeekChartFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/10 0010.
 * K线图
 */

public class StockDetailChartView extends LinearLayout {

    StockTabGroupButton tabGroupButton;//
    FrameLayout fragmentContainer;
    FragmentActivity activity;
    StockViewModel mStockViewModel;

    List<StockBaseChartFragment> viewList = new ArrayList<>();

    public StockDetailChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (FragmentActivity) context;
        inflate(context, R.layout.stock_detail_chart_layout, this);
        initView();
    }

    private void bindData() {
        List<String> list = new ArrayList<>();
        list.add("分时");
        list.add("日");
        list.add("周");
        list.add("月");
        tabGroupButton.setTabItemArrayText(list);
        tabGroupButton.initView();

        StockBaseChartFragment minuteFragment = new StockMinuteChartFragment();
        StockBaseChartFragment dayFragment = new StockDayChartFragment();
        StockBaseChartFragment weekFragment = new StockWeekChartFragment();
        StockBaseChartFragment monthFragment = new StockMonthChartFragment();
        viewList.add(minuteFragment);
        viewList.add(dayFragment);
        viewList.add(weekFragment);
        viewList.add(monthFragment);
        for (StockBaseChartFragment base : viewList) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(StockBaseChartFragment.StockViewModel_TAG, mStockViewModel);
            base.setArguments(bundle);
        }

        tabGroupButton.setOnTabItemSelectedListener(new StockTabGroupButton.OnTabItemSelectedListener() {
            @Override
            public void onTabItemClicked(int whichButton) {
                FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                Fragment from = null;
                List<Fragment> fragments = supportFragmentManager.getFragments();
                for (Fragment fragment : fragments) {
                    if (!fragment.isHidden()) {
                        from = fragment;
                    }
                }
                StockBaseChartFragment stockBaseChartFragment = viewList.get(whichButton);
                stockBaseChartFragment.refreshAllData(mStockViewModel);
                if (from == stockBaseChartFragment) {
                    return;
                }
                if (!stockBaseChartFragment.isAdded()) {
                    if (from != null) {
                        fragmentTransaction.hide(from);
                    }
                    fragmentTransaction.add(R.id.fragment_container, stockBaseChartFragment);
                } else {
                    if (from != null) {
                        fragmentTransaction.hide(from);
                    }
                    fragmentTransaction.show(stockBaseChartFragment);
                }
                fragmentTransaction.commit();
            }
        });

        //默认第一个
        StockBaseChartFragment stockBaseChartFragment = viewList.get(0);
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, stockBaseChartFragment);
        fragmentTransaction.commit();
        stockBaseChartFragment.refreshAllData(mStockViewModel);
    }

    private void initView() {
        tabGroupButton = (StockTabGroupButton) findViewById(R.id.tab_group);
        fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
    }

    public void setStockViewModel(StockViewModel stockViewModel) {
        this.mStockViewModel = stockViewModel;
        bindData();
    }

}
