package com.xt.lxl.stock.util;

import android.content.Context;

import com.xt.lxl.stock.model.model.StockIndexChangeModel;
import com.xt.lxl.stock.model.model.StockSyncModel;
import com.xt.lxl.stock.model.model.StockViewModel;
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
        if (viewModel.stockChangeD == 0) {
            HotelLabelDrawable[] drawables = new HotelLabelDrawable[1];
            drawables[0] = new HotelLabelDrawable(context);
            StockIndexChangeModel centerModel = new StockIndexChangeModel();
            if (viewModel.isSuspension) {
                centerModel.mShowText = "停牌";
            } else {
                centerModel.mShowText = viewModel.stockChange + "%";
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
        if (viewModel.stockChangeD > 0) {
            leftModel.mBgColor = defaultColor;
            rightModel.mShowText = viewModel.stockChange + "%";
            rightModel.mShowIndex = viewModel.stockChangeD * 10;
            rightModel.mShowIndex = rightModel.mShowIndex > 1 ? 1 : rightModel.mShowIndex;
            rightModel.mBgColor = "#FA5259";
            if (viewModel.stockChangeD > 0.03) {
                rightModel.mTextColor = "#ffffff";
                rightModel.mShowLocation = StockIndexChangeModel.SHOW_LOCATION_CENTER;
            } else {
                rightModel.mTextColor = "#000000";
                rightModel.mShowLocation = StockIndexChangeModel.SHOW_LOCATION_RIGHT;
            }
        } else {
            rightModel.mBgColor = defaultColor;

            leftModel.mShowText = viewModel.stockChange + "%";
            leftModel.mShowIndex = viewModel.stockChangeD * 10;
            leftModel.mShowIndex = leftModel.mShowIndex < -1 ? -1 : leftModel.mShowIndex;
            leftModel.mBgColor = "#4CB774";
            leftModel.mTextColor = "#000000";
            if (viewModel.stockChangeD < -0.03) {
                leftModel.mTextColor = "#ffffff";
                leftModel.mShowLocation = StockIndexChangeModel.SHOW_LOCATION_LEFT;
            } else {
                leftModel.mTextColor = "#000000";
                leftModel.mShowLocation = StockIndexChangeModel.SHOW_LOCATION_CENTER;
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
        if (StringUtil.emptyOrNull(code) || (code.length() != 6 && code.length() != 8)) {
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
        stockViewModel.stockName = split[1];//股票名称
        stockViewModel.stockCode = split[2];//股票代码
        stockViewModel.stockPirce = split[3];//当前价格
        if ("0.00".equals(stockViewModel.stockPirce)) {
            //当前价格为0时代表停牌，取上一次的
            stockViewModel.stockPirce = split[4];
            stockViewModel.isSuspension = true;
        }
        stockViewModel.stockBasePirce = split[4];//昨收价格
//        stockViewModel.stockCode = split[5];//今开
//        stockViewModel.stockCode = split[6];//成交量（手）
//        stockViewModel.stockCode = split[7];//外盘
//        stockViewModel.stockCode = split[8];//内盘
//        stockViewModel.stockCode = split[9];//买一
//        stockViewModel.stockCode = split[10];//买一量（手）
//        stockViewModel.stockCode = split[11];//买二
//        stockViewModel.stockCode = split[12];//买二量（手）
//        stockViewModel.stockCode = split[13];//买三
//        stockViewModel.stockCode = split[14];//买三量（手）
//        stockViewModel.stockCode = split[15];//买四
//        stockViewModel.stockCode = split[16];//买四量（手）
//        stockViewModel.stockCode = split[17];//买五
//        stockViewModel.stockCode = split[18];//买五量（手）
//        stockViewModel.stockCode = split[19];//卖一
//        stockViewModel.stockCode = split[20];//卖一量（手）
//        stockViewModel.stockCode = split[21];//卖二
//        stockViewModel.stockCode = split[22];//卖二量（手）
//        stockViewModel.stockCode = split[23];//卖三
//        stockViewModel.stockCode = split[24];//卖三量（手）
//        stockViewModel.stockCode = split[25];//卖四
//        stockViewModel.stockCode = split[26];//卖四量（手）
//        stockViewModel.stockCode = split[27];//卖五
//        stockViewModel.stockCode = split[28];//卖五量（手）
//        stockViewModel.stockCode = split[29];//最近逐笔成交
//        stockViewModel.stockCode = split[30];//时间
        stockViewModel.stockChangeValue = split[31];//涨跌
        stockViewModel.stockChange = split[32];//涨跌%
        stockViewModel.maxPrice = split[33];//最高
        stockViewModel.minPrice = split[34];//最低
//        stockViewModel.stockCode = split[35];//价格/成交量（手）/成交额
        stockViewModel.volume = split[36];//成交量（手）
//        stockViewModel.stockCode = split[37];//成交额（万）
        stockViewModel.turnover = split[38];//换手率
        stockViewModel.ratio = split[39];//市盈率
//        stockViewModel.stockCode = split[40];//无
//        stockViewModel.stockCode = split[41];//最高
//        stockViewModel.stockCode = split[42];//最低
        stockViewModel.amplitude = split[43];//振幅
//        stockViewModel.stockCode = split[44];//流通市值
        stockViewModel.valueAll = split[45];//总市值
//        stockViewModel.stockCode = split[46];//市净率
//        stockViewModel.stockCode = split[47];//涨停价
//        stockViewModel.stockCode = split[48];//跌停价
        stockViewModel.init();
        return stockViewModel;
    }


    public static List<StockViewModel> stockList2stockSyncList(List<StockSyncModel> syncModelList) {
        List<StockViewModel> stockViewModelList = new ArrayList<>();
        for (StockSyncModel syncModel : syncModelList) {
            StockViewModel viewModel = new StockViewModel();
            viewModel.stockCode = syncModel.stockCode;
            viewModel.stockName = syncModel.stockName;
            stockViewModelList.add(viewModel);
        }
        return stockViewModelList;
    }

}
