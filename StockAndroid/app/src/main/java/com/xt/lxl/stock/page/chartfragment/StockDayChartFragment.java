package com.xt.lxl.stock.page.chartfragment;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.BarLineChartTouchListener;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.xt.lxl.stock.R;
import com.xt.lxl.stock.model.model.StockDateDataModel;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.model.reponse.StockGetDateDataResponse;
import com.xt.lxl.stock.sender.StockSender;
import com.xt.lxl.stock.util.FormatUtil;
import com.xt.lxl.stock.util.StockUtil;
import com.xt.lxl.stock.util.VolFormatter;
import com.xt.lxl.stock.widget.stockchart.bean.DayViewModel;
import com.xt.lxl.stock.widget.stockchart.mychart.CoupleChartGestureListener;
import com.xt.lxl.stock.widget.view.StockTextView;

import java.text.DecimalFormat;
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
    StockTextView max5Text, max10Text, max30Text;
    private BarLineChartTouchListener mChartTouchListener;
    private CoupleChartGestureListener coupleChartGestureListener;
    Handler mHandler = new Handler();

    BarData mBarData;
    CombinedData mCombinedData;
    ArrayList<String> xVals = new ArrayList<>();
    ArrayList<BarEntry> barEntries = new ArrayList<>();
    ArrayList<CandleEntry> candleEntries = new ArrayList<>();
    ArrayList<Entry> line5Entries = new ArrayList<>();//5日均线
    ArrayList<Entry> line10Entries = new ArrayList<>();//10日均线
    ArrayList<Entry> line30Entries = new ArrayList<>();//30日均线

    private Handler handler = new Handler();

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
        max5Text = (StockTextView) view.findViewById(R.id.max5_text);
        max10Text = (StockTextView) view.findViewById(R.id.max10_text);
        max30Text = (StockTextView) view.findViewById(R.id.max30_text);
        barChart.setVisibility(View.INVISIBLE);
        combinedchart.setVisibility(View.INVISIBLE);
        initChart();
    }

    @Override
    public void refreshAllData(StockViewModel stockViewModel) {
        if (mBarData == null) {
            sendServiceGetDayDataService(stockViewModel.getRequestStockCode());
        }
    }

    private void sendServiceGetDayDataService(final String stockCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StockGetDateDataResponse dataResponses = StockSender.getInstance().requestDateData(stockCode, StockGetDateDataResponse.TYPE_DAY);
                final DayViewModel dayViewModel = calculationData(dataResponses);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        bindAverageDate(dayViewModel);
                        bindDate(dayViewModel);
                    }
                });
            }
        }).start();
    }

    private void bindAverageDate(DayViewModel dayViewModel) {
        List<StockDateDataModel> dateDataList = dayViewModel.dateDataList;
        int size = dateDataList.size();
        float max5 = getSum(dateDataList, size - 5, size - 1) / 5;
        float max10 = getSum(dateDataList, size - 10, size - 1) / 10;
        float max30 = getSum(dateDataList, size - 30, size - 1) / 30;
        max5Text.setText("MAX5 " + StockUtil.roundedFor(max5, 2));
        max10Text.setText("MAX10 " + StockUtil.roundedFor(max10, 2));
        max30Text.setText("MAX30 " + StockUtil.roundedFor(max30, 2));

    }

    //拆成复用和不复用的两种
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
        xVals.clear();
        barEntries.clear();
        candleEntries.clear();
        line5Entries.clear();
        line10Entries.clear();
        line30Entries.clear();
        axisLeftBar.setValueFormatter(new VolFormatter((int) Math.pow(10, u)));
        for (int i = 0, j = 0; i < dateDataList.size(); i++, j++) {
            StockDateDataModel stockDateData = dateDataList.get(i);
            xVals.add(stockDateData.date);
            boolean showRed = stockDateData.close > stockDateData.open;
            barEntries.add(new BarEntry(stockDateData.volume, i, showRed));
            candleEntries.add(new CandleEntry(i, stockDateData.high, stockDateData.low, stockDateData.open, stockDateData.close, showRed));
            if (i >= 4) {
                line5Entries.add(new Entry(getSum(dateDataList, i - 4, i) / 5, i));
            }
            if (i >= 9) {
                line10Entries.add(new Entry(getSum(dateDataList, i - 9, i) / 10, i));
            }
            if (i >= 29) {
                line30Entries.add(new Entry(getSum(dateDataList, i - 29, i) / 30, i));
            }
        }

        if (mBarData == null) {
            //成交量的bar
            BarDataSet barDataSet = new BarDataSet(barEntries, "成交量");
            barDataSet.setBarSpacePercent(30); //bar空隙
            barDataSet.setHighlightEnabled(true);
            barDataSet.setHighLightAlpha(255);
            barDataSet.setHighLightColor(Color.WHITE);
            barDataSet.setDrawValues(false);
            barDataSet.setColors(new int[]{Color.RED, Color.parseColor("#41CB47")});
            barDataSet.setDrawUpAndDown(true);
            mBarData = new BarData(xVals, barDataSet);
            barChart.setData(mBarData);
            barChart.moveViewToX(dateDataList.size() - 1);
            barChart.invalidate();
        } else {
//            mBarData.setXVals(xVals);
            mBarData.notifyDataChanged();
            return;
        }

        final ViewPortHandler viewPortHandlerBar = barChart.getViewPortHandler();
        viewPortHandlerBar.setMaximumScaleX(StockUtil.culcMaxscale(xVals.size()));
        Matrix touchmatrix = viewPortHandlerBar.getMatrixTouch();
        final float xscale = 4;
        touchmatrix.postScale(xscale, 1f);

        /******此处修复如果显示的点的个数达不到MA均线的位置所有的点都从0开始计算最小值的问题******************************/

        if (mCombinedData == null) {
            mCombinedData = new CombinedData(xVals);

            ArrayList<ILineDataSet> sets = new ArrayList<>();
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
            //蜡烛图
            CandleDataSet candleDataSet = new CandleDataSet(candleEntries, "KLine");
            candleDataSet.setDrawHorizontalHighlightIndicator(false);
            candleDataSet.setHighlightEnabled(true);
            candleDataSet.setHighLightColor(Color.WHITE);
            candleDataSet.setValueTextSize(10f);
            candleDataSet.setDrawValues(false);
            candleDataSet.setColors(new int[]{Color.RED, Color.parseColor("#41CB47")});
            candleDataSet.setDrawUpAndDown(true);
            candleDataSet.setShadowWidth(1f);
            candleDataSet.setShadowColorSameAsCandle(true);
            candleDataSet.setIncreasingPaintStyle(Paint.Style.FILL);
            candleDataSet.setIncreasingColor(Color.RED);
            candleDataSet.setDecreasingColor(Color.parseColor("#41CB47"));
            candleDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            CandleData candleData = new CandleData(xVals, candleDataSet);
            mCombinedData.setData(candleData);

            //均线图
            LineData lineData = new LineData(xVals, sets);
            mCombinedData.setData(lineData);
            mCombinedData.setDrawValues(false);
            combinedchart.setData(mCombinedData);
        } else {
            mCombinedData.notifyDataChanged();
        }
        combinedchart.moveViewToX(dateDataList.size() - 1);
        combinedchart.invalidate();

        final ViewPortHandler viewPortHandlerCombin = combinedchart.getViewPortHandler();
        viewPortHandlerCombin.setMaximumScaleX(StockUtil.culcMaxscale(xVals.size()));
        Matrix matrixCombin = viewPortHandlerCombin.getMatrixTouch();
        final float xscaleCombin = 4;
        matrixCombin.postScale(xscaleCombin, 1f);
        setOffset();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                barChart.setVisibility(View.VISIBLE);
                combinedchart.setVisibility(View.VISIBLE);

                barChart.setAutoScaleMinMaxEnabled(true);
                combinedchart.setAutoScaleMinMaxEnabled(true);

                combinedchart.notifyDataSetChanged();
                barChart.notifyDataSetChanged();

                combinedchart.invalidate();
                barChart.invalidate();
            }
        }, 300);
    }

    public static DayViewModel calculationData(StockGetDateDataResponse dataResponses) {
        DayViewModel dayViewModel = new DayViewModel();
        if (dataResponses.dateDataList.size() == 0) {
            return dayViewModel;
        }
        dayViewModel.dateDataList = dataResponses.dateDataList;
        int size = dayViewModel.dateDataList.size();
        if (size > 260) {
            dayViewModel.dateDataList = dayViewModel.dateDataList.subList(size - 260, size);
        }

        for (StockDateDataModel dateDataModel : dayViewModel.dateDataList) {
            if (dateDataModel.date.contains(" ")) {
                dateDataModel.date = dateDataModel.date.split(" ")[0];
            }
        }
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
            lineDataSetMa.setColor(Color.parseColor("#FEB911"));
        } else if (ma == 10) {
            lineDataSetMa.setColor(Color.parseColor("#60CFFF"));
        } else {
            lineDataSetMa.setColor(Color.parseColor("#F184F5"));
        }
        lineDataSetMa.setLineWidth(1f);
        lineDataSetMa.setDrawCircles(false);
        lineDataSetMa.setAxisDependency(YAxis.AxisDependency.LEFT);
        return lineDataSetMa;
    }

    private float getSum(List<StockDateDataModel> dateDataList, Integer a, Integer b) {
        float num = 0;
        if (a < 0) {
            return 0;
        }
        for (int i = a; i <= b; i++) {
            num += dateDataList.get(i).close;
        }
        return num;
    }

    private void initChart() {
        barChart.setDrawBorders(false);
        barChart.setBorderWidth(1);
        barChart.setBorderColor(getResources().getColor(R.color.minute_grayLine));
        barChart.setDescription("");
        barChart.setDragEnabled(true);
        barChart.setScaleYEnabled(false);
        barChart.setMaxVisibleValueCount(30);
        barChart.setDoubleTapToZoomEnabled(false);

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
        xAxisBar.setValueFormatter(new XAxisValueFormatter() {
            @Override
            public String getXValue(String original, int index, ViewPortHandler viewPortHandler) {
                if (original.length() == 10) {
                    return original.substring(0, 7);
                }
                return original;
            }
        });

        axisLeftBar = barChart.getAxisLeft();
        axisLeftBar.setAxisMinValue(0);
        axisLeftBar.setDrawGridLines(false);
        axisLeftBar.setDrawAxisLine(false);
        axisLeftBar.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        axisLeftBar.setDrawLabels(true);
        axisLeftBar.setSpaceTop(0);
        axisLeftBar.setShowOnlyMinMax(true);
        axisLeftBar.setAxisMinValue(0);
        axisLeftBar.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                DecimalFormat mFormat = new DecimalFormat("#0.00%");
                return mFormat.format(value);
            }
        });
        axisRightBar = barChart.getAxisRight();
        axisRightBar.setDrawLabels(false);
        axisRightBar.setDrawGridLines(false);
        axisRightBar.setDrawAxisLine(false);

        /****************************************************************/
        combinedchart.setPadding(0, 0, 0, 0);
        combinedchart.setDoubleTapToZoomEnabled(false);
        combinedchart.setDrawBorders(false);
        combinedchart.setBorderWidth(1);
        combinedchart.setDescription("");
        combinedchart.setDragEnabled(true);
        combinedchart.setScaleYEnabled(false);
        combinedchart.setMaxVisibleValueCount(30);

        Legend combinedchartLegend = combinedchart.getLegend();
        combinedchartLegend.setEnabled(false);
        //bar x y轴
        xAxisK = combinedchart.getXAxis();
        xAxisK.setDrawLabels(false);
        xAxisK.setDrawGridLines(false);
        xAxisK.setDrawAxisLine(false);
        xAxisK.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        xAxisK.setPosition(XAxis.XAxisPosition.BOTTOM);

        axisLeftK = combinedchart.getAxisLeft();
        axisLeftK.setLabelCount(3, true);
        axisLeftK.setDrawLabels(true);
        axisLeftK.setDrawGridLines(true);
        axisLeftK.setDrawAxisLine(false);
        axisLeftK.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        axisLeftK.setGridColor(getResources().getColor(R.color.day_grid_line));
        axisLeftK.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeftK.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                return String.valueOf(StockUtil.roundedFor(value, 2));
            }
        });
        axisRightK = combinedchart.getAxisRight();
        axisRightK.setDrawLabels(false);
        axisRightK.setDrawGridLines(false);
        axisRightK.setDrawAxisLine(false);
        axisRightK.setGridColor(getResources().getColor(R.color.day_grid_line));
        combinedchart.setDragDecelerationEnabled(true);
        barChart.setDragDecelerationEnabled(true);
        combinedchart.setDragDecelerationFrictionCoef(0.2f);
        barChart.setDragDecelerationFrictionCoef(0.2f);


        // 将K线控的滑动事件传递给交易量控件
        combinedchart.setOnChartGestureListener(new CoupleChartGestureListener(combinedchart, new Chart[]{barChart}));
        // 将交易量控件的滑动事件传递给K线控件
        barChart.setOnChartGestureListener(new CoupleChartGestureListener(barChart, new Chart[]{combinedchart}));
//        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
//            @Override
//            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
//                Log.e("%%%%", h.getXIndex() + "");
//                combinedchart.highlightValues(new Highlight[]{h});
//            }
//
//            @Override
//            public void onNothingSelected() {
//                combinedchart.highlightValue(null);
//            }
//        });
//        combinedchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
//            @Override
//            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
//
//                barChart.highlightValues(new Highlight[]{h});
//            }
//
//            @Override
//            public void onNothingSelected() {
//                barChart.highlightValue(null);
//            }
//        });
    }

}
