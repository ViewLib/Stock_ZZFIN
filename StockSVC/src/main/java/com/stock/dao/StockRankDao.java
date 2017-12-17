package com.stock.dao;

import com.stock.dao.model.StockRankEntity;

import java.util.List;

/**
 * Created by 杨蕾 on 2017/12/9.
 */
public interface StockRankDao {
    long getTotalCount();
    List findByProperty(String propertyName, Object value);
    List findByPage(int startIndex, int count);
    void save(StockRankEntity transientInstance);
    void delete(StockRankEntity persistentInstance);
    StockRankEntity merge(StockRankEntity detachedInstance);
    void attachDirty(StockRankEntity instance);
    void attachClean(StockRankEntity instance);
}
