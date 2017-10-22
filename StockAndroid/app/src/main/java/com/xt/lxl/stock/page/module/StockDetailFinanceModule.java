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
 * 财务信息
 */

public class StockDetailFinanceModule extends StockDetailBaseModule {

    private StockTextView mTitle;
    private StockTabGroupButton mTab;//查看更多
    private LinearLayout mContainer;//问题列表


    public StockDetailFinanceModule(StockViewModel stockViewModel) {
        super(stockViewModel);
    }

    @Override
    public void setModuleView(View view) {
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
    public void bindData(StockViewModel stockViewModel) {

    }

}
