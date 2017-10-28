package com.xt.lxl.stock.page.module;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.model.model.StockDetailFinanceGroup;
import com.xt.lxl.stock.model.reponse.StockDetailFinanceResponse;
import com.xt.lxl.stock.page.adapter.StockViewPagerAdapter;
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
    private ViewPager mViewPager;//问题列表
    private StockViewPagerAdapter adapter;


    public StockDetailFinanceModule(StockDetailCacheBean cacheBean) {
        super(cacheBean);
    }

    @Override
    public void initModuleView(View view) {
        mTitle = (StockTextView) view.findViewById(R.id.stock_detail_finance_title);
        mTab = (StockTabGroupButton) view.findViewById(R.id.stock_detail_finance_tab);
        mViewPager = (ViewPager) view.findViewById(R.id.stock_detail_finance_view_pager);
        List<String> list = new ArrayList<>();
        list.add("收入");
        list.add("净利率");
        list.add("毛利率");
        list.add("分红率");
        mTab.setTabItemArrayText(list);
        mTab.initView();
        mTab.setOnTabItemSelectedListener(new StockTabGroupButton.OnTabItemSelectedListener() {
            @Override
            public void onTabItemClicked(int whichButton) {
                //选中

            }
        });
    }

    @Override
    public void bindData() {
        //刷新数据
        StockDetailFinanceResponse financeResponse = mCacheBean.financeResponse;
        Log.i("lxltest", "financeResponse:" + financeResponse.groupList.size());
        List<View> viewList = createViewList(financeResponse.groupList);
        adapter = new StockViewPagerAdapter(viewList);
        mViewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private List<View> createViewList(List<StockDetailFinanceGroup> financeGroupList) {
        List<View> viewList = new ArrayList<>();


        return viewList;
    }

}
