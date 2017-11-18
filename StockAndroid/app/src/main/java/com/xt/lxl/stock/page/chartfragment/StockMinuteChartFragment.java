package com.xt.lxl.stock.page.chartfragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.xt.lxl.stock.R;
import com.xt.lxl.stock.model.model.StockMinuteDataModel;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.model.reponse.StockGetMinuteDataResponse;
import com.xt.lxl.stock.sender.StockSender;
import com.xt.lxl.stock.util.DateUtil;
import com.xt.lxl.stock.widget.stockchart.bean.MinuteViewModel;
import com.xt.lxl.stock.widget.stockchart.mychart.MyXAxis;
import com.xt.lxl.stock.widget.stockchart.mychart.MyYAxis;
import com.xt.lxl.stock.widget.stockchart.view.MyBottomMarkerView;
import com.xt.lxl.stock.widget.stockchart.view.MyLeftMarkerView;
import com.xt.lxl.stock.widget.stockchart.view.MyLineChart;
import com.xt.lxl.stock.widget.stockchart.view.MyRightMarkerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/10/15.
 */
public class StockMinuteChartFragment extends StockBaseChartFragment {

    //    MyBarChart barChart;
    MyLineChart lineChart;
    private LineDataSet d1, d2;//均价，成交价
    MyXAxis xAxisLine;
    MyYAxis axisRightLine;
    MyYAxis axisLeftLine;
    BarDataSet barDataSet;
    //    MyXAxis xAxisBar;
//    MyYAxis axisLeftBar;
//    MyYAxis axisRightBar;
    SparseArray<String> stringSparseArray;
    private MinuteViewModel minuteViewModel = new MinuteViewModel();
    private LineData cd;

    Handler mHandler = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stock_detail_chart_minute_layout, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lineChart = (MyLineChart) view.findViewById(R.id.kline_minute_chart);
        lineChart.setMaxVisibleValueCount(12);
        initChart();
        stringSparseArray = setXLabels();
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
//                barChart.highlightValue(new Highlight(h.getXIndex(), 0));
            }

            @Override
            public void onNothingSelected() {
//                barChart.highlightValue(null);
            }
        });
    }

    @Override
    public void refreshAllData(StockViewModel stockViewModel) {
        sendServiceGetMinuteDataResponse(stockViewModel);
    }

    private void initChart() {
        lineChart.setScaleEnabled(false);
        lineChart.setDrawBorders(true);
        lineChart.setBorderWidth(1);
        lineChart.setBorderColor(getResources().getColor(R.color.minute_grayLine));
        lineChart.setDescription("");
        Legend lineChartLegend = lineChart.getLegend();
        lineChartLegend.setEnabled(false);

//        barChart.setScaleEnabled(false);
//        barChart.setDrawBorders(true);
//        barChart.setBorderWidth(1);
//        barChart.setBorderColor(getResources().getColor(R.color.minute_grayLine));
//        barChart.setDescription("");


//        Legend barChartLegend = barChart.getLegend();
//        barChartLegend.setEnabled(false);
        //x轴
        xAxisLine = lineChart.getXAxis();
        xAxisLine.setDrawLabels(true);
        xAxisLine.setPosition(XAxis.XAxisPosition.BOTTOM);
        // xAxisLine.setLabelsToSkip(59);


        //左边y
        axisLeftLine = lineChart.getAxisLeft();
        /*折线图y轴左没有basevalue，调用系统的*/
        axisLeftLine.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeftLine.setStartAtZero(false);//设置Y轴的数据不是从0开始
        axisLeftLine.setLabelCount(5, true);
        axisLeftLine.setDrawLabels(true);
        axisLeftLine.setDrawGridLines(false);
        /*轴不显示 避免和border冲突*/
        axisLeftLine.setDrawAxisLine(false);


        //右边y
        axisRightLine = lineChart.getAxisRight();
        axisRightLine.setLabelCount(2, true);
        axisRightLine.setDrawLabels(true);
        axisRightLine.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                DecimalFormat mFormat = new DecimalFormat("#0.00%");
                return mFormat.format(value);
            }
        });

        axisRightLine.setStartAtZero(false);
        axisRightLine.setDrawGridLines(false);
        axisRightLine.setDrawAxisLine(false);
        //背景线
        xAxisLine.setGridColor(getResources().getColor(R.color.minute_grayLine));
        xAxisLine.enableGridDashedLine(10f, 5f, 0f);
        xAxisLine.setAxisLineColor(getResources().getColor(R.color.minute_grayLine));
        xAxisLine.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        axisLeftLine.setGridColor(getResources().getColor(R.color.minute_grayLine));
        axisLeftLine.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        axisRightLine.setAxisLineColor(getResources().getColor(R.color.minute_grayLine));
        axisRightLine.setTextColor(getResources().getColor(R.color.minute_zhoutv));

        //y轴样式
        this.axisLeftLine.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                DecimalFormat mFormat = new DecimalFormat("#0.00");
                return mFormat.format(value);
            }
        });

    }

    private SparseArray<String> setXLabels() {
        SparseArray<String> xLabels = new SparseArray<>();
        xLabels.put(0, "09:30");
        xLabels.put(60, "10:30");
        xLabels.put(121, "11:30/13:00");
        xLabels.put(182, "14:00");
        xLabels.put(241, "15:00");
        return xLabels;
    }

    private void sendServiceGetMinuteDataResponse(final StockViewModel stockViewModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String currentDate = DateUtil.getCurrentDate();
                StockGetMinuteDataResponse minuteDataResponse = StockSender.getInstance().requestMinuteData(stockViewModel.stockCode, currentDate);
//                StockGetMinuteDataResponse minuteDataResponse = DataSource.getMinuteDataResponses();
                minuteViewModel.initAllModel(minuteDataResponse.stockMinuteDataModels, stockViewModel.stockBasePirce);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshMinute();
                    }
                });
            }
        }).start();
    }

    private void refreshMinute() {
        setMarkerView(minuteViewModel.minuteList);
        setShowLabels(stringSparseArray);
        if (minuteViewModel.minuteList.size() == 0) {
            lineChart.setNoDataText("暂无数据");
            return;
        }
        //设置y左右两轴最大最小值
        axisLeftLine.setAxisMinValue(minuteViewModel.minPrice);
        axisLeftLine.setAxisMaxValue(minuteViewModel.maxPrice);
        axisRightLine.setAxisMinValue(minuteViewModel.maxFallChange);
        axisRightLine.setAxisMaxValue(minuteViewModel.maxRiseChange);
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        if (cd == null) {
            cd = new LineData(getMinutesCount(), sets);
            lineChart.setData(cd);
        }
        //基准线
        LimitLine ll = new LimitLine(0);
        ll.setLineWidth(1f);
        ll.setLineColor(getResources().getColor(R.color.minute_jizhun));
        ll.enableDashedLine(10f, 10f, 0f);
        ll.setLineWidth(1);
        axisRightLine.addLimitLine(ll);
        axisRightLine.setBaseValue(0);

        ArrayList<Entry> lineCJEntries = new ArrayList<>();
        ArrayList<Entry> lineJJEntries = new ArrayList<>();
        ArrayList<String> dateList = new ArrayList<>();
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();
        Log.e("##", Integer.toString(xVals.size()));
        for (int i = 0, j = 0; i < minuteViewModel.minuteList.size(); i++, j++) {
           /* //避免数据重复，skip也能正常显示
            if (mData.getDatas().get(i).time.equals("13:30")) {
                continue;
            }*/
            StockMinuteDataModel stockMinuteData = minuteViewModel.minuteList.get(j);
            if (stockMinuteData == null || stockMinuteData.state == 0) {
                lineCJEntries.add(new Entry(Float.NaN, i));
                lineJJEntries.add(new Entry(Float.NaN, i));
                barEntries.add(new BarEntry(Float.NaN, i));
                continue;
            }
            if (!TextUtils.isEmpty(stringSparseArray.get(i)) &&
                    stringSparseArray.get(i).contains("/")) {
                i++;
            }
            lineCJEntries.add(new Entry(((float) stockMinuteData.price), i));//成交价格
            lineJJEntries.add(new Entry(stockMinuteData.pjprice, i));//平均价格
            barEntries.add(new BarEntry(stockMinuteData.volume, i));//成交数量
            // dateList.add(mData.getDatas().get(i).time);
        }
        d1 = new LineDataSet(lineCJEntries, "成交价");
        d2 = new LineDataSet(lineJJEntries, "均价");
        d1.setDrawValues(false);
        d2.setDrawValues(false);
        barDataSet = new BarDataSet(barEntries, "成交量");

        d1.setCircleRadius(0);
        d2.setCircleRadius(0);
        d1.setColor(getResources().getColor(R.color.minute_blue));
        d2.setColor(getResources().getColor(R.color.minute_yellow));
        d1.setHighLightColor(Color.WHITE);
        d2.setHighlightEnabled(false);
        d1.setDrawFilled(true);


        barDataSet.setBarSpacePercent(50); //bar空隙
        barDataSet.setHighLightColor(Color.WHITE);
        barDataSet.setHighLightAlpha(255);
        barDataSet.setDrawValues(false);
        barDataSet.setHighlightEnabled(true);
        barDataSet.setColor(Color.RED);
        List<Integer> list = new ArrayList<>();
        list.add(Color.RED);
        list.add(Color.GREEN);
        barDataSet.setColors(list);
        //谁为基准
        d1.setAxisDependency(YAxis.AxisDependency.LEFT);
        // d2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        sets.add(d1);
        sets.add(d2);
//        BarData barData = new BarData(getMinutesCount(), barDataSet);
//        barChart.setData(barData);
        setOffset();
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();//刷新图
//        barChart.invalidate();
    }

    private void setMarkerView(List<StockMinuteDataModel> minuteDateList) {
        MyLeftMarkerView leftMarkerView = new MyLeftMarkerView(getContext(), R.layout.mymarkerview);
        MyRightMarkerView rightMarkerView = new MyRightMarkerView(getContext(), R.layout.mymarkerview);
        MyBottomMarkerView bottomMarkerView = new MyBottomMarkerView(getContext(), R.layout.mymarkerview);
        lineChart.setMarker(leftMarkerView, rightMarkerView, bottomMarkerView, minuteDateList);
//        barChart.setMarker(leftMarkerView, rightMarkerView, bottomMarkerView, mData);
    }


    /*设置量表对齐*/
    private void setOffset() {
        float lineLeft = lineChart.getViewPortHandler().offsetLeft();
//        float barLeft = barChart.getViewPortHandler().offsetLeft();
        float lineRight = lineChart.getViewPortHandler().offsetRight();
//        float barRight = barChart.getViewPortHandler().offsetRight();
//        float barBottom = barChart.getViewPortHandler().offsetBottom();
        float offsetLeft, offsetRight;
        float transLeft = 0, transRight = 0;
 /*注：setExtraLeft...函数是针对图表相对位置计算，比如A表offLeftA=20dp,B表offLeftB=30dp,则A.setExtraLeftOffset(10),并不是30，还有注意单位转换*/
//        if (barLeft < lineLeft) {
//            //offsetLeft = Utils.convertPixelsToDp(lineLeft - barLeft);
//            // barChart.setExtraLeftOffset(offsetLeft);
//            transLeft = lineLeft;
//        } else {
//            offsetLeft = Utils.convertPixelsToDp(barLeft - lineLeft);
//            lineChart.setExtraLeftOffset(offsetLeft);
//            transLeft = barLeft;
//        }
  /*注：setExtraRight...函数是针对图表绝对位置计算，比如A表offRightA=20dp,B表offRightB=30dp,则A.setExtraLeftOffset(30),并不是10，还有注意单位转换*/
//        if (barRight < lineRight) {
//            //offsetRight = Utils.convertPixelsToDp(lineRight);
//            //barChart.setExtraRightOffset(offsetRight);
//            transRight = lineRight;
//        } else {
//            offsetRight = Utils.convertPixelsToDp(barRight);
//            lineChart.setExtraRightOffset(offsetRight);
//            transRight = barRight;
//        }
//        barChart.setViewPortOffsets(transLeft, 5, transRight, barBottom);
    }

    public String[] getMinutesCount() {
        return new String[242];
    }

    public void setShowLabels(SparseArray<String> labels) {
        xAxisLine.setXLabels(labels);
//        xAxisBar.setXLabels(labels);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
