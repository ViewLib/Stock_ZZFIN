package com.stock.model.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2017/10/29.
 */
public class StockEventsDataList {
    public final static int TYPE_LIFTED = 101;//是否解禁
    public final static int TYPE_PLEDGE = 102;//股权质押
    public final static int TYPE_HOLDER_CHANGE = 106;//股权质押

    public final static int TYPE_REFORM = 201;//重组
    public final static int TYPE_SETBY = 202;//定增
    public final static int TYPE_BLOCK_TRADING = 203;//大宗交易
    public final static int TYPE_DIVIEND = 204;//分红
    public final static int TYPE_REDUCE = 205;//减持

    public int eventType;//查询类型，2代表重大事件，3代表重大消息
    public int subType;//子类型
    public String eventName = "";
    public List<StockEventDataModel> stockEventsDataModels = new ArrayList<>();
}
