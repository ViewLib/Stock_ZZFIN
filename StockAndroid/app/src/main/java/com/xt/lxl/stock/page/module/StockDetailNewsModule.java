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
 * 重大消息
 */

public class StockDetailNewsModule extends StockDetailBaseModule {

    private StockTextView mTitle;
    private StockTabGroupButton mTab;//查看更多
    private LinearLayout mContainer;//问题列表


    public StockDetailNewsModule(StockDetailCacheBean cacheBean) {
        super(cacheBean);
    }

    @Override
    public void initModuleView(View view) {
        mTitle = (StockTextView) view.findViewById(R.id.stock_news_title);
        mTab = (StockTabGroupButton) view.findViewById(R.id.stock_detail_tab);
        mContainer = (LinearLayout) view.findViewById(R.id.stock_news_container);

        List<String> list = new ArrayList<>();
        list.add("重组");
        list.add("定增");
        list.add("大宗");
        list.add("分红");
        list.add("投资");
        mTab.setTabItemArrayText(list);
        mTab.initView();
    }

    @Override
    public void bindData() {

    }
}
