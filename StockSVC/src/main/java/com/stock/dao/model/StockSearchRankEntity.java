package com.stock.dao.model;

import javax.persistence.*;

/**
 * Created by 杨蕾 on 2017/12/9.
 */
@Entity
@Table(name = "stock_search_rank", schema = "stock_zzfin", catalog = "")
public class StockSearchRankEntity {
    private int searchId;
    private int showType;
    private int searchType;
    private String searchTitle;
    private String searchDesc;
    private String searchRelation;
    private int searchWeight;

    @Id
    @Column(name = "search_id", nullable = false)
    public int getSearchId() {
        return searchId;
    }

    public void setSearchId(int searchId) {
        this.searchId = searchId;
    }

    @Basic
    @Column(name = "show_type", nullable = false)
    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    @Basic
    @Column(name = "search_type", nullable = false)
    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    @Basic
    @Column(name = "search_title", nullable = false, length = 20)
    public String getSearchTitle() {
        return searchTitle;
    }

    public void setSearchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
    }

    @Basic
    @Column(name = "search_desc", nullable = true, length = 20)
    public String getSearchDesc() {
        return searchDesc;
    }

    public void setSearchDesc(String searchDesc) {
        this.searchDesc = searchDesc;
    }

    @Basic
    @Column(name = "search_relation", nullable = false, length = 5)
    public String getSearchRelation() {
        return searchRelation;
    }

    public void setSearchRelation(String searchRelation) {
        this.searchRelation = searchRelation;
    }

    @Basic
    @Column(name = "search_weight", nullable = false)
    public int getSearchWeight() {
        return searchWeight;
    }

    public void setSearchWeight(int searchWeight) {
        this.searchWeight = searchWeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockSearchRankEntity that = (StockSearchRankEntity) o;

        if (searchId != that.searchId) return false;
        if (showType != that.showType) return false;
        if (searchType != that.searchType) return false;
        if (searchWeight != that.searchWeight) return false;
        if (searchTitle != null ? !searchTitle.equals(that.searchTitle) : that.searchTitle != null) return false;
        if (searchDesc != null ? !searchDesc.equals(that.searchDesc) : that.searchDesc != null) return false;
        if (searchRelation != null ? !searchRelation.equals(that.searchRelation) : that.searchRelation != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = searchId;
        result = 31 * result + showType;
        result = 31 * result + searchType;
        result = 31 * result + (searchTitle != null ? searchTitle.hashCode() : 0);
        result = 31 * result + (searchDesc != null ? searchDesc.hashCode() : 0);
        result = 31 * result + (searchRelation != null ? searchRelation.hashCode() : 0);
        result = 31 * result + searchWeight;
        return result;
    }
}
