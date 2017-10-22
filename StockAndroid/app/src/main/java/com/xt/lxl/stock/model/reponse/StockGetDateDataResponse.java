package com.xt.lxl.stock.model.reponse;

import com.xt.lxl.stock.model.ServiceResponse;
import com.xt.lxl.stock.model.model.StockDateDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/10/16.
 * 分时数据 日、周、月
 */
public class StockGetDateDataResponse extends ServiceResponse {

    public static final String TYPE_DAY = "day";//天
    public static final String TYPE_WEEK = "week";//周
    public static final String TYPE_MONTH = "month";//月

    public int serviceCode = 5001;//服务号
    public String stockCode = "";//股票代码
    public String stockName = "";//股票名称
    public String stockKData = "";//坐标系类型
    public List<StockDateDataModel> dateDataList = new ArrayList<>();//分时数据

}
