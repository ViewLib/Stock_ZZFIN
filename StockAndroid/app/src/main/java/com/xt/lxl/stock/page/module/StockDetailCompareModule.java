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
 * 公司介绍
 */

public class StockDetailCompareModule extends StockDetailBaseModule {

    private StockTextView mTitle;
    private StockTabGroupButton mTab;//查看更多
    private LinearLayout mContainer;//问题列表


    public StockDetailCompareModule(StockDetailCacheBean cacheBean) {
        super(cacheBean);
    }

    @Override
    public void initModuleView(View view) {
        mTitle = (StockTextView) view.findViewById(R.id.stock_detail_compare_title);
        mTab = (StockTabGroupButton) view.findViewById(R.id.stock_detail_compare_tab);
        mContainer = (LinearLayout) view.findViewById(R.id.stock_detail_compare_pager);
        List<String> list = new ArrayList<>();
        list.add("市盈率");
        list.add("融资融券");
        list.add("收入增长");
        list.add("年度表现");
        list.add("分红比例");
        mTab.setTabItemArrayText(list);
        mTab.initView();
    }

    @Override
    public void bindData() {

    }
}
