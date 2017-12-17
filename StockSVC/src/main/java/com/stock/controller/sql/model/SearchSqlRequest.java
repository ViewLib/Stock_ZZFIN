package com.stock.controller.sql.model;

/**
 * Created by 杨蕾 on 2017/12/9.
 */
public class SearchSqlRequest {
    private int limit;    //页面大小
    private int offset;
    private int maxrows;
    private int pageindex;
    private int searchId;
    private int searchTitle;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getMaxrows() {
        return maxrows;
    }

    public void setMaxrows(int maxrows) {
        this.maxrows = maxrows;
    }

    public int getPageindex() {
        return pageindex;
    }

    public void setPageindex(int pageindex) {
        this.pageindex = pageindex;
    }

    public int getSearchId() {
        return searchId;
    }

    public void setSearchId(int searchId) {
        this.searchId = searchId;
    }

    public int getSearchTitle() {
        return searchTitle;
    }

    public void setSearchTitle(int searchTitle) {
        this.searchTitle = searchTitle;
    }
}
