package com.stock.dao.model;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by 杨蕾 on 2017/12/9.
 */
@Entity
@Table(name = "stock_rank", schema = "stock_zzfin", catalog = "")
public class StockRankEntity {
    private int rankId;
    private int searchRelation;
    private String rankTitle;
    private Date submissionDate;
    private String rankSql;

    @Id
    @Column(name = "rank_id", nullable = false)
    public int getRankId() {
        return rankId;
    }

    public void setRankId(int rankId) {
        this.rankId = rankId;
    }

    @Basic
    @Column(name = "search_relation", nullable = false)
    public int getSearchRelation() {
        return searchRelation;
    }

    public void setSearchRelation(int searchRelation) {
        this.searchRelation = searchRelation;
    }

    @Basic
    @Column(name = "rank_title", nullable = false, length = 300)
    public String getRankTitle() {
        return rankTitle;
    }

    public void setRankTitle(String rankTitle) {
        this.rankTitle = rankTitle;
    }

    @Basic
    @Column(name = "submission_date", nullable = true)
    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    @Basic
    @Column(name = "rank_sql", nullable = true, length = 3000)
    public String getRankSql() {
        return rankSql;
    }

    public void setRankSql(String rankSql) {
        this.rankSql = rankSql;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockRankEntity that = (StockRankEntity) o;

        if (rankId != that.rankId) return false;
        if (searchRelation != that.searchRelation) return false;
        if (rankTitle != null ? !rankTitle.equals(that.rankTitle) : that.rankTitle != null) return false;
        if (submissionDate != null ? !submissionDate.equals(that.submissionDate) : that.submissionDate != null)
            return false;
        if (rankSql != null ? !rankSql.equals(that.rankSql) : that.rankSql != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rankId;
        result = 31 * result + searchRelation;
        result = 31 * result + (rankTitle != null ? rankTitle.hashCode() : 0);
        result = 31 * result + (submissionDate != null ? submissionDate.hashCode() : 0);
        result = 31 * result + (rankSql != null ? rankSql.hashCode() : 0);
        return result;
    }
}
