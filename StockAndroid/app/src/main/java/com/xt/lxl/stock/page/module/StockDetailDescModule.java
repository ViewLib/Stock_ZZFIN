package com.xt.lxl.stock.page.module;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.widget.view.StockTabGroupButton;
import com.xt.lxl.stock.widget.view.StockTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/19 0019.
 * 公司介绍
 */

public class StockDetailDescModule extends StockDetailBaseModule {

    private StockTextView mTitle;
    private StockTabGroupButton mTab;//查看更多
    private LinearLayout mContainer;//问题列表


    public StockDetailDescModule(StockViewModel stockViewModel) {
        super(stockViewModel);
    }

    @Override
    public void setModuleView(View view) {
        mTitle = (StockTextView) view.findViewById(R.id.stock_detail_desc_title);
        mTab = (StockTabGroupButton) view.findViewById(R.id.stock_detail_desc_tab);
        mContainer = (LinearLayout) view.findViewById(R.id.stock_detail_event_lookmore);
        List<String> list = new ArrayList<>();
        list.add("公司简介");
        list.add("产品");
        list.add("区域");
        list.add("费用");
        list.add("员工");
        mTab.setTabItemArrayText(list);
        mTab.initView();
    }

    @Override
    public void bindData(StockViewModel stockViewModel) {

    }
}
