package com.stock.controller.sql.model;

import com.stock.dao.model.StockRankEntity;
import java.util.List;

/**
 * Created by 杨蕾 on 2017/12/9.
 */
public class SearchSqlResponse {
    private List<SearchRankModel> rows;
    private long total;

    public List<SearchRankModel> getRows() {
        return rows;
    }

    public void setRows(List<SearchRankModel> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
