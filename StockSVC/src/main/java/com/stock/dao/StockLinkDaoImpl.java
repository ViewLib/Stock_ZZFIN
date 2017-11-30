package com.stock.dao;

import com.mysql.jdbc.log.LogUtils;
import com.stock.model.model.*;
import com.stock.util.Logger;
import com.stock.util.StringUtil;
import com.stock.util.TransformUtil;
import com.stock.viewmodel.SQLViewModel;
import com.stock.viewmodel.StoctEventSQLResultModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiangleiliu on 2017/10/29.
 */
public class StockLinkDaoImpl implements StockLinkDao {
    private static StockLinkDaoImpl dao;
    private static Logger logger = Logger.getLogger();
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

    private StockLinkDaoImpl() {

    }

    public static synchronized StockLinkDaoImpl getStockLinkDaoImpl() {
        if (dao == null) {
            dao = new StockLinkDaoImpl();
        }
        return dao;
    }


    public synchronized Connection getConnection() {
        logger.showMessage("开始连接数据库");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.showMessage("数据库连接失败！");
        }
        logger.showMessage("数据库连接成功");
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
                logger.showMessage("关闭ResultSet失败");
            }
        }
        if (ps != null) {
            try {
                ps.close();
                ps = null;
            } catch (SQLException e) {
                e.printStackTrace();
                logger.showMessage("关闭PreparedStatement失败");
            }
        }
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
                logger.showMessage("关闭Connection失败");
            }
        }
    }


    public List<StockRankResultModel> selectRankDetailModelList(int search_relation) {
        List<StockRankResultModel> searchModelList = new ArrayList<>();
        String sql = "select * from stock_rank where search_relation = ?";
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        Connection connection = getConnection();
        try {
            preStmt = connection.prepareStatement(sql);
            preStmt.setInt(1, search_relation);
            rs = preStmt.executeQuery();
            while (rs.next()) {
                String rank_title = rs.getString("rank_title");
                String rank_sql = rs.getString("rank_sql");
                preStmt = connection.prepareStatement(rank_sql);
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
            this.close(rs, preStmt, connection);
        }

        return searchModelList;
    }

    @Override
    public List<StockDetailGradleModel> selectStockGradle(String stockCode) {
        List<StockDetailGradleModel> gradleModelList = new ArrayList<>();
        String sql = "SELECT top 20 [S_EST_INSTITUTE],[S_EST_LOWPRICE_INST],[S_EST_HIGHPRICE_INST],ann_dt FROM [wind].[dbo].[asharestockrating] where s_info_windcode=? order by ann_dt desc; ";
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        Connection connection = getConnection();
        try {
            preStmt = connection.prepareStatement(sql);
            preStmt.setString(1, stockCode);
            rs = preStmt.executeQuery();
            while (rs.next()) {
                String brokerName = rs.getString("S_EST_INSTITUTE");//机构名称
                String lowPrice = rs.getString("S_EST_LOWPRICE_INST");//评级最低价
                String highPirce = rs.getString("S_EST_HIGHPRICE_INST");//评级最高价
                String dataStr = rs.getString("ann_dt");//评级日期
                preStmt = connection.prepareStatement(sql);
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
            this.close(rs, preStmt, connection);
        }
        return gradleModelList;
    }

    @Override
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

    @Override
    public String selectLastTradeDate() {
        String tradeDate = "";
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        String sql = "select max(TRADE_DATE) from [zzfin].[dbo].mkt_d_price";//查询最后一个交易日
        Connection con = getConnection();
        try {
            preStmt = con.prepareStatement(sql);
            rs = preStmt.executeQuery();
            while (rs.next()) {
                String dateStr = rs.getString(1);
                tradeDate = dateStr;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close(rs, preStmt, con);
        }
        return tradeDate;
    }

    @Override
    public Map<String, String> selectRatioByCodeList(List<String> stockInfoList, String tradeDate) {
        Map<String, String> ratioMap = new HashMap<>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
//        String s = TransformUtil.stockCodeList2String(stockInfoList);
        String pssql = TransformUtil.stockCode2SQL(stockInfoList);
        String sql = "SELECT s_val_pe,TRADE_DT,[S_INFO_WINDCODE] ts_code FROM [wind].[dbo].[ashareeodderivativeindicator]  where TRADE_DT = ? and [S_INFO_WINDCODE] in (" +
                pssql + ")";//市盈率的
        Connection con = getConnection();
        try {
            preStmt = con.prepareStatement(sql);
            preStmt.setString(1, tradeDate);
            for (int i = 0; i < stockInfoList.size(); i++) {
                preStmt.setString(2 + i, stockInfoList.get(i));
            }
            rs = preStmt.executeQuery();
            while (rs.next()) {
                String ts_code = rs.getString("ts_code");
                String peStr = rs.getString("s_val_pe");
                ratioMap.put(ts_code, peStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close(rs, preStmt, con);
        }
        return ratioMap;
    }

    @Override
    public Map<String, String> selectIncomeGrowthByCodeList(List<String> stockInfoList, String tradeStr) {
        Map<String, String> map = new HashMap<>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        String pssql = TransformUtil.stockCode2SQL(stockInfoList);
        String sql = "select top 4 s_fa_yoy_or,s_info_windcode ts_code,report_period " +
                " from [wind].[dbo].asharefinancialindicator where [S_INFO_WINDCODE] in (" + pssql + ")" +
                " order by report_period desc;";//横向比较的sql
//        String s = TransformUtil.stockCodeList2String(stockInfoList);
        Connection con = getConnection();
        try {
            preStmt = con.prepareStatement(sql);
            for (int i = 0; i < stockInfoList.size(); i++) {
                preStmt.setString(1 + i, stockInfoList.get(i));
            }
            rs = preStmt.executeQuery();
            while (rs.next()) {
                String ts_code = rs.getString("ts_code");
                String s_fa_yoy_or = rs.getString("s_fa_yoy_or");
                map.put(ts_code, s_fa_yoy_or);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close(rs, preStmt, con);
        }
        return map;
    }

    @Override
    public Map<String, String> selectStockPriceByCodeList(List<String> stockInfoList, String tradeStr) {
        Map<String, String> map = new HashMap<>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        String pssql = TransformUtil.stockCode2SQL(stockInfoList);
        String sql = "SELECT ts_code,[CLOSE] FROM [zzfin].[dbo].[MKT_D_PRICE] " +
                " where TRADE_DATE = ? and ts_code in (" + pssql + ")";//横向比较的sql
        Connection con = getConnection();
        logger.showMessage("SQL:" + sql);
        logger.showMessage("数据:" + stockInfoList.toString());
        try {
            preStmt = con.prepareStatement(sql);
            preStmt.setString(1, tradeStr);
            for (int i = 0; i < stockInfoList.size(); i++) {
                preStmt.setString(2 + i, stockInfoList.get(i));
            }
            rs = preStmt.executeQuery();
            while (rs.next()) {
                String ts_code = rs.getString("ts_code");
                String close = rs.getString("CLOSE");
                map.put(ts_code, close);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close(rs, preStmt, con);
        }
        return map;
    }

    @Override
    public Map<String, String> selectShareOutByCodeList(List<String> stockInfoList, String tradeStr) {
        Map<String, String> ratioMap = new HashMap<>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        String pssql = TransformUtil.stockCode2SQL(stockInfoList);
        String sql = "SELECT div.ts_code ts_code, div.EX_DIV_DATE date, [DIV_CASH_BONUS_PRE_TAX]/(price.[PRE_CLOSE]*price.ADJ_FACTOR) div_pct" +
                " FROM [zzfin].[dbo].[EQ_DIVIDEND]  div,[zzfin].[dbo].MKT_D_PRICE price" +
                " where div.ts_code in (" + pssql + ") and div.ts_code=price.ts_code and div.EX_DIV_DATE=price.TRADE_DATE" +
                " order by div.EX_DIV_DATE desc;";//横向比较的sql
        Connection con = getConnection();
        try {
            preStmt = con.prepareStatement(sql);
            for (int i = 0; i < stockInfoList.size(); i++) {
                preStmt.setString(1 + i, stockInfoList.get(i));
            }
            rs = preStmt.executeQuery();
            while (rs.next()) {
                String ts_code = rs.getString("ts_code");
                String div_pct = rs.getString("div_pct");
                ratioMap.put(ts_code, div_pct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close(rs, preStmt, con);
        }
        return ratioMap;
    }

    @Override
    public Map<String, StockViewModel> selectStockByCode(List<String> stockList) {
        Map<String, StockViewModel> map = new HashMap<>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        String pssql = TransformUtil.stockCode2SQL(stockList);
        String sql = "select ts.name , ts.ts_code from [zzfin].[dbo].[TS_SECURITY] ts where ts.ts_code in (" + pssql + ");";//横向比较的sql
        Connection con = dao.getConnection();
        try {
            preStmt = con.prepareStatement(sql);
            for (int i = 0; i < stockList.size(); i++) {
                String stockCode = stockList.get(i);
                preStmt.setString((1 + i), stockCode);
            }
            rs = preStmt.executeQuery();
            while (rs.next()) {
                StockViewModel stockViewModel = new StockViewModel();
                String stockCode = rs.getString("ts_code");
                String stockName = rs.getString("name");
                stockViewModel.stockName = stockName;
                stockViewModel.stockCode = stockCode;
                map.put(stockCode, stockViewModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close(rs, preStmt, con);
        }
        return map;
    }

    @Override
    public String selectFirstTradeDate(String lastTradeDate) {
        String firstTradeDate = "";
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        String sql = "select distinct trade_date from [zzfin].[dbo].[MKT_D_PRICE] order by trade_date";
        Connection con = getConnection();
        try {
            preStmt = con.prepareStatement(sql);
            rs = preStmt.executeQuery();
            while (rs.next()) {
                String trade_date = rs.getString("trade_date");
                firstTradeDate = trade_date;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close(rs, preStmt, con);
        }
        return firstTradeDate;
    }

    @Override
    public List<String> selectCompareStockCodeList(String stockCode, String tradeDate) {
        List<String> codeList = new ArrayList<>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        String sql = "select top 3 ts_code from [zzfin].[dbo].mkt_d_price where ts_code in" +
                " (select s_info_windcode from wind.dbo.ashareswindustriesclass where sw_ind_code in " +
                "(select top 1 sw_ind_code from [wind].dbo.ashareswindustriesclass where S_INFO_WINDCODE =? order by ENTRY_DT desc" +
                "))" +
                " and TRADE_DATE =?" +
                " and ts_code !=? order by amount";//横向比较的sql
//        String sql = "";
        Connection con = getConnection();
        try {
            preStmt = con.prepareStatement(sql);
            preStmt.setString(1, stockCode);
            preStmt.setString(2, tradeDate);
            preStmt.setString(3, stockCode);
            rs = preStmt.executeQuery();
            while (rs.next()) {
                String ts_code = rs.getString("ts_code");
                codeList.add(ts_code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close(rs, preStmt, con);
        }
        return codeList;
    }


}