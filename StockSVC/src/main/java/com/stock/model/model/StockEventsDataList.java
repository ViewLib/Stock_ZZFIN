package com.stock.model.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2017/10/29.
 */
public class StockEventsDataList {

    /**
     * 重大事件的类型
     *
     * 是否解禁
     * 股权质押
     * 股权激励
     * 股票置换
     */
    public final static int TYPE_LIFTED = 101;//是否解禁
    public final static int TYPE_PLEDGE = 102;//股权质押
    public final static int TYPE_EXCITATION = 103;//股权激励
    public final static int TYPE_EXCHANGE = 104;//股票置换
    public final static int TYPE_INSTITUTIONAL_NUM = 105;//机构持股数量
    public final static int TYPE_SHAREHOLDER_NUM = 106;//股东变化数量
    public final static int TYPE_BUSINESS_DEPT = 108;//营业部买卖情况
    public final static int TYPE_GUOJIADUI_HOLER = 109;//国家队持股情况
    public final static int TYPE_REVENUE = 110;//营收情况
    public final static int TYPE_MARGING = 107;//融资融券
    /**
     *  重大消息的类型
     */
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
