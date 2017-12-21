package com.stock.controller.sql.model;

/**
 * Created by 杨蕾 on 2017/12/17.
 */
public class SearchRankModel {
    private int searchId;
    private int showType;
    private int searchType;
    private String searchTitle;
    private String searchDesc;
    private int searchRelation;
    private int searchWeight;
    private String rankSql;

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public String getSearchTitle() {
        return searchTitle;
    }

    public void setSearchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
    }

    public String getSearchDesc() {
        return searchDesc;
    }

    public void setSearchDesc(String searchDesc) {
        this.searchDesc = searchDesc;
    }

    public int getSearchRelation() {
        return searchRelation;
    }

    public void setSearchRelation(int searchRelation) {
        this.searchRelation = searchRelation;
    }

    public int getSearchWeight() {
        return searchWeight;
    }

    public void setSearchWeight(int searchWeight) {
        this.searchWeight = searchWeight;
    }

    public String getRankSql() {
        return rankSql;
    }

    public void setRankSql(String rankSql) {
        this.rankSql = rankSql;
    }

    public int getSearchId() {
        return searchId;
    }

    public void setSearchId(int searchId) {
        this.searchId = searchId;
    }
}
