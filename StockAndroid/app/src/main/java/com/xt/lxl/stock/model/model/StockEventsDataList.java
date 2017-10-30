package com.xt.lxl.stock.model.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2017/10/29.
 */
public class StockEventsDataList {
    public final static int TYPE_LIFTED = 1;//是否解禁
    public final static int TYPE_PLEDGE = 2;//股权质押

    public int eventType;//
    public String eventName = "";
    public List<StockEventsDataModel> stockEventsDataModels = new ArrayList<>();

}
