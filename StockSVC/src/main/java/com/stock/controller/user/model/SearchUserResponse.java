package com.stock.controller.user.model;

import com.stock.model.model.StockUserModel;

import java.util.List;

/**
 * Created by 杨蕾 on 2017/11/19.
 */
public class SearchUserResponse {
    private List<StockUserModel> rows;
    private long total;

    public List<StockUserModel> getRows() {
        return rows;
    }

    public void setRows(List<StockUserModel> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
