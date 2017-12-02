package com.xt.lxl.stock.widget.stockchart.bean;

import com.xt.lxl.stock.model.model.StockMinuteDataModel;
import com.xt.lxl.stock.util.LogUtil;
import com.xt.lxl.stock.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/10/18.
 */
public class MinuteViewModel {

    public float mMaxPrice;//最高价格
    public float mMinPrice;//最低价格
    public float mMaxUpChange;//显示的最大涨幅
    public float mMaxDownChange;//显示的最大跌幅
    public float mMaxUpChangeRatio;//显示的最大涨幅比例
    public float mMaxDownChangeRatio;//显示的最大跌幅比例
    public float mPriceSum;//价格总价
    public float mBasePrice;//昨日价格
    public List<StockMinuteDataModel> minuteList = new ArrayList<>();//最大跌幅

    public void initAllModel(List<StockMinuteDataModel> list, String yestodayPrice) {
        if (list.size() == 0) {
            return;
        }
        try {
            mBasePrice = Float.parseFloat(yestodayPrice);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMaxPrice = mBasePrice;
        mMinPrice = mBasePrice;
        minuteList.clear();
        for (int i = 0; i < list.size(); i++) {
            StockMinuteDataModel stockMinuteData = list.get(i);
            stockMinuteData.state = 1;//lxltest
            //如果是9点25分的过滤掉
            if (stockMinuteData.time.equals("09:25")) {
                continue;
            }
            //最高价格
            addModel(stockMinuteData);
            if (stockMinuteData.price > mMaxPrice) {
                mMaxPrice = stockMinuteData.price;
            }
            if (stockMinuteData.price < mMinPrice) {
                mMinPrice = stockMinuteData.price;
            }
            //打印
//            LogUtil.LogI("time:" + stockMinuteData.time + ",price:" + stockMinuteData.price + ",volum:" + stockMinuteData.volume);
        }
        //计算最大涨幅和最大跌幅
        float maxUpChange = (mMaxPrice - mBasePrice);
        //最大跌幅
        float maxDownChange = (mMinPrice - mBasePrice);

        float maxChange = Math.abs(maxUpChange) > Math.abs(maxDownChange) ? Math.abs(maxUpChange) : Math.abs(maxDownChange);

        if (maxChange > 0) {
            mMaxUpChange = maxChange;
            mMaxDownChange = -maxChange;
            mMaxUpChangeRatio = mMaxUpChange / mBasePrice;
            mMaxDownChangeRatio = mMaxDownChange / mBasePrice;
        } else {
            mMaxUpChange = -maxChange;
            mMaxDownChange = maxChange;
            mMaxUpChangeRatio = mMaxUpChange / mBasePrice;
            mMaxDownChangeRatio = mMaxDownChange / mBasePrice;
        }
    }

//    public void initOneModel(StockMinuteDataModel stockMinuteData) {
//        addModel(stockMinuteData);
//        //计算最大涨幅和最大跌幅
//        float i = ((maxPrice - basePrice)) / basePrice;
//        if (i > 0) {
//            maxRiseChangeRatio = i;
//            maxDownChangeRatio = -i;
//        } else {
//            maxRiseChangeRatio = -i;
//            maxDownChangeRatio = i;
//        }
//    }

    private void addModel(StockMinuteDataModel stockMinuteData) {
        minuteList.add(stockMinuteData);
        mPriceSum += stockMinuteData.price;
        stockMinuteData.pjprice = mPriceSum / minuteList.size();//计算平均价格
        stockMinuteData.priceSpread = StringUtil.toFloat(stockMinuteData.change);
        stockMinuteData.basePrice = stockMinuteData.price + stockMinuteData.priceSpread;
        stockMinuteData.spreadPer = stockMinuteData.priceSpread / stockMinuteData.basePrice;
    }

}
