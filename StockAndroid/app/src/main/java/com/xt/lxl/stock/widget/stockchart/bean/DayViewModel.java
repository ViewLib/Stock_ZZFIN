package com.xt.lxl.stock.widget.stockchart.bean;

import com.xt.lxl.stock.model.model.StockDateDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/10/18.
 */
public class DayViewModel {

    public float maxVolum;//最大换手量
    public List<StockDateDataModel> dateDataList = new ArrayList<>();//分时数据

}
