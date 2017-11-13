package com.xt.lxl.stock.model.model;

/**
 * Created by xiangleiliu on 2017/10/16.
 * 包含日，周，月 的数据
 */
public class StockDateDataModel extends StockBaseModel {

    //以下数据服务端下发
    public String date;//当前时间string类型
    public Float high;//当前股票最高价格，单位：分
    public Float low;//当前股票最低价格，单位：分
    public Float open;//当前股票最低价格，单位：分
    public Float close;//当前股票最低价格，单位：分
    public Float volume;//当前股票成交量,单位：手
    public Float price_change;//价格变化，单位：元
    public Float p_change;//价格变化比例
    public Float turnover;//换手率

    //以下数据客户端计算

    public StockDateDataModel() {

    }

}
