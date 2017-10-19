package com.xt.lxl.stock.widget.stockchart.bean;

import com.xt.lxl.stock.model.model.StockMinuteDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/10/18.
 */
public class MinuteViewModel {

    public float maxPrice;//最高价格
    public float minPrice;//最低价格
    public float maxRiseChange;//最高涨幅
    public float maxFallChange;//最大跌幅
    public float priceSum;//价格总价
    public float basePrice;//昨日价格
    public List<StockMinuteDataModel> minuteList = new ArrayList<>();//最大跌幅

    public void initAllModel(List<StockMinuteDataModel> list) {
        if (list.size() == 0) {
            return;
        }
        basePrice = list.get(0).basePrice / 100f;
        minPrice = basePrice;
        maxPrice = basePrice;
        for (int i = 0; i < list.size(); i++) {
            StockMinuteDataModel stockMinuteData = list.get(i);
            //最高价格
            addModel(stockMinuteData);
        }
        //计算最大涨幅和最大跌幅
        float i = ((maxPrice - basePrice)) / basePrice;
        if (i > 0) {
            maxRiseChange = i;
            maxFallChange = -i;
        } else {
            maxRiseChange = -i;
            maxFallChange = i;
        }
    }

    public void initOneModel(StockMinuteDataModel stockMinuteData) {
        addModel(stockMinuteData);
        //计算最大涨幅和最大跌幅
        float i = ((maxPrice - basePrice)) / basePrice;
        if (i > 0) {
            maxRiseChange = i;
            maxFallChange = -i;
        } else {
            maxRiseChange = -i;
            maxFallChange = i;
        }
    }


    private void addModel(StockMinuteDataModel stockMinuteData) {
        minuteList.add(stockMinuteData);
        float priceF = stockMinuteData.price / 100f;
        if (priceF > maxPrice) {
            maxPrice = priceF;
        }
        //最低价格
        if (priceF < minPrice) {
            minPrice = priceF;
        }
        priceSum += stockMinuteData.price;
        stockMinuteData.pjprice = priceSum / minuteList.size();//计算平均价格
        stockMinuteData.priceSpread = stockMinuteData.basePrice - stockMinuteData.price;
        stockMinuteData.spreadPer = stockMinuteData.priceSpread / stockMinuteData.basePrice;
    }

}
