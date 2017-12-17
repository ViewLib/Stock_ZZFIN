package com.stock.dao;

import com.stock.dao.model.StockRankEntity;
import java.util.List;
import java.util.Map;

import org.hibernate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StockRankDaoImpl implements StockRankDao{
    private static final Logger log = LoggerFactory.getLogger(StockRankDaoImpl.class);

    public long getTotalCount(){
        log.debug("finding StockRank Total Count");
        try {
            String queryString = "select count(*) from StockRankEntity";
            Query queryObject = HibernateSessionFactory.getSession().createQuery(queryString);
            return (Long)queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("get total count failed", re);
            throw re;
        }
    }

    public List findByPage(int startIndex, int count){
        log.debug("finding StockRank instances by page");
        try {
            String queryString = "from StockRankEntity";
            Query queryObject = HibernateSessionFactory.getSession().createQuery(queryString);
            queryObject.setFirstResult((startIndex - 1) * count).setMaxResults(startIndex*count);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by page failed", re);
            throw re;
        }
    }

    public List findByProperty(String propertyName, Object value) {
        log.debug("finding StockRank instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from StockRankEntity as model where model." + propertyName + "= ?";
            Query queryObject = HibernateSessionFactory.getSession().createQuery(queryString);
            queryObject.setParameter(0, value);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public void save(StockRankEntity transientInstance) {
        log.debug("saving StockRank instance");
        Transaction tran = null;
        try {
            tran = HibernateSessionFactory.getSession().beginTransaction();
            HibernateSessionFactory.getSession().save(transientInstance);
            tran.commit();
            log.debug("save successful");
        } catch (RuntimeException re) {
            tran.rollback();
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(StockRankEntity persistentInstance) {
        log.debug("deleting StockRank instance");
        Transaction tran = null;
        try {
            tran = HibernateSessionFactory.getSession().beginTransaction();
            Object tem = HibernateSessionFactory.getSession().load(StockRankEntity.class, persistentInstance.getRankId());
            HibernateSessionFactory.getSession().evict(tem);
            HibernateSessionFactory.getSession().delete(persistentInstance);

            tran.commit();
            log.debug("delete successful");
        } catch (RuntimeException re) {
            tran.rollback();
            log.error("delete failed", re);
            throw re;
        }
    }

    public StockRankEntity merge(StockRankEntity detachedInstance) {
        log.debug("merging StockRank instance");
        try {
            StockRankEntity result = (StockRankEntity) HibernateSessionFactory.getSession().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(StockRankEntity instance) {
        log.debug("attaching dirty StockRank instance");
        try {
            HibernateSessionFactory.getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(StockRankEntity instance) {
        log.debug("attaching clean StockRank instance");
        try {
            HibernateSessionFactory.getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
}
