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
            String stockName = compareModel.stockName;
            if (StringUtil.emptyOrNull(stockName)) {
                stockName = compareModel.stockCode;
            }
            ratioMap.put(stockName, compareModel.ratio);
            incomeMap.put(stockName, compareModel.income);
            priceShowMap.put(stockName, compareModel.pricePerfor);
            shareOutMap.put(stockName, compareModel.shareOut);
        }
        List<View> viewList = new ArrayList<>();

        viewList.add(createBarChart(compareModelList, ratioMap));
        viewList.add(createBarChart(compareModelList, incomeMap));
        viewList.add(createBarChart(compareModelList, priceShowMap));
        viewList.add(createBarChart(compareModelList, shareOutMap));

        return viewList;
    }

    private View createBarChart(List<StockDetailCompareModel> compareModelList, Map<String, Float> map) {
        BarChart mChart = new BarChart(mContainer.getContext());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(-1, DeviceUtil.getPixelFromDip(mContainer.getContext(), 150));
        mChart.setLayoutParams(lp);
        initBarChart(mChart);
        initBarChartXY(mChart);
        bindChartData(mChart, compareModelList, map);
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


    private void initBarChartXY(BarChart mChart) {
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


    private void bindChartData(BarChart mChart, List<StockDetailCompareModel> compareModelList, Map<String, Float> map) {
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < compareModelList.size(); i++) {
            StockDetailCompareModel compareModel = compareModelList.get(i);
            xVals.add(compareModel.stockName);
        }
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < compareModelList.size(); i++) {
            StockDetailCompareModel compareModel = compareModelList.get(i);
            Float f = map.get(compareModel.stockName);
            yVals1.add(new BarEntry(f, i));//填充数据
        }
        BarDataSet set1;
        set1 = new BarDataSet(yVals1, "");
        set1.setBarSpacePercent(60);//设置矩形之间的间距，参数为百分数，可控制矩形的宽度
        set1.setColors(new int[]{Color.parseColor("#FF8704"), Color.parseColor("#186db7"), Color.parseColor("#186db7"), Color.parseColor("#186db7")});
        set1.setDrawValues(true);
        set1.setDrawUpAndDown(false);

        BarData data = new BarData(xVals, set1);
        data.setValueTextSize(10f);
        data.setValueTypeface(Typeface.DEFAULT);
        mChart.setData(data);
    }

}
