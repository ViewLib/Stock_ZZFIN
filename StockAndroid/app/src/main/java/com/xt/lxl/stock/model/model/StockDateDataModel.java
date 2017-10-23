package com.xt.lxl.stock.model.model;

/**
 * Created by xiangleiliu on 2017/10/16.
 * 包含日，周，月 的数据
 */
public class StockDateDataModel  extends StockBaseModel{

    //以下数据服务端下发
    public String dateStr;//当前时间string类型
    public int maxPrice;//当前股票最高价格，单位：分
    public int minPrice;//当前股票最低价格，单位：分
    public int openPrice;//当前股票最低价格，单位：分
    public int closePrice;//当前股票最低价格，单位：分
    public int volume;//当前股票成交量,单位：手

    //以下数据客户端计算

    public StockDateDataModel() {

    }

    public StockDateDataModel(String dateStr, int maxPrice, int minPrice, int openPrice, int closePrice, int volume) {
        this.dateStr = dateStr;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.volume = volume;
    }
}
