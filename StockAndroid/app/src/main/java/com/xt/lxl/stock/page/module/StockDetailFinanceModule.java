package com.xt.lxl.stock.page.module;

import android.view.View;
import android.widget.LinearLayout;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.viewmodel.StockDetailCacheBean;
import com.xt.lxl.stock.widget.view.StockTabGroupButton;
import com.xt.lxl.stock.widget.view.StockTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/19 0019.
 * 财务信息
 */

public class StockDetailFinanceModule extends StockDetailBaseModule {

    private StockTextView mTitle;
    private StockTabGroupButton mTab;//查看更多
    private LinearLayout mContainer;//问题列表


    public StockDetailFinanceModule(StockDetailCacheBean cacheBean) {
        super(cacheBean);
    }

    @Override
    public void initModuleView(View view) {
        mTitle = (StockTextView) view.findViewById(R.id.stock_detail_finance_title);
        mTab = (StockTabGroupButton) view.findViewById(R.id.stock_detail_finance_tab);
        mContainer = (LinearLayout) view.findViewById(R.id.stock_detail_finance_container);
        List<String> list = new ArrayList<>();
        list.add("收入");
        list.add("毛利");
        list.add("净利");
        list.add("现金");
        list.add("分红");

        mTab.setTabItemArrayText(list);
        mTab.initView();
    }

    @Override
    public void bindData() {

    }

}
