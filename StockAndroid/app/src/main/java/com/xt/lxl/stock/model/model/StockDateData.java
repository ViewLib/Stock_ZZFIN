package com.xt.lxl.stock.model.model;

/**
 * Created by xiangleiliu on 2017/10/16.
 * 包含日，周，月 的数据
 */
public class StockDateData {
    public long currentTime;//当前时间long型 单位：毫秒
    public String currentTimeStr;//当前时间string类型
    public int price;//当前股票价格，单位：分
    public int volume;//当前股票成交量,单位：手

    public StockDateData() {

    }

    public StockDateData(long currentTime, String currentTimeStr, int price, int voume) {
        this.currentTime = currentTime;
        this.currentTimeStr = currentTimeStr;
        this.price = price;
        this.volume = voume;
    }
}
