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
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.xt.lxl.stock.R;
import com.xt.lxl.stock.model.model.StockDetailCompareModel;
import com.xt.lxl.stock.model.model.StockDetailFinanceGroup;
import com.xt.lxl.stock.model.model.StockDetailFinanceItem;
import com.xt.lxl.stock.model.reponse.StockDetailCompareResponse;
import com.xt.lxl.stock.page.adapter.StockViewPagerAdapter;
import com.xt.lxl.stock.util.DeviceUtil;
import com.xt.lxl.stock.util.StringUtil;
import com.xt.lxl.stock.viewmodel.StockDetailCacheBean;
import com.xt.lxl.stock.widget.view.StockTabGroupButton;
import com.xt.lxl.stock.widget.view.StockTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/19 0019.
 * 横向比较
 */

public class StockDetailCompareModule extends StockDetailBaseModule {

    private StockTextView mTitle;
    private StockTabGroupButton mTab;//
    private ViewPager mViewPager;//横向比较内容
    StockViewPagerAdapter adapter;


    public StockDetailCompareModule(StockDetailCacheBean cacheBean) {
        super(cacheBean);
    }

    @Override
    public void initModuleView(View view) {
        mTitle = (StockTextView) view.findViewById(R.id.stock_detail_compare_title);
        mTab = (StockTabGroupButton) view.findViewById(R.id.stock_detail_compare_tab);
        mViewPager = (ViewPager) view.findViewById(R.id.stock_detail_compare_pager);
        List<String> list = new ArrayList<>();
        list.add("市盈率");
        list.add("收入增长");
        list.add("年度表现");
        list.add("分红比例");
        mTab.setTabItemArrayText(list);
        mTab.initView();
    }

    @Override
    public void bindData() {
        StockDetailCompareResponse compareResponse = mCacheBean.compareResponse;
        List<View> viewList = createViewList(compareResponse.compareList);
        adapter = new StockViewPagerAdapter(viewList);
        mViewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mTab.setOnTabItemSelectedListener(new StockTabGroupButton.OnTabItemSelectedListener() {
            @Override
            public void onTabItemClicked(int whichButton) {
                mViewPager.setCurrentItem(whichButton);
            }
        });
    }

    private List<View> createViewList(List<StockDetailCompareModel> compareModelList) {
        //进行数据整理
        Map<String, Float> ratioMap = new HashMap<>();//市盈率
        Map<String, Float> incomeMap = new HashMap<>();//收入增长
        Map<String, Float> priceShowMap = new HashMap<>();//年度表现
        Map<String, Float> shareOutMap = new HashMap<>();//分红

        for (StockDetailCompareModel compareModel : compareModelList) {
            ratioMap.put(compareModel.stockCode, compareModel.ratio);
            incomeMap.put(compareModel.stockCode, compareModel.income);
            priceShowMap.put(compareModel.stockCode, compareModel.pricePerfor);
            shareOutMap.put(compareModel.stockCode, compareModel.shareOut);
        }
        List<View> viewList = new ArrayList<>();

        viewList.add(createBarChart(ratioMap));
        viewList.add(createBarChart(incomeMap));
        viewList.add(createBarChart(priceShowMap));
        viewList.add(createBarChart(shareOutMap));

        return viewList;
    }

    private View createBarChart(Map<String, Float> map) {
        BarChart mChart = new BarChart(mContainer.getContext());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(-1, DeviceUtil.getPixelFromDip(mContainer.getContext(), 150));
        mChart.setLayoutParams(lp);
        initBarChart(mChart);
        initBarChartXY(mChart, map);
        bindChartData(mChart, map);
        return mChart;
    }

    private void initBarChart(BarChart mChart) {
        mChart.setNoDataText("没有可展示的数据");
        mChart.setDescription("");
        mChart.setDragEnabled(false);//是否可以拖拽
        mChart.setDrawBarShadow(false);
        mChart.setMaxVisibleValueCount(60);
        mChart.setDrawValueAboveBar(false);//允许在水平以下画线
        //不展示比例
        Legend l = mChart.getLegend();
        l.setEnabled(false);
    }


    private void initBarChartXY(BarChart mChart, final Map<String, Float> map) {
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(Typeface.DEFAULT);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);//设置x标签的间隙

        YAxisValueFormatter custom = new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                //大于1亿就展示为1.xx亿
                return String.valueOf(value);
            }
        };

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(Typeface.DEFAULT);
        leftAxis.setLabelCount(6, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(20f);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        mChart.getAxisRight().setEnabled(false);//隐藏右边的坐标轴
    }


    private void bindChartData(BarChart mChart, Map<String, Float> map) {
        ArrayList<String> xVals = new ArrayList<String>();
        for (String stockName : map.keySet()) {
            xVals.add(stockName);
        }
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        int i = 0;
        for (String stockName : map.keySet()) {
            Float f = map.get(stockName);
            yVals1.add(new BarEntry(f, i++));//填充数据
        }

        BarDataSet set1;
//        mChart.animateY(2000);//设置动画
        set1 = new BarDataSet(yVals1, "");
        set1.setBarSpacePercent(60);//设置矩形之间的间距，参数为百分数，可控制矩形的宽度
        List<Integer> list = new ArrayList<Integer>();
        list.add(Color.parseColor("#186db7"));//设置颜色
        set1.setColors(list);
        set1.setDrawValues(false);

        BarData data = new BarData(xVals, set1);
        data.setValueTextSize(10f);
        data.setValueTypeface(Typeface.DEFAULT);
        mChart.setData(data);
    }

}
