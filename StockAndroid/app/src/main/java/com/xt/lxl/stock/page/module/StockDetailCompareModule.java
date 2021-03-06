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
import com.xt.lxl.stock.model.model.StockDetailCompareModel;
import com.xt.lxl.stock.model.reponse.StockDetailCompareResponse;
import com.xt.lxl.stock.page.adapter.StockViewPagerAdapter;
import com.xt.lxl.stock.util.DeviceUtil;
import com.xt.lxl.stock.util.StringUtil;
import com.xt.lxl.stock.viewmodel.StockDetailCacheBean;
import com.xt.lxl.stock.widget.view.StockTabGroupButton2;
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
    private StockTabGroupButton2 mTab;//
    private ViewPager mViewPager;//横向比较内容
    StockViewPagerAdapter adapter;


    public StockDetailCompareModule(StockDetailCacheBean cacheBean, StockDetailListener listener) {
        super(cacheBean, listener);
    }

    @Override
    public void initModuleView(View view) {
        mTitle = (StockTextView) view.findViewById(R.id.stock_detail_compare_title);
        mTab = (StockTabGroupButton2) view.findViewById(R.id.stock_detail_compare_tab);
        mViewPager = (ViewPager) view.findViewById(R.id.stock_detail_compare_pager);
        List<String> list = new ArrayList<>();
        list.add("市盈率");
        list.add("收入增长");
        list.add("年度表现");
        list.add("每股净资产");
        mTab.setTabItemArrayText(list);
    }

    @Override
    public void bindData() {
        StockDetailCompareResponse compareResponse = mCacheBean.compareResponse;
        List<View> viewList = createViewList(compareResponse.compareList);
        adapter = new StockViewPagerAdapter(viewList);
        mViewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mTab.setOnTabItemSelectedListener(new StockTabGroupButton2.OnTabItemSelectedListener() {
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
        Map<String, Float> assetsMap = new HashMap<>();//分红

        for (StockDetailCompareModel compareModel : compareModelList) {
            String stockName = compareModel.stockName;
            if (StringUtil.emptyOrNull(stockName)) {
                stockName = compareModel.stockCode;
            }
            ratioMap.put(stockName, compareModel.ratio);
            incomeMap.put(stockName, compareModel.income);
            priceShowMap.put(stockName, compareModel.pricePerfor);
            assetsMap.put(stockName, compareModel.assets);
        }
        List<View> viewList = new ArrayList<>();

        viewList.add(createBarChart(compareModelList, ratioMap, 1));
        viewList.add(createBarChart(compareModelList, incomeMap, 2));
        viewList.add(createBarChart(compareModelList, priceShowMap, 3));
        viewList.add(createBarChart(compareModelList, assetsMap, 4));

        return viewList;
    }

    private View createBarChart(List<StockDetailCompareModel> compareModelList, Map<String, Float> map, int type) {
        BarChart mChart = new BarChart(mContainer.getContext());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(-1, DeviceUtil.getPixelFromDip(mContainer.getContext(), 150));
        mChart.setLayoutParams(lp);
        initBarChart(mChart);
        initBarChartXY(mChart, type);
        bindChartData(mChart, compareModelList, map, type);
        return mChart;
    }

    private void initBarChart(BarChart mChart) {
        mChart.setNoDataText("没有可展示的数据");
        mChart.setDescription("");
        mChart.setDragEnabled(false);//是否可以拖拽
        mChart.setDrawBarShadow(false);
        mChart.setMaxVisibleValueCount(60);
        mChart.setDrawValueAboveBar(true);
        mChart.setDoubleTapToZoomEnabled(false);
        //不展示比例
        Legend l = mChart.getLegend();
        l.setEnabled(false);
    }


    private void initBarChartXY(BarChart mChart, final int type) {
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(Typeface.DEFAULT);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);//设置x标签的间隙

//        YAxisValueFormatter custom = new YAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, YAxis yAxis) {
//                //大于1亿就展示为1.xx亿
//                if (type == 1) {
//                    return String.valueOf(value) + "x";
//                } else if (type == 2) {
//                    return String.valueOf(value) + "%";
//                } else if (type == 3) {
//                    return String.valueOf(value) + "%";
//                } else if (type == 4) {
//                    return String.valueOf(value) + "元";
//                }
//                return String.valueOf(value);
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


    private void bindChartData(BarChart mChart, List<StockDetailCompareModel> compareModelList, Map<String, Float> map, final int type) {
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        int index = 0;
        for (int i = 0; i < compareModelList.size(); i++) {
            StockDetailCompareModel compareModel = compareModelList.get(i);
            Float f = map.get(compareModel.stockName);
            if (f == 0) {
                continue;
            }
            yVals1.add(new BarEntry(f, index++));//填充数据
            xVals.add(compareModel.stockName);
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

        ValueFormatter custom = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                if (type == 1) {
                    return String.valueOf(value) + "x";
                } else if (type == 2) {
                    return String.valueOf(value) + "%";
                } else if (type == 3) {
                    return String.valueOf(value) + "%";
                } else if (type == 4) {
                    return String.valueOf(value) + "元";
                }
                return String.valueOf(value);
            }
        };
        mChart.getBarData().setValueFormatter(custom);
    }

}
