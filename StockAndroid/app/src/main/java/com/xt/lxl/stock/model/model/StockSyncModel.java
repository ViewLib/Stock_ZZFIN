package com.xt.lxl.stock.model.model;

public class StockSyncModel extends StockBaseModel {
    public String stockCode = "";
    public String stockName = "";
    public int version = 1;

    public StockSyncModel() {

    }

    public StockSyncModel(String stockCode, String stockName) {
        this.stockCode = stockCode;
        this.stockName = stockName;
    }

}
