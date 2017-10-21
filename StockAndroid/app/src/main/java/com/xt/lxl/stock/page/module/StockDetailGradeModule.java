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
 * 券商评级
 */

public class StockDetailGradeModule extends StockDetailBaseModule {

    private StockTextView mTitle;
    private StockTabGroupButton mTab;
    private LinearLayout mContainer;


    public StockDetailGradeModule(StockViewModel stockViewModel) {
        super(stockViewModel);
    }

    @Override
    public void setModuleView(View view) {
        mTitle = (StockTextView) view.findViewById(R.id.stock_detail_grade_title);
        mTab = (StockTabGroupButton) view.findViewById(R.id.stock_detail_grade_tab);
        mContainer = (LinearLayout) view.findViewById(R.id.stock_detail_grade_container);
    }

    @Override
    public void bindData(StockViewModel stockViewModel) {
        List<String> list = new ArrayList<>();
        list.add("评级变化");
        list.add("平均价格");
        list.add("券商评论");
        mTab.setTabItemArrayText(list);
        mTab.initView();
    }
}
