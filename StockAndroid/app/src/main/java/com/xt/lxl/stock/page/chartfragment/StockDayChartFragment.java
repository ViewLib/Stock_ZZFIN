package com.xt.lxl.stock.page.chartfragment;

import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.BarLineChartTouchListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.xt.lxl.stock.R;
import com.xt.lxl.stock.model.model.StockDateDataModel;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.model.reponse.StockGetDateDataResponse;
import com.xt.lxl.stock.model.reponse.StockGetMinuteDataResponse;
import com.xt.lxl.stock.sender.StockSender;
import com.xt.lxl.stock.util.DataSource;
import com.xt.lxl.stock.util.StockUtil;
import com.xt.lxl.stock.util.VolFormatter;
import com.xt.lxl.stock.widget.stockchart.bean.DayViewModel;
import com.xt.lxl.stock.widget.stockchart.mychart.CoupleChartGestureListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/10/15.
 */
public class StockDayChartFragment extends StockBaseChartFragment {

    CombinedChart combinedchart;
    BarChart barChart;
    XAxis xAxisBar, xAxisK;
    YAxis axisLeftBar, axisLeftK;
    YAxis axisRightBar, axisRightK;
    BarDataSet barDataSet;
    private BarLineChartTouchListener mChartTouchListener;
    private CoupleChartGestureListener coupleChartGestureListener;
    float sum = 0;
    Handler mHandler = new Handler();


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            barChart.setAutoScaleMinMaxEnabled(true);
            combinedchart.setAutoScaleMinMaxEnabled(true);

            combinedchart.notifyDataSetChanged();
            barChart.notifyDataSetChanged();

            combinedchart.invalidate();
            barChart.invalidate();

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stock_detail_chart_day_layout, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        combinedchart = (CombinedChart) view.findViewById(R.id.kline_day_chart);
        barChart = (BarChart) view.findViewById(R.id.kline_day_bar);
        initChart();
    }

    @Override
    public void refreshAllData(StockViewModel stockViewModel) {
        sendServiceGetDayDataService();
    }

    private void sendServiceGetDayDataService() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StockGetDateDataResponse dataResponses = DataSource.getDataResponses(StockGetDateDataResponse.TYPE_DAY);
                final DayViewModel dayViewModel = calculationData(dataResponses);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        bindDate(dayViewModel);
                    }
                });
            }
        }).start();
    }

    private void bindDate(DayViewModel dayViewModel) {
        List<StockDateDataModel> dateDataList = dayViewModel.dateDataList;
        int size = dateDataList.size();   //点的个数
        // axisLeftBar.setAxisMaxValue(mData.getVolmax());
        String unit = StockUtil.getVolUnit(dayViewModel.maxVolum);
        int u = 1;
        if ("万手".equals(unit)) {
            u = 4;
        } else if ("亿手".equals(unit)) {
            u = 8;
        }
        axisLeftBar.setValueFormatter(new VolFormatter((int) Math.pow(10, u)));
        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<CandleEntry> candleEntries = new ArrayList<>();
        ArrayList<Entry> line5Entries = new ArrayList<>();//5日均线
        ArrayList<Entry> line10Entries = new ArrayList<>();//10日均线
        ArrayList<Entry> line30Entries = new ArrayList<>();//30日均线
        for (int i = 0, j = 0; i < dateDataList.size(); i++, j++) {
            StockDateDataModel stockDateData = dateDataList.get(i);
            xVals.add(stockDateData.dateStr);
            barEntries.add(new BarEntry(stockDateData.volume, i));
            candleEntries.add(new CandleEntry(i, stockDateData.maxPrice / 100f, stockDateData.minPrice / 100f, stockDateData.openPrice / 100f, stockDateData.closePrice / 100f));
            if (i >= 4) {
                sum = 0;
                line5Entries.add(new Entry(getSum(dateDataList, i - 4, i) / 5, i));
            }
            if (i >= 9) {
                sum = 0;
                line10Entries.add(new Entry(getSum(dateDataList, i - 9, i) / 10, i));
            }
            if (i >= 29) {
                sum = 0;
                line30Entries.add(new Entry(getSum(dateDataList, i - 29, i) / 30, i));
            }

        }
        barDataSet = new BarDataSet(barEntries, "成交量");
        barDataSet.setBarSpacePercent(50); //bar空隙
        barDataSet.setHighlightEnabled(true);
        barDataSet.setHighLightAlpha(255);
        barDataSet.setHighLightColor(Color.WHITE);
        barDataSet.setDrawValues(false);
        barDataSet.setColor(Color.RED);
        BarData barData = new BarData(xVals, barDataSet);
        barChart.setData(barData);
        final ViewPortHandler viewPortHandlerBar = barChart.getViewPortHandler();
        viewPortHandlerBar.setMaximumScaleX(StockUtil.culcMaxscale(xVals.size()));
        Matrix touchmatrix = viewPortHandlerBar.getMatrixTouch();
        final float xscale = 3;
        touchmatrix.postScale(xscale, 1f);


        CandleDataSet candleDataSet = new CandleDataSet(candleEntries, "KLine");
        candleDataSet.setDrawHorizontalHighlightIndicator(false);
        candleDataSet.setHighlightEnabled(true);
        candleDataSet.setHighLightColor(Color.WHITE);
        candleDataSet.setValueTextSize(10f);
        candleDataSet.setDrawValues(false);
        candleDataSet.setColor(Color.RED);
        candleDataSet.setShadowWidth(1f);
        candleDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        CandleData candleData = new CandleData(xVals, candleDataSet);


        ArrayList<ILineDataSet> sets = new ArrayList<>();

        /******此处修复如果显示的点的个数达不到MA均线的位置所有的点都从0开始计算最小值的问题******************************/
        if (size >= 30) {
            sets.add(setMaLine(5, xVals, line5Entries));
            sets.add(setMaLine(10, xVals, line10Entries));
            sets.add(setMaLine(30, xVals, line30Entries));
        } else if (size >= 10 && size < 30) {
            sets.add(setMaLine(5, xVals, line5Entries));
            sets.add(setMaLine(10, xVals, line10Entries));
        } else if (size >= 5 && size < 10) {
            sets.add(setMaLine(5, xVals, line5Entries));
        }


        CombinedData combinedData = new CombinedData(xVals);
        LineData lineData = new LineData(xVals, sets);
        combinedData.setData(candleData);
        combinedData.setData(lineData);
        combinedchart.setData(combinedData);
        combinedchart.moveViewToX(dateDataList.size() - 1);
        final ViewPortHandler viewPortHandlerCombin = combinedchart.getViewPortHandler();
        viewPortHandlerCombin.setMaximumScaleX(StockUtil.culcMaxscale(xVals.size()));
        Matrix matrixCombin = viewPortHandlerCombin.getMatrixTouch();
        final float xscaleCombin = 3;
        matrixCombin.postScale(xscaleCombin, 1f);

        combinedchart.moveViewToX(dateDataList.size() - 1);
        barChart.moveViewToX(dateDataList.size() - 1);
        setOffset();
        handler.sendEmptyMessageDelayed(0, 300);

    }

    private DayViewModel calculationData(StockGetDateDataResponse dataResponses) {
        DayViewModel dayViewModel = new DayViewModel();
        if (dataResponses.dateDataList.size() == 0) {
            return dayViewModel;
        }
        dayViewModel.dateDataList = dataResponses.dateDataList;
        dayViewModel.maxVolum = dayViewModel.dateDataList.get(0).volume;
        for (int i = 0; i < dayViewModel.dateDataList.size(); i++) {
            StockDateDataModel stockDateData = dayViewModel.dateDataList.get(i);
            dayViewModel.maxVolum = stockDateData.volume > dayViewModel.maxVolum ? stockDateData.volume : dayViewModel.maxVolum;
        }
        return dayViewModel;
    }

    /*设置量表对齐*/
    private void setOffset() {
        float lineLeft = combinedchart.getViewPortHandler().offsetLeft();
        float barLeft = barChart.getViewPortHandler().offsetLeft();
        float lineRight = combinedchart.getViewPortHandler().offsetRight();
        float barRight = barChart.getViewPortHandler().offsetRight();
        float barBottom = barChart.getViewPortHandler().offsetBottom();
        float offsetLeft, offsetRight;
        float transLeft = 0, transRight = 0;
 /*注：setExtraLeft...函数是针对图表相对位置计算，比如A表offLeftA=20dp,B表offLeftB=30dp,则A.setExtraLeftOffset(10),并不是30，还有注意单位转换*/
        if (barLeft < lineLeft) {
           /* offsetLeft = Utils.convertPixelsToDp(lineLeft - barLeft);
            barChart.setExtraLeftOffset(offsetLeft);*/
            transLeft = lineLeft;
        } else {
            offsetLeft = Utils.convertPixelsToDp(barLeft - lineLeft);
            combinedchart.setExtraLeftOffset(offsetLeft);
            transLeft = barLeft;
        }
  /*注：setExtraRight...函数是针对图表绝对位置计算，比如A表offRightA=20dp,B表offRightB=30dp,则A.setExtraLeftOffset(30),并不是10，还有注意单位转换*/
        if (barRight < lineRight) {
          /*  offsetRight = Utils.convertPixelsToDp(lineRight);
            barChart.setExtraRightOffset(offsetRight);*/
            transRight = lineRight;
        } else {
            offsetRight = Utils.convertPixelsToDp(barRight);
            combinedchart.setExtraRightOffset(offsetRight);
            transRight = barRight;
        }
        barChart.setViewPortOffsets(transLeft, 15, transRight, barBottom);
    }

    @NonNull
    private LineDataSet setMaLine(int ma, ArrayList<String> xVals, ArrayList<Entry> lineEntries) {
        LineDataSet lineDataSetMa = new LineDataSet(lineEntries, "ma" + ma);
        if (ma == 5) {
            lineDataSetMa.setHighlightEnabled(true);
            lineDataSetMa.setDrawHorizontalHighlightIndicator(false);
            lineDataSetMa.setHighLightColor(Color.WHITE);
        } else {/*此处必须得写*/
            lineDataSetMa.setHighlightEnabled(false);
        }
        lineDataSetMa.setDrawValues(false);
        if (ma == 5) {
            lineDataSetMa.setColor(Color.GREEN);
        } else if (ma == 10) {
            lineDataSetMa.setColor(Color.GRAY);
        } else {
            lineDataSetMa.setColor(Color.YELLOW);
        }
        lineDataSetMa.setLineWidth(1f);
        lineDataSetMa.setDrawCircles(false);
        lineDataSetMa.setAxisDependency(YAxis.AxisDependency.LEFT);
        return lineDataSetMa;
    }

    private float getSum(List<StockDateDataModel> dateDataList, Integer a, Integer b) {
        for (int i = a; i <= b; i++) {
            sum += dateDataList.get(i).closePrice / 100;
        }
        return sum;
    }

    private void initChart() {
        barChart.setDrawBorders(true);
        barChart.setBorderWidth(1);
        barChart.setBorderColor(getResources().getColor(R.color.minute_grayLine));
        barChart.setDescription("");
        barChart.setDragEnabled(true);
        barChart.setScaleYEnabled(false);

        Legend barChartLegend = barChart.getLegend();
        barChartLegend.setEnabled(false);

        //BarYAxisFormatter  barYAxisFormatter=new BarYAxisFormatter();
        //bar x y轴
        xAxisBar = barChart.getXAxis();
        xAxisBar.setDrawLabels(true);
        xAxisBar.setDrawGridLines(false);
        xAxisBar.setDrawAxisLine(false);
        xAxisBar.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        xAxisBar.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisBar.setGridColor(getResources().getColor(R.color.minute_grayLine));

        axisLeftBar = barChart.getAxisLeft();
        axisLeftBar.setAxisMinValue(0);
        axisLeftBar.setDrawGridLines(false);
        axisLeftBar.setDrawAxisLine(false);
        axisLeftBar.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        axisLeftBar.setDrawLabels(true);
        axisLeftBar.setSpaceTop(0);
        axisLeftBar.setShowOnlyMinMax(true);
        axisRightBar = barChart.getAxisRight();
        axisRightBar.setDrawLabels(false);
        axisRightBar.setDrawGridLines(false);
        axisRightBar.setDrawAxisLine(false);
        /****************************************************************/
        combinedchart.setDrawBorders(true);
        combinedchart.setBorderWidth(1);
        combinedchart.setBorderColor(getResources().getColor(R.color.minute_grayLine));
        combinedchart.setDescription("");
        combinedchart.setDragEnabled(true);
        combinedchart.setScaleYEnabled(false);

        Legend combinedchartLegend = combinedchart.getLegend();
        combinedchartLegend.setEnabled(false);
        //bar x y轴
        xAxisK = combinedchart.getXAxis();
        xAxisK.setDrawLabels(true);
        xAxisK.setDrawGridLines(false);
        xAxisK.setDrawAxisLine(false);
        xAxisK.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        xAxisK.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisK.setGridColor(getResources().getColor(R.color.minute_grayLine));

        axisLeftK = combinedchart.getAxisLeft();
        axisLeftK.setDrawGridLines(true);
        axisLeftK.setDrawAxisLine(false);
        axisLeftK.setDrawLabels(true);
        axisLeftK.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        axisLeftK.setGridColor(getResources().getColor(R.color.minute_grayLine));
        axisLeftK.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisRightK = combinedchart.getAxisRight();
        axisRightK.setDrawLabels(false);
        axisRightK.setDrawGridLines(true);
        axisRightK.setDrawAxisLine(false);
        axisRightK.setGridColor(getResources().getColor(R.color.minute_grayLine));
        combinedchart.setDragDecelerationEnabled(true);
        barChart.setDragDecelerationEnabled(true);
        combinedchart.setDragDecelerationFrictionCoef(0.2f);
        barChart.setDragDecelerationFrictionCoef(0.2f);


        // 将K线控的滑动事件传递给交易量控件
        combinedchart.setOnChartGestureListener(new CoupleChartGestureListener(combinedchart, new Chart[]{barChart}));
        // 将交易量控件的滑动事件传递给K线控件
        barChart.setOnChartGestureListener(new CoupleChartGestureListener(barChart, new Chart[]{combinedchart}));
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Log.e("%%%%", h.getXIndex() + "");
                combinedchart.highlightValues(new Highlight[]{h});
            }

            @Override
            public void onNothingSelected() {
                combinedchart.highlightValue(null);
            }
        });
        combinedchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                barChart.highlightValues(new Highlight[]{h});
            }

            @Override
            public void onNothingSelected() {
                barChart.highlightValue(null);
            }
        });
    }

}
