package com.xt.lxl.stock.model.model;

import com.xt.lxl.stock.util.StockUtil;
import com.xt.lxl.stock.util.StringUtil;

import java.math.BigDecimal;
import java.util.ArrayList;

public class StockDetailFinanceGroup {

    public final static int FinanceType_INCOME = 1;//收入
    public final static int FinanceType_SPLASHES = 2;//净利率
    public final static int FinanceType_GROSSMARGIN = 3;//毛利率
    public final static int FinanceType_ASSETS = 4;//

    public String financeName = "";//收入/净利率/毛利率/分红率
    public int financeType = FinanceType_INCOME;
    public ArrayList<StockDetailFinanceItem> financeItemList = new ArrayList<>();
    public ArrayList<StockDetailFinanceItem> yearItemList = new ArrayList<>();

    //以下值客户端来计算
    public float maxValue = 0;

    public String getShowItemUnit(float value) {
        if (maxValue == 0) {
            for (StockDetailFinanceItem item : yearItemList) {
                float v = StringUtil.toFloat(item.valueStr);
                if (v > maxValue) {
                    maxValue = v;
                }
            }
        }

        //判断一下类型，按照类型中最大值来算
        if (financeType == FinanceType_INCOME) {
            if (maxValue > 100000000) {
                return StockUtil.roundedFor(value / 100000000, 1) + "亿";
            }
            if (maxValue > 10000) {
                return StockUtil.roundedFor(value / 10000, 1) + "万";
            }
            return String.valueOf(value);
        }
        if (financeType == FinanceType_ASSETS) {
            Float aFloat = StockUtil.roundedFor(value, 1);
            return aFloat + "元";
        }
        if (financeType == FinanceType_SPLASHES || financeType == FinanceType_GROSSMARGIN) {
            Float aFloat = StockUtil.roundedFor(value, 1);
            return aFloat + "%";
        }
        return String.valueOf(value);
    }

}
