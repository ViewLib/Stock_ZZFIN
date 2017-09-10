package com.xt.lxl.stock.util;

import android.content.Context;

import com.xt.lxl.stock.model.StockIndexChangeModel;
import com.xt.lxl.stock.model.StockViewModel;
import com.xt.lxl.stock.widget.helper.HotelLabelDrawable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/8/5.
 */
public class DataShowUtil {

    static DecimalFormat df = new DecimalFormat("######0.00");
    //    static String defaultColor = "#F0F0F1";
    static String defaultColor = "#00ffffff";

    public static HotelLabelDrawable[] transforDrawables(Context context, StockViewModel viewModel) {
        if (viewModel.mStockChangeD == 0) {
            HotelLabelDrawable[] drawables = new HotelLabelDrawable[1];
            drawables[0] = new HotelLabelDrawable(context);
            StockIndexChangeModel centerModel = new StockIndexChangeModel();
            if (viewModel.isSuspension) {
                centerModel.mShowText = "停牌";
            } else {
                centerModel.mShowText = viewModel.mStockChange + "%";
            }
            centerModel.mTextColor = "#5C5C5C";
            centerModel.mShowIndex = 0;
            centerModel.mBgColor = defaultColor;
            centerModel.mShowLocation = StockIndexChangeModel.SHOW_LOCATION_CENTER;
            drawables[0].setLabelModel(centerModel);
            return drawables;
        }

        HotelLabelDrawable[] drawables = new HotelLabelDrawable[2];
        drawables[0] = new HotelLabelDrawable(context);
        drawables[1] = new HotelLabelDrawable(context);
        StockIndexChangeModel leftModel = new StockIndexChangeModel();
        StockIndexChangeModel rightModel = new StockIndexChangeModel();
        if (viewModel.mStockChangeD > 0) {
            leftModel.mBgColor = defaultColor;
            rightModel.mShowText = viewModel.mStockChange + "%";
            rightModel.mShowIndex = viewModel.mStockChangeD * 10;
            rightModel.mShowIndex = rightModel.mShowIndex > 1 ? 1 : rightModel.mShowIndex;
            rightModel.mBgColor = "#FA5259";
            if (viewModel.mStockChangeD > 0.03) {
                rightModel.mTextColor = "#ffffff";
                rightModel.mShowLocation = StockIndexChangeModel.SHOW_LOCATION_CENTER;
            } else {
                rightModel.mTextColor = "#000000";
                rightModel.mShowLocation = StockIndexChangeModel.SHOW_LOCATION_RIGHT;
            }
        } else {
            rightModel.mBgColor = defaultColor;

            leftModel.mShowText = viewModel.mStockChange + "%";
            leftModel.mShowIndex = viewModel.mStockChangeD * 10;
            leftModel.mShowIndex = leftModel.mShowIndex < -1 ? -1 : leftModel.mShowIndex;
            leftModel.mBgColor = "#4CB774";
            leftModel.mTextColor = "#000000";
            if (viewModel.mStockChangeD < -0.03) {
                leftModel.mTextColor = "#ffffff";
                leftModel.mShowLocation = StockIndexChangeModel.SHOW_LOCATION_CENTER;
            } else {
                leftModel.mTextColor = "#000000";
                leftModel.mShowLocation = StockIndexChangeModel.SHOW_LOCATION_LEFT;
            }
        }
        drawables[0].setLabelModel(leftModel);
        drawables[1].setLabelModel(rightModel);
        return drawables;
    }


    public static String getDisplayChangeStr(double change) {
        StringBuilder builder = new StringBuilder();
        if (change < 0) {
            builder.append("-");
            change = change * -1.0;
        }
        builder.append(df.format(change * 100));
        builder.append("%");
        return builder.toString();
    }

    public static List<List<String>> divisionList(List<String> list, int limit) {
        List<List<String>> divisionList = new ArrayList<>();
        for (int start = 0; start < list.size(); ) {
            int end = start + limit;
            if (end > list.size()) {
                end = list.size();
            }
            divisionList.add(list.subList(start, end));
            start = end;
        }
        return divisionList;
    }

    //股票代码转化为sz300170这样的
    public static String code2MarketCode(String code) {
        if (StringUtil.emptyOrNull(code) || code.length() != 6) {
            LogUtil.LogI("输入异常code:" + code);
            return code;
        }

        if (code.startsWith("6")) {
            return "sh" + code;
        }
        return "sz" + code;
    }

    public static List<StockViewModel> resultStr2StockList(String resultStr) {
        List<StockViewModel> list = new ArrayList<>();
        if (StringUtil.emptyOrNull(resultStr)) {
            return list;
        }
        String[] split = resultStr.split(";");
        StockViewModel stockViewModel;
        for (int i = 0; i < split.length; i++) {
            try {
                stockViewModel = resultStr2Stock(split[i]);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            if (stockViewModel != null) {
                list.add(stockViewModel);
            }
        }
        return list;
    }

    public static StockViewModel resultStr2Stock(String resultOne) throws Exception {
        if (StringUtil.emptyOrNull(resultOne) || !resultOne.contains("~")) {
            return null;
        }
        int beginIndex = resultOne.indexOf("=\"");
        int endIndex = resultOne.indexOf("\"", beginIndex + 2);
        String substring = resultOne.substring(beginIndex, endIndex);
        String[] split = substring.split("~");
        StockViewModel stockViewModel = new StockViewModel();
        stockViewModel.mStockName = split[1];//股票名称
        stockViewModel.mStockCode = split[2];//股票代码
        stockViewModel.mStockPirce = split[3];//当前价格
        if ("0.00".equals(stockViewModel.mStockPirce)) {
            //当前价格为0时代表停牌，取上一次的
            stockViewModel.mStockPirce = split[4];
            stockViewModel.isSuspension = true;
        }
//        stockViewModel.mStockCode = split[4];//昨收价格
//        stockViewModel.mStockCode = split[5];//今开
//        stockViewModel.mStockCode = split[6];//成交量（手）
//        stockViewModel.mStockCode = split[7];//外盘
//        stockViewModel.mStockCode = split[8];//内盘
//        stockViewModel.mStockCode = split[9];//买一
//        stockViewModel.mStockCode = split[10];//买一量（手）
//        stockViewModel.mStockCode = split[11];//买二
//        stockViewModel.mStockCode = split[12];//买二量（手）
//        stockViewModel.mStockCode = split[13];//买三
//        stockViewModel.mStockCode = split[14];//买三量（手）
//        stockViewModel.mStockCode = split[15];//买四
//        stockViewModel.mStockCode = split[16];//买四量（手）
//        stockViewModel.mStockCode = split[17];//买五
//        stockViewModel.mStockCode = split[18];//买五量（手）
//        stockViewModel.mStockCode = split[19];//卖一
//        stockViewModel.mStockCode = split[20];//卖一量（手）
//        stockViewModel.mStockCode = split[21];//卖二
//        stockViewModel.mStockCode = split[22];//卖二量（手）
//        stockViewModel.mStockCode = split[23];//卖三
//        stockViewModel.mStockCode = split[24];//卖三量（手）
//        stockViewModel.mStockCode = split[25];//卖四
//        stockViewModel.mStockCode = split[26];//卖四量（手）
//        stockViewModel.mStockCode = split[27];//卖五
//        stockViewModel.mStockCode = split[28];//卖五量（手）
//        stockViewModel.mStockCode = split[29];//最近逐笔成交
//        stockViewModel.mStockCode = split[30];//时间
//        stockViewModel.mStockCode = split[31];//涨跌
        stockViewModel.mStockChange = split[32];//涨跌%
//        stockViewModel.mStockCode = split[33];//最高
//        stockViewModel.mStockCode = split[34];//最低
//        stockViewModel.mStockCode = split[35];//价格/成交量（手）/成交额
//        stockViewModel.mStockCode = split[36];//成交量（手）
//        stockViewModel.mStockCode = split[37];//成交额（万）
        stockViewModel.mTurnover = split[38];//换手率
        stockViewModel.mRatio = split[39];//市盈率
//        stockViewModel.mStockCode = split[40];//无
//        stockViewModel.mStockCode = split[41];//最高
//        stockViewModel.mStockCode = split[42];//最低
//        stockViewModel.mStockCode = split[43];//振幅
//        stockViewModel.mStockCode = split[44];//流通市值
        stockViewModel.mValueAll = split[45];//总市值
//        stockViewModel.mStockCode = split[46];//市净率
//        stockViewModel.mStockCode = split[47];//涨停价
//        stockViewModel.mStockCode = split[48];//跌停价
        stockViewModel.init();
        return stockViewModel;
    }


}
