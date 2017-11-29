package com.stock.model.model;

import java.util.ArrayList;
import java.util.List;

public class StockDetailFinanceGroup {

    public final static int FinanceType_INCOME = 1;//收入
    public final static int FinanceType_SPLASHES = 2;//净利率
    public final static int FinanceType_GROSSMARGIN = 3;//毛利率
    public final static int FinanceType_DIVIDERNDRATE = 4;//

    public String financeName = "";//收入/净利率/毛利率/分红率
    public int financeType = FinanceType_INCOME;
    public List<StockDetailFinanceItem> financeItemList = new ArrayList<>();
    public List<StockDetailFinanceItem> yearItemList = new ArrayList<>();

}
