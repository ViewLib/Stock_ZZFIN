package com.xt.lxl.stock.widget.stockchart.bean;

import com.xt.lxl.stock.model.model.StockDateData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/10/18.
 */
public class DayViewModel {


    public int maxVolum;//最大换手量
    public List<StockDateData> dateDataList = new ArrayList<>();//分时数据

}
