package com.stock.dao;

import com.stock.model.model.StockDetailGradleModel;
import com.stock.model.model.StockEventsDataList;
import com.stock.model.model.StockEventDataModel;
import com.stock.model.model.StockRankResultModel;
import com.stock.util.StringUtil;
import com.stock.viewmodel.SQLViewModel;
import com.stock.viewmodel.StoctEventSQLResultModel;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/10/29.
 */
public class StockLinkDaoImpl implements StockLinkDao {
    Connection conn;
    private static StockLinkDaoImpl dao;
    private static Logger logger = Logger.getLogger(StockLinkDaoImpl.class);
    // 连接驱动
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    // 连接路径
    private static final String URL = "jdbc:sqlserver://47.92.26.6:1433;databaseName=zzfin";
    // 用户名
    private static final String USERNAME = "wd";
    // 密码
    private static final String PASSWORD = "Abcd1234";

    //静态代码块
    static {
        try {
            // 加载驱动
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //获取数据库连接

    public StockLinkDaoImpl() {
        this.conn = getConnection();
    }

    public Connection getConnection() {
        logger.debug("开始连接数据库");
        System.out.println("开始连接数据库。");
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } else {
                return conn;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("数据库连接失败！");
            System.out.println("数据库连接失败。");
        }
        logger.debug("数据库连接成功");
        System.out.println("已顺利连接到数据库。");
        return conn;
    }

    /*
     * 关闭数据库连接，注意关闭的顺序
     */
    public void close(ResultSet rs, PreparedStatement ps, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("关闭ResultSet失败");
            }
        }
        if (ps != null) {
            try {
                ps.close();
                ps = null;
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("关闭PreparedStatement失败");
            }
        }
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("关闭Connection失败");
            }
        }
    }


    public List<StockRankResultModel> selectRankDetailModelList(int search_relation) {
        List<StockRankResultModel> searchModelList = new ArrayList<>();
        String sql = "select * from stock_rank where search_relation = ?";
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        try {
            preStmt = conn.prepareStatement(sql);
            preStmt.setInt(1, search_relation);
            rs = preStmt.executeQuery();
            while (rs.next()) {
                String rank_title = rs.getString("rank_title");
                String rank_sql = rs.getString("rank_sql");
                preStmt = conn.prepareStatement(rank_sql);
                ResultSet rslist = preStmt.executeQuery();
                while (rslist.next()) {
                    StockRankResultModel resultModel = new StockRankResultModel();
                    ResultSetMetaData metaData = rslist.getMetaData();
                    resultModel.stockCode = rslist.getString(metaData.getColumnName(1));
                    resultModel.stockName = rslist.getString(metaData.getColumnName(2));
                    if (metaData.getColumnCount() >= 3) {
                        resultModel.attr1 = rslist.getString(metaData.getColumnName(3));
                    }
                    if (metaData.getColumnCount() >= 4) {
                        resultModel.attr2 = rslist.getString(metaData.getColumnName(4));
                    }
                    if (metaData.getColumnCount() >= 5) {
                        resultModel.attr3 = rslist.getString(metaData.getColumnName(5));
                    }
                    searchModelList.add(resultModel);
                }
            }
            return searchModelList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close(rs, preStmt, conn);
        }

        return searchModelList;
    }

    @Override
    public List<StockDetailGradleModel> selectStockGradle(String stockCode) {
        List<StockDetailGradleModel> gradleModelList = new ArrayList<>();
        String sql = "SELECT top 20 [S_EST_INSTITUTE],[S_EST_LOWPRICE_INST],[S_EST_HIGHPRICE_INST],ann_dt FROM [wind].[dbo].[asharestockrating] where s_info_windcode=? order by ann_dt desc; ";
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        try {
            preStmt = getConnection().prepareStatement(sql);
            preStmt.setString(1, stockCode);
            rs = preStmt.executeQuery();
            while (rs.next()) {
                String brokerName = rs.getString("S_EST_INSTITUTE");//机构名称
                String lowPrice = rs.getString("S_EST_LOWPRICE_INST");//评级最低价
                String highPirce = rs.getString("S_EST_HIGHPRICE_INST");//评级最高价
                String dataStr = rs.getString("ann_dt");//评级日期
                preStmt = conn.prepareStatement(sql);
                StockDetailGradleModel gradleModel = new StockDetailGradleModel();
                gradleModel.dateStr = dataStr;
                gradleModel.maxPrice = StringUtil.toFloat(highPirce);
                gradleModel.minPrice = StringUtil.toFloat(lowPrice);
                gradleModel.stockBrokerName = brokerName;
                gradleModelList.add(gradleModel);
            }
            return gradleModelList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close(rs, preStmt, conn);
        }
        return gradleModelList;
    }

    public List<StoctEventSQLResultModel> getStockEventBySQLModel(SQLViewModel sqlViewModel, String stockCode) {
        List<StoctEventSQLResultModel> list = new ArrayList<>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        StockEventsDataList dataList = new StockEventsDataList();
        dataList.eventName = sqlViewModel.sqlTitle;
        dataList.eventType = sqlViewModel.sqlType;
        Connection con = getConnection();
        try {
            preStmt = con.prepareStatement(sqlViewModel.sql);
            preStmt.setString(1, stockCode);
            rs = preStmt.executeQuery();
            while (rs.next()) {
                ResultSetMetaData metaData = rs.getMetaData();
                StoctEventSQLResultModel resultModel = new StoctEventSQLResultModel();
                resultModel.eventDate = rs.getString(metaData.getColumnName(1));
                resultModel.attr1 = rs.getString(metaData.getColumnName(2));
                if (metaData.getColumnCount() >= 3) {
                    resultModel.attr2 = rs.getString(metaData.getColumnName(3));
                }
                if (metaData.getColumnCount() >= 4) {
                    resultModel.attr3 = rs.getString(metaData.getColumnName(4));
                }
                if (metaData.getColumnCount() >= 5) {
                    resultModel.attr4 = rs.getString(metaData.getColumnName(5));
                }
                if (metaData.getColumnCount() >= 6) {
                    resultModel.attr5 = rs.getString(metaData.getColumnName(6));
                }
                if (metaData.getColumnCount() >= 7) {
                    resultModel.attr6 = rs.getString(metaData.getColumnName(7));
                }
                if (metaData.getColumnCount() >= 8) {
                    resultModel.attr7 = rs.getString(metaData.getColumnName(8));
                }
                if (metaData.getColumnCount() >= 9) {
                    resultModel.attr8 = rs.getString(metaData.getColumnName(9));
                }
                if (metaData.getColumnCount() >= 10) {
                    resultModel.attr9 = rs.getString(metaData.getColumnName(10));
                }
                if (metaData.getColumnCount() >= 11) {
                    resultModel.attr10 = rs.getString(metaData.getColumnName(11));
                }
                list.add(resultModel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(rs, preStmt, con);
        }
        return list;
    }
}