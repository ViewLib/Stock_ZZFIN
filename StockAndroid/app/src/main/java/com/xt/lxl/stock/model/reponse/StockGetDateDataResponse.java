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

    public static final int TYPE_DAY = 1;//天
    public static final int TYPE_WEEK = 2;//周
    public static final int TYPE_MOUTH = 3;//月

    public int serviceCode = 5001;//服务号
    public String stockCode = "";//股票代码
    public String stockName = "";//股票名称
    public int kLinetype = TYPE_DAY;//坐标系类型
    public List<StockDateDataModel> dateDataList = new ArrayList<>();//分时数据

}
