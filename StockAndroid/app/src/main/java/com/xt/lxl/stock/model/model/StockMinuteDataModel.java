package com.xt.lxl.stock.model.model;

/**
 * Created by xiangleiliu on 2017/10/16.
 */
public class StockMinuteDataModel {
    public long time;//当前时间long型 单位：毫秒
    public int price;//当前股票价格，单位：分
    public int volume;//当前股票成交量,单位：手
    public int state;//状态 0无数据 1正常 -1停牌
    public int basePrice;//昨日价格 单位：分

    //以下数据客户端计算
    public int priceSpread;//相对于昨日的价格差 单位：分
    public int spreadPer;//相对于昨日的价格差比例

    //以下数据客户端赋值
    public float pjprice;//当日成交平均价格

    public StockMinuteDataModel() {

    }

    public StockMinuteDataModel(long currentTime, int price, int voume, int baseyprice) {
        this.time = currentTime;
        this.price = price;
        this.volume = voume;
        this.basePrice = baseyprice;
        this.state = 1;
    }

}
