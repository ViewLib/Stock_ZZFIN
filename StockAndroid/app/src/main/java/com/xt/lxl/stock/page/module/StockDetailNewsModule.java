package com.xt.lxl.stock.page.module;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.xt.lxl.stock.R;
import com.xt.lxl.stock.listener.StockDetailListener;
import com.xt.lxl.stock.model.model.StockDateDataModel;
import com.xt.lxl.stock.model.model.StockEventsDataList;
import com.xt.lxl.stock.model.model.StockEventDataModel;
import com.xt.lxl.stock.model.reponse.StockEventsDataResponse;
import com.xt.lxl.stock.page.adapter.StockViewPagerAdapter;
import com.xt.lxl.stock.util.DateUtil;
import com.xt.lxl.stock.util.DeviceUtil;
import com.xt.lxl.stock.viewmodel.StockDetailCacheBean;
import com.xt.lxl.stock.widget.stockchart.bean.DayViewModel;
import com.xt.lxl.stock.widget.view.StockTabGroupButton2;
import com.xt.lxl.stock.widget.view.StockTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/19 0019.
 * 重大消息
 */

public class StockDetailNewsModule extends StockDetailBaseModule implements View.OnClickListener {

    private StockTextView mTitle;
    private StockTabGroupButton2 mTab;//查看更多
    private ViewPager mViewPager;//问题列表
    private RelativeLayout mStockNewDetail;
    private LineChart mLineChart;
    private StockViewPagerAdapter adapter;


    public StockDetailNewsModule(StockDetailCacheBean cacheBean, StockDetailListener listener) {
        super(cacheBean, listener);
    }

    @Override
    public void initModuleView(View view) {
        mTitle = (StockTextView) view.findViewById(R.id.stock_news_title);
        mTab = (StockTabGroupButton2) view.findViewById(R.id.stock_detail_news_tab);
        mLineChart = (LineChart) view.findViewById(R.id.stock_detail_linechart);
        mStockNewDetail = (RelativeLayout) view.findViewById(R.id.stock_detail_news_detail);
        mViewPager = (ViewPager) view.findViewById(R.id.stock_detail_news_view_pager);
        mStockNewDetail = (RelativeLayout) view.findViewById(R.id.stock_detail_news_detail);
        mStockNewDetail.setOnClickListener(this);
        List<String> list = new ArrayList<>();
        list.add("重组");
        list.add("定增");
        list.add("大宗");
        list.add("分红");
        list.add("投资");
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
                mViewPager.setCurrentItem(whichButton, false);
            }
        });
    }

    @Override
    public void bindData() {
        StockEventsDataResponse newsResponse = mCacheBean.newsResponse;
        DayViewModel dayViewModel = mCacheBean.dayViewModel;
        List<StockDateDataModel> dataList = dayViewModel.dateDataList;//重大事件日线图

        //K线图不变化
        initBarChart(mLineChart);
        initBarChartXY(mLineChart, dataList);
        bindChartData(mLineChart, dataList);

        //线程刷新
        //消息事件发生变化
        List<View> viewList = createViewList(dataList, newsResponse.stockEventsDataLists, mLineChart);
        adapter = new StockViewPagerAdapter(viewList);
        mViewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private List<View> createViewList(List<StockDateDataModel> dataList, List<StockEventsDataList> stockEventsDataLists, LineChart lineChart) {
        List<View> viewList = new ArrayList<>();
        if (dataList.size() <= 1) {
            return viewList;
        }
        int showNum = dataList.size();//一屏内展示的数量
        YAxis leftAxis = lineChart.getAxisLeft();
        float axisMaximum = leftAxis.getAxisMaximum();
        float axisMinimum = leftAxis.getAxisMinimum();

        int leftPadding = DeviceUtil.getPixelFromDip(mContext, 29);
        int rigntPadding = DeviceUtil.getPixelFromDip(mContext, 15);
        int topPadding = DeviceUtil.getPixelFromDip(mContext, 15);
        int bottomPadding = DeviceUtil.getPixelFromDip(mContext, 15);
        int containerHeight = DeviceUtil.getPixelFromDip(mContext, 150);
        double itemHeight = ((double) (containerHeight - topPadding - bottomPadding)) / (axisMaximum - axisMinimum);//1分价格对应的高度
        double itemWidth = (double) (DeviceUtil.getScreenWidth(mContext) - leftPadding - rigntPadding) / (showNum - 1);

        int iconWidth = DeviceUtil.getPixelFromDip(mContext, 15);
        int iconHeight = DeviceUtil.getPixelFromDip(mContext, 12);


        for (int i = 0; i < stockEventsDataLists.size(); i++) {
            RelativeLayout eventContainer = new RelativeLayout(mContainer.getContext());
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            eventContainer.setPadding(leftPadding, topPadding, rigntPadding, bottomPadding);
            eventContainer.setLayoutParams(lp);

            StockEventsDataList eventsDataList = stockEventsDataLists.get(i);
            Map<String, StockEventDataModel> eventMap = new HashMap<>();
            for (StockEventDataModel eventsDataModel : eventsDataList.stockEventsDataModels) {
                if (eventMap.get(eventsDataModel.eventDate) == null) {
                    eventMap.put(eventsDataModel.eventDate, eventsDataModel);
                }
            }
            boolean isShowBigIcon = false;
//            boolean isShowBigIcon = eventMap.size() <= 1;

            //向RelativeLayout中添加view
            for (int k = 0; k < dataList.size(); k++) {
                StockDateDataModel model = dataList.get(k);
                StockEventDataModel eventsDataModel = eventMap.get(model.date);
                if (eventsDataModel == null) {
                    continue;
                }
                int showWidth = isShowBigIcon ? iconWidth * 3 : iconWidth;
                int showHeight = isShowBigIcon ? iconHeight * 3 : iconHeight;
                TextView text = new TextView(mContainer.getContext());
                text.setBackgroundResource(R.drawable.stock_detail_news_icon);
                text.setOnClickListener(this);
                text.setTag(eventsDataModel);
                RelativeLayout.LayoutParams relp = new RelativeLayout.LayoutParams(showWidth, showHeight);
                if (isShowBigIcon) {
                    text.setText(eventsDataModel.eventTitle);
                }
                //计算上下边距
                int itemRightMargin = (int) (itemWidth * k - (iconWidth / 2));
                int itemTopMarigin = (int) ((axisMaximum - model.close) * itemHeight - iconHeight / 2);

                relp.setMargins(itemRightMargin, itemTopMarigin, 0, 0);
                eventContainer.addView(text, relp);
            }
            viewList.add(eventContainer);
        }
        return viewList;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.stock_detail_news_detail) {
            mStockNewDetail.setVisibility(View.GONE);
        } else if (v instanceof TextView) {
            StockEventDataModel eventsDataModel = (StockEventDataModel) v.getTag();
            mStockNewDetail.setVisibility(View.VISIBLE);
            TextView newsTv = (TextView) mStockNewDetail.findViewById(R.id.stock_detail_news_detail_title);
            TextView descTv = (TextView) mStockNewDetail.findViewById(R.id.stock_detail_news_detail_desc);
            newsTv.setText(eventsDataModel.eventTitle);
            descTv.setText(eventsDataModel.eventDesc);
        }
    }

    private void initBarChart(LineChart lineChart) {
        lineChart.setDrawBorders(false); // 是否在折线图上添加边框
        lineChart.setDescription("");// 隐藏描述
//        lineChart.setDescriptionPosition(550, 60);//设置表格描述
//        lineChart.setDescriptionColor(Color.BLACK);//设置颜色
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        lineChart.setNoDataTextDescription("You need to provide data for the chart.");
        lineChart.setDrawGridBackground(true); // 是否显示表格颜色
//        lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度
        lineChart.setGridBackgroundColor(Color.WHITE);
        lineChart.setTouchEnabled(true); // 设置是否可以触摸
        lineChart.setDragEnabled(true);// 是否可以拖拽
        lineChart.setScaleEnabled(true);// 是否可以缩放
        lineChart.setPinchZoom(true);//X、Y轴同时缩放
        lineChart.getAxisRight().setEnabled(false); // 隐藏右边 的坐标轴
//        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//设置横坐标在底部
        lineChart.getXAxis().setGridColor(Color.TRANSPARENT);//去掉网格中竖线的显示
        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);//
        lineChart.setBackgroundColor(Color.WHITE);// 设置背景

        Legend lineChartLegend = lineChart.getLegend();
        lineChartLegend.setEnabled(false);

    }

    private void initBarChartXY(LineChart lineChart, List<StockDateDataModel> dataList) {
        /**
         * 设置X轴
         * */
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(true);//显示X轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//X轴位置
//        xAxis.setDrawGridLines(true);//设置x轴上每个点对应的线
        xAxis.setSpaceBetweenLabels(2);
        xAxis.setDrawGridLines(false);

        /**
         *
         * 设置左侧Y轴
         * */
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//        leftAxis.setValueFormatter();//自定义Y轴数据格式
        leftAxis.setStartAtZero(false);//设置Y轴的数据不是从0开始
        leftAxis.setDrawTopYLabelEntry(true);
        leftAxis.setDrawGridLines(false);
    }

    private void bindChartData(LineChart mChart, List<StockDateDataModel> dataList) {
        //设置X轴的标签，此处只是简单的数字
//        String[] xData = new String[dataList.size()];
//        for (int i = 0; i < dataList.size(); i++) {
//            StockDateDataModel model = dataList.get(i);
//            xData[i] = String.valueOf(model.closePrice);
//        }

        ArrayList<String> xValues = new ArrayList<String>();
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        for (int i = 0; i < dataList.size(); i++) {
            StockDateDataModel model = dataList.get(i);
            Calendar calendar = DateUtil.dateStr2calendar(model.date, DateUtil.SIMPLEFORMATTYPESTRING7);
            String dataStr = DateUtil.calendar2Time(calendar, DateUtil.SIMPLEFORMATTYPESTRING21);
            xValues.add(dataStr);
            // y轴的数据
            yValues.add(new Entry(model.close, i));
        }

        // y轴的数据集合
        LineDataSet lineDataSet = new LineDataSet(yValues, "" /* 显示在比例图上 */);
        // 用y轴的集合来设置参数
        lineDataSet.setLineWidth(2.0f); // 线宽
        lineDataSet.setCircleSize(0f);// 显示的圆形大小
        lineDataSet.setColor(Color.parseColor("#1F91E5"));// 显示颜色
        lineDataSet.setCircleColor(Color.TRANSPARENT);// 圆形的颜色
        lineDataSet.setHighLightColor(Color.TRANSPARENT); // 点击后高亮的线的颜色
        lineDataSet.setDrawValues(false);//隐藏折线图每个数据点的值
        List<ILineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(lineDataSet); // add the datasets
        lineDataSet.setDrawCircles(false);//图表上的数据点是否用小圆圈表示
        lineDataSet.setDrawCubic(true);//允许设置平滑曲线
//        lineDataSet.setCubicIntensity(2.0f);//设置折线的平滑度
        lineDataSet.setDrawFilled(false);//是否填充折线下方
        lineDataSet.setFillColor(Color.rgb(0, 255, 255));//折线图下方填充颜色设置
        LineData lineData = new LineData(xValues, lineDataSets);
        mChart.setData(lineData);
    }

}
