package com.xt.lxl.stock.page.module;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.xt.lxl.stock.R;
import com.xt.lxl.stock.listener.StockDetailListener;
import com.xt.lxl.stock.model.model.StockDetailFinanceGroup;
import com.xt.lxl.stock.model.model.StockDetailFinanceItem;
import com.xt.lxl.stock.model.reponse.StockDetailFinanceResponse;
import com.xt.lxl.stock.page.adapter.StockViewPagerAdapter;
import com.xt.lxl.stock.util.DeviceUtil;
import com.xt.lxl.stock.util.StringUtil;
import com.xt.lxl.stock.viewmodel.StockDetailCacheBean;
import com.xt.lxl.stock.widget.view.StockTabGroupButton2;
import com.xt.lxl.stock.widget.view.StockTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2017/10/19 0019.
 * 财务信息
 */

public class StockDetailFinanceModule extends StockDetailBaseModule {

    private StockTextView mTitle;
    private StockTabGroupButton2 mTab;//查看更多
    private ViewPager mViewPager;//问题列表
    private StockViewPagerAdapter adapter;

    public StockDetailFinanceModule(StockDetailCacheBean cacheBean, StockDetailListener listener) {
        super(cacheBean, listener);
    }

    @Override
    public void initModuleView(View view) {
        mTitle = (StockTextView) view.findViewById(R.id.stock_detail_finance_title);
        mTab = (StockTabGroupButton2) view.findViewById(R.id.stock_detail_finance_tab);
        mViewPager = (ViewPager) view.findViewById(R.id.stock_detail_finance_view_pager);
        List<String> list = new ArrayList<>();
        list.add("收入");
        list.add("净利率");
        list.add("毛利率");
        list.add("每股净资产");
        mTab.setTabItemArrayText(list);
        mTab.setOnTabItemSelectedListener(new StockTabGroupButton2.OnTabItemSelectedListener() {
            @Override
            public void onTabItemClicked(int whichButton) {
                //选中
                if (adapter == null) {
                    return;
                }
                if (mViewPager.getAdapter().getCount() <= whichButton) {
                    return;
                }
                mViewPager.setCurrentItem(whichButton);
            }
        });
    }

    @Override
    public void bindData() {
        //刷新数据
        StockDetailFinanceResponse financeResponse = mCacheBean.financeResponse;
        List<View> viewList = createViewList(financeResponse.groupList);
        adapter = new StockViewPagerAdapter(viewList);
        mViewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private List<View> createViewList(List<StockDetailFinanceGroup> financeGroupList) {
        List<View> viewList = new ArrayList<>();
        for (int i = 0; i < financeGroupList.size(); i++) {
            StockDetailFinanceGroup stockDetailFinanceGroup = financeGroupList.get(i);
            View bar = createBarChart(stockDetailFinanceGroup);
            viewList.add(bar);
        }
        return viewList;
    }

    private View createBarChart(StockDetailFinanceGroup stockDetailFinanceGroup) {
        BarChart mChart = new BarChart(mContainer.getContext());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(-1, DeviceUtil.getPixelFromDip(mContainer.getContext(), 150));
        mChart.setLayoutParams(lp);
        initBarChart(mChart);
        initBarChartXY(mChart, stockDetailFinanceGroup);
        bindChartData(mChart, stockDetailFinanceGroup);
        return mChart;
    }

    private void initBarChart(BarChart mChart) {
        mChart.setNoDataText("没有可展示的数据");
        mChart.setDescription("");
        mChart.setDragEnabled(false);//是否可以拖拽
        mChart.setDrawBarShadow(false);
        mChart.setMaxVisibleValueCount(60);
        mChart.setDrawValueAboveBar(true);//允许在水平以下画线
        mChart.setDoubleTapToZoomEnabled(false);

        //不展示比例
        Legend l = mChart.getLegend();
        l.setEnabled(false);
    }


    private void initBarChartXY(BarChart mChart, final StockDetailFinanceGroup stockDetailFinanceGroup) {
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(Typeface.DEFAULT);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);//设置x标签的间隙

//        YAxisValueFormatter custom = new YAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, YAxis yAxis) {
//                //大于1亿就展示为1.xx亿
//                return stockDetailFinanceGroup.getShowItemUnit(value);
//            }
//        };

//        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setTypeface(Typeface.DEFAULT);
//        leftAxis.setLabelCount(6, false);
//        leftAxis.setValueFormatter(custom);
//        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//        leftAxis.setSpaceTop(20f);
//        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        mChart.getAxisRight().setEnabled(false);//隐藏右边的坐标轴
        mChart.getAxisLeft().setEnabled(false);
    }


    private void bindChartData(BarChart mChart, final StockDetailFinanceGroup stockDetailFinanceGroup) {
        ArrayList<StockDetailFinanceItem> financeItemList = stockDetailFinanceGroup.yearItemList;
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < financeItemList.size(); i++) {
            StockDetailFinanceItem stockDetailFinanceItem = financeItemList.get(i);
            xVals.add(stockDetailFinanceItem.dateStr);//格式化成年
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < financeItemList.size(); i++) {
            StockDetailFinanceItem stockDetailFinanceItem = financeItemList.get(i);
            float y = StringUtil.toFloat(stockDetailFinanceItem.valueStr);
            yVals1.add(new BarEntry(y, i));//填充数据
        }

        BarDataSet set1;
//        mChart.animateY(2000);//设置动画
        set1 = new BarDataSet(yVals1, "");
        set1.setBarSpacePercent(60);//设置矩形之间的间距，参数为百分数，可控制矩形的宽度
        List<Integer> list = new ArrayList<Integer>();
        list.add(Color.parseColor("#186db7"));//设置颜色
        set1.setColors(list);
        set1.setDrawValues(true);

        BarData data = new BarData(xVals, set1);
        data.setValueTextSize(10f);
        data.setValueTypeface(Typeface.DEFAULT);
        data.setDrawValues(true);
        mChart.setData(data);


        ValueFormatter custom = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return stockDetailFinanceGroup.getShowItemUnit(value);
            }
        };
        mChart.getBarData().setValueFormatter(custom);
    }

}
