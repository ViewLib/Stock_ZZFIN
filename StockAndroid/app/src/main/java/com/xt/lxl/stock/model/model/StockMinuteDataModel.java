package com.xt.lxl.stock.model.model;

/**
 * Created by xiangleiliu on 2017/10/16.
 */
public class StockMinuteDataModel extends StockBaseModel {
    public String time;//时间
    public float price;//价格
    public int volume;//当前成交量
    public String change;//价格变化值
    public int amount;//总成交量
    public int state = 1;//状态

    //以下数据客户端计算
    public float basePrice;//昨日价格
    public float priceSpread;//相对于昨日的价格差
    public float spreadPer;//相对于昨日的价格差比例
    public float pjprice;//当日成交平均价格

    public StockMinuteDataModel() {

    }

//    public StockMinuteDataModel(String currentTime, int price, int voume, int baseyprice) {
//        this.time = currentTime;
//        this.price = price;
//        this.volume = voume;
//        this.basePrice = baseyprice;
//        this.state = 1;
//    }

}
