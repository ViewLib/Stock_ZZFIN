package com.stock.dao;

import com.stock.model.model.*;
import com.stock.util.StringUtil;
import com.stock.viewmodel.SQLViewModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockDaoImpl implements StockDao {
    Connection conn;
    private static StockDaoImpl dao;

    private StockDaoImpl() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://115.159.31.128:3306/stock_zzfin?useUnicode=true&characterEncoding=utf-8";
            String user = "lxl";
            String password = "lxl301lxl";
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized static StockDaoImpl getDao() {
        if (dao == null) {
            dao = new StockDaoImpl();
        }
        return dao;
    }


    @Override
    public List<StockSearchModel> selectSerchModelRankList(int showType, int limit) {
        List<StockSearchModel> searchModelList = new ArrayList<>();
        String sql = "select * from stock_search_rank where show_type = ? order by search_weight desc limit ?";
        PreparedStatement preStmt = null;
        try {
            preStmt = conn.prepareStatement(sql);
            preStmt.setInt(1, showType);
            preStmt.setInt(2, limit);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                int searchId = rs.getInt("search_id");
//                int showType = rs.getInt("show_type");
                int searchType = rs.getInt("search_type");
                String searchTitle = rs.getString("search_title");
                String searchDesc = rs.getString("search_desc");
                int searchRelation = rs.getInt("search_relation");
                int searchWeight = rs.getInt("search_weight");

                StockSearchModel stockSearchModel = new StockSearchModel();
                stockSearchModel.searchType = searchType;
                stockSearchModel.showType = showType;
                stockSearchModel.searchWeight = searchWeight;
                if (stockSearchModel.searchType == StockSearchModel.STOCK_SEARCH_TYPE_STOCK) {
                    stockSearchModel.stockViewModel.stockCode = searchTitle;
                    stockSearchModel.stockViewModel.stockName = searchDesc;
                } else if (stockSearchModel.searchType == StockSearchModel.STOCK_SEARCH_TYPE_RNAK) {
                    stockSearchModel.rankModel.title = searchTitle;
                    stockSearchModel.rankModel.searchRelation = searchRelation;
                }
                searchModelList.add(stockSearchModel);
            }
            return searchModelList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSql(preStmt, null);
        }

        return searchModelList;
    }

    @Override
    public List<StockSyncModel> selectSyncModelList(int version) {
        List<StockSyncModel> syncModelList = new ArrayList<>();
        String sql = "select * from stock_all_db where version > ? order by stock_code";
        PreparedStatement preStmt = null;
        try {
            preStmt = conn.prepareStatement(sql);
            preStmt.setInt(1, version);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                String stockCode = rs.getString("stock_code");
                String stockName = rs.getString("stock_name");
                int sversion = rs.getInt("version");
                StockSyncModel syncModel = new StockSyncModel();
                syncModel.stockCode = stockCode;
                syncModel.stockName = stockName;
                syncModel.version = sversion;
                syncModelList.add(syncModel);
            }
            return syncModelList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSql(preStmt, null);
        }

        return syncModelList;
    }

    @Override
    public List<StockRankResultModel> selectRankDetailModelList(int search_relation, String strsql) {
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
                //拼接sql
               // System.out.println("1---rank_sql_01:"+rank_sql);
                if (!strsql.equals("")) {
                   // rank_sql = rank_sql + strsql;
                    rank_sql=rank_sql.replace("#",strsql);
                }else{
                    rank_sql=rank_sql.replace("#","");
                }
                Connection con = null;
                StockLinkDaoImpl stockLinkDao = new StockLinkDaoImpl();
                PreparedStatement preStmt2 = null;
               // System.out.println("2---search_relation:"+search_relation);
                //System.out.println("3---strsql:"+strsql);
                //System.out.println("4---rank_sql_02:"+rank_sql);
                try {
                    con = stockLinkDao.getConnection();
                    preStmt2 = con.prepareStatement(rank_sql);
                    ResultSet rslist = preStmt2.executeQuery();
                    StockRankResultModel resultModelHeader = null;
                    while (rslist.next()) {
                        ResultSetMetaData metaData = rslist.getMetaData();
                        String stockCode = metaData.getColumnName(1);
                        String stockName = metaData.getColumnName(2);
                        String attr1 = "";
                        String attr2 = "";
                        String attr3 = "";
                        if (metaData.getColumnCount() >= 3) {
                            attr1 = metaData.getColumnName(3);
                        }
                        if (metaData.getColumnCount() >= 4) {
                            attr2 = metaData.getColumnName(4);
                        }
                        if (metaData.getColumnCount() >= 5) {
                            attr3 = metaData.getColumnName(5);
                        }
                        if (resultModelHeader == null) {
                            resultModelHeader = new StockRankResultModel();
                            resultModelHeader.stockName = stockName;
                            resultModelHeader.stockCode = stockCode;
                            resultModelHeader.attr1 = attr1;
                            resultModelHeader.attr2 = attr2;
                            resultModelHeader.attr3 = attr3;
                            searchModelList.add(resultModelHeader);
                        }
                        StockRankResultModel resultModel = new StockRankResultModel();
                        resultModel.stockCode = rslist.getString(stockCode);
                        resultModel.stockName = rslist.getString(stockName);
                        if (!StringUtil.emptyOrNull(attr1)) {
                            resultModel.attr1 = rslist.getString(metaData.getColumnName(3));
                        }
                        if (!StringUtil.emptyOrNull(attr2)) {
                            resultModel.attr2 = rslist.getString(metaData.getColumnName(4));
                        }
                        if (!StringUtil.emptyOrNull(attr3)) {
                            resultModel.attr3 = rslist.getString(metaData.getColumnName(5));
                        }
                        searchModelList.add(resultModel);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                   // stockLinkDao.close(rs, preStmt2, con);
                }
            }
            return searchModelList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           // closeSql(preStmt, null);
        }

        return searchModelList;
    }

    @Override
    public List<StockRankFilterGroupModel> getStockRankFilterGroup() {
        //StockRankFilterItemModel
        List<StockRankFilterGroupModel> topList = new ArrayList<>();
        String first_sql = "select * from stock_filter_group where status=1";
        PreparedStatement preStmt = null;
        //第一层
        try {
            preStmt = conn.prepareStatement(first_sql);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                StockRankFilterGroupModel groupModel = new StockRankFilterGroupModel();
                groupModel.groupId = rs.getInt("group_id");
                groupModel.groupName = rs.getString("group_name");
                groupModel.level = rs.getInt("level");
                groupModel.showType = rs.getInt("show_type");

                topList.add(groupModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topList;
    }

    @Override
    public List<StockRankFilterGroupModel> getStockRankFilterSubGroup(int parentFilterId) {
        //StockRankFilterItemModel
        List<StockRankFilterGroupModel> secondList = new ArrayList<>();
        String first_sql = "select * from stock_filter_subgroup where parent_group_id =" + parentFilterId;
        PreparedStatement preStmt = null;
        //第一层
        try {
            preStmt = conn.prepareStatement(first_sql);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                StockRankFilterGroupModel groupModel = new StockRankFilterGroupModel();
                groupModel.groupId = rs.getInt("group_id");
                groupModel.groupName = rs.getString("group_name");
                groupModel.parentGroupId = rs.getInt("parent_group_id");
                groupModel.level = rs.getInt("level");
                groupModel.showType = rs.getInt("show_type");
                secondList.add(groupModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return secondList;
    }

    @Override
    public List<StockRankFilterItemModel> getAllStockRankFilterItem() {
        List<StockRankFilterItemModel> filterItemList = new ArrayList<>();
        String first_sql = "select * from stock_filter_item where status=1";
        PreparedStatement preStmt = null;
        try {
            preStmt = conn.prepareStatement(first_sql);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                StockRankFilterItemModel itemModel = new StockRankFilterItemModel();
                itemModel.filterId = rs.getInt("filter_id");
                itemModel.filterName = rs.getString("filter_name");
                itemModel.parentGroupId = rs.getInt("parent_group_id");
                filterItemList.add(itemModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filterItemList;
    }

    @Override
    public List<StockDateDataModel> selectStocDetailkDataList(String stockCode, String sqlCode) {
        List<StockDateDataModel> stockDetailDataModels = new ArrayList<>();
        String sql = "select * from stock_sqlsetup where sql_type=1 and sql_code=" + "'" + sqlCode + "'";
        //System.out.println("StockDetailDataModel:"+sql);
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        try {
            preStmt = conn.prepareStatement(sql);
            rs = preStmt.executeQuery();
            while (rs.next()) {
                String sql_title = rs.getString("sql_title");
                String sql_list = rs.getString("sql_list");
                Connection con = null;
                StockLinkDaoImpl stockLinkDao = new StockLinkDaoImpl();
                PreparedStatement preStmt2 = null;
                con = stockLinkDao.getConnection();
                try {
                    //preStmt = conn.prepareStatement(sql_list);
                    //preStmt.setString(1, stockCode);
                    //rs=preStmt.executeQuery();
                    preStmt2 = con.prepareStatement(sql_list);
                    preStmt2.setString(1, stockCode);
                    ResultSet rslist = preStmt2.executeQuery();
                    while (rslist.next()) {
                        // ResultSetMetaData metaData = rslist.getMetaData();
                        StockDateDataModel stockDetailDataModel = new StockDateDataModel();
                        //stockDetailDataModel.stockCode=rslist.getString(metaData.getColumnName(0));
                        // stockDetailDataModel.stockCode=rslist.getString("ts_code");
                        // System.out.println("0:"+stockDetailDataModel.stockCode);
                        // stockDetailDataModel.tradeDate=rslist.getDate(metaData.getColumnName(1));
                        stockDetailDataModel.dateStr = rslist.getString("trade_date");
                        // System.out.println("1:"+stockDetailDataModel.tradeDate);
                        // stockDetailDataModel.preClose=rslist.getDouble(metaData.getColumnName(2));
                        //stockDetailDataModel.closePrice=rslist.getInt("pre_close")*100;
                        //  System.out.println("2:"+stockDetailDataModel.preClose);
                        // stockDetailDataModel.Close=rslist.getDouble(metaData.getColumnName(3));
                        //stockDetailDataModel.=rslist.getDouble("close");
                        // System.out.println("3:"+stockDetailDataModel.Close);
                        //stockDetailDataModel.Open=rslist.getDouble(metaData.getColumnName(4));
                        stockDetailDataModel.openPrice = rslist.getInt("open") * 100;
                        //stockDetailDataModel.High=rslist.getDouble(metaData.getColumnName(5));
                        stockDetailDataModel.maxPrice = rslist.getInt("high") * 100;
                        // stockDetailDataModel.Low=rslist.getDouble(metaData.getColumnName(6));
                        stockDetailDataModel.minPrice = rslist.getInt("low") * 100;
                        //stockDetailDataModel.Change=rslist.getDouble(metaData.getColumnName(7));
                        stockDetailDataModel.closePrice = rslist.getInt("close") * 100;
                        // stockDetailDataModel.pctChange=rslist.getDouble(metaData.getColumnName(8));
                        //  stockDetailDataModel.pctChange=rslist.getDouble("pct_change");
                        // stockDetailDataModel.Volumn=rslist.getInt(metaData.getColumnName(9));
                        stockDetailDataModel.volume = rslist.getInt("volume");
                        // stockDetailDataModel.Amount=rslist.getDouble(metaData.getColumnName(10));
                        // stockDetailDataModel.Amount=rslist.getDouble("amount");
                        stockDetailDataModels.add(stockDetailDataModel);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stockDetailDataModels;
    }

    public int getHoliday(String p_date) {
        String sql = "select * from stock_date_holiday where holiday_date=" + p_date;
        PreparedStatement preStmt = null;
        try {
            preStmt = conn.prepareStatement(sql);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                return 1;
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSql(preStmt, null);
        }
        return 0;
    }

    public StockDetailCompanyModel getCompanyInfo(String stockCode) {
        StockDetailCompanyModel stockDetailCompanyModel = new StockDetailCompanyModel();
        String sql = "SELECT [PROVINCE],[CITY],[CHAIRMAN],[FOUNDDATE],[COMPANY_DESC] FROM [zzfin].[dbo].[TS_COMPANY]  where ts_code =" + "'" + stockCode + "'";
        Connection con = null;
        StockLinkDaoImpl stockLinkDao = new StockLinkDaoImpl();
        PreparedStatement preStmt = null;
        con = stockLinkDao.getConnection();
        try {
            preStmt = con.prepareStatement(sql);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                stockDetailCompanyModel.baseArea = rs.getString("province");
                stockDetailCompanyModel.companyBusiness = rs.getString("company_desc");
                stockDetailCompanyModel.companyName = rs.getString("chairman");
                stockDetailCompanyModel.establishDate = rs.getString("founddate");
            }
            return stockDetailCompanyModel;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSql(preStmt, null);
        }
        return stockDetailCompanyModel;
    }

    public List<StockDetailStockHolder> getStockHolder(String stockCode) {
        List<StockDetailStockHolder> stockDetailStockHolders = new ArrayList<>();
        String sql = "SELECT TOP 10[HOLDER_NAME],[HOLD_NUMBER],[HOLD_RATIO] FROM [zzfin].[dbo].[EQ_FLOAT_HOLDER] \n" +
                "where ts_code =? and end_date= (select max(end_date) from [zzfin].[dbo].[EQ_FLOAT_HOLDER] \n" +
                "where ts_code =?) order by hold_number desc";
        Connection con = null;
        StockLinkDaoImpl stockLinkDao = new StockLinkDaoImpl();
        PreparedStatement preStmt = null;
        con = stockLinkDao.getConnection();
        try {
            preStmt = con.prepareStatement(sql);
            preStmt.setString(1, stockCode);
            preStmt.setString(2, stockCode);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                StockDetailStockHolder stockDetailStockHolder = new StockDetailStockHolder();
                stockDetailStockHolder.stockHolderAmount = rs.getString("hold_number");
                stockDetailStockHolder.stockHolderName = rs.getString("holder_name");
                stockDetailStockHolder.stockHolderRatio = rs.getString("hold_ratio");
                stockDetailStockHolders.add(stockDetailStockHolder);
            }
            return stockDetailStockHolders;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSql(preStmt, null);
        }
        return stockDetailStockHolders;
    }

    @Override
    public String getListSql(int filter_id) {
        String sql = "select * from stock_filter_item where filter_id=" + filter_id;
        PreparedStatement preStmt = null;
        String strSql = "";
        try {
            preStmt = conn.prepareStatement(sql);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                strSql = rs.getString("filter_sql");
            }
            return strSql;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           // closeSql(preStmt, null);
        }
        return strSql;
    }

    public List<StockDetailFinanceItem> getFinalList(String stockCode, int FinanceType) {
        List<StockDetailFinanceItem> stockDetailFinanceItemList = new ArrayList<>();
        String sql = "";
        if (FinanceType == 1) {//收入
            sql = "select top 10 report_period,oper_rev from [wind].[dbo].[ASHAREINCOME] where wind_code=? and STATEMENT_TYPE=408001000 order by report_period desc";
        }
        if (FinanceType == 2) {//净利率
            sql = "SELECT TOP 10 report_period,s_fa_grossprofitmargin" +
                    "  FROM [wind].[dbo].[asharefinancialindicator] where wind_code=? order by report_period desc";
        }
        if (FinanceType == 3) {//毛利率
            sql = "SELECT TOP 10 report_period,s_fa_netprofitmargin" +
                    "  FROM [wind].[dbo].[asharefinancialindicator] where wind_code=? order by report_period desc";
        }
        if (FinanceType == 4) {//分红率
            sql = "SELECT div.EX_DIV_DATE date, [DIV_CASH_BONUS_PRE_TAX]/(price.[PRE_CLOSE]*price.ADJ_FACTOR) div_pct\n" +
                    "  FROM [zzfin].[dbo].[EQ_DIVIDEND]  div,[zzfin].[dbo].MKT_D_PRICE price\n" +
                    "  where div.ts_code=? and div.ts_code=price.ts_code and div.EX_DIV_DATE=price.TRADE_DATE order by div.EX_DIV_DATE desc";
        }
        Connection con = null;
        StockLinkDaoImpl stockLinkDao = new StockLinkDaoImpl();
        PreparedStatement preStmt = null;
        con = stockLinkDao.getConnection();
        try {
            preStmt = con.prepareStatement(sql);
            preStmt.setString(1, stockCode);
            ResultSet rs = preStmt.executeQuery();
            StockRankResultModel resultModelHeader = null;
            while (rs.next()) {
                StockDetailFinanceItem stockDetailFinanceItem = new StockDetailFinanceItem();
                ResultSetMetaData metaData = rs.getMetaData();
                String ColumnName1 = metaData.getColumnName(1);
                String ColumnName2 = metaData.getColumnName(2);
                stockDetailFinanceItem.dateStr = rs.getString(ColumnName1);
                stockDetailFinanceItem.valueStr = rs.getString(ColumnName2);
                stockDetailFinanceItemList.add(stockDetailFinanceItem);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSql(preStmt, null);
        }
        return stockDetailFinanceItemList;
    }

    @Override
    public List<SQLViewModel> getStockEventSQL(int type) {
        List<SQLViewModel> sqlViewModelList = new ArrayList<>();
        StockEventsDataList stockEventsDataLists = new StockEventsDataList();
        List<StockEventDataModel> eventsDataModelList = new ArrayList<>();
        String sql = "select * from stock_sqlsetup where sql_type=" + type;
        //System.out.println("StockDetailDataModel:"+sql);
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        try {
            preStmt = conn.prepareStatement(sql);
            rs = preStmt.executeQuery();
            while (rs.next()) {
                SQLViewModel sqlViewModel = new SQLViewModel();
                int sqlId = rs.getInt("sql_id");
                String sql_title = rs.getString("sql_title");
                String sql_list = rs.getString("sql_list");
                String sqlCode = rs.getString("sql_code");
                int sqlType = rs.getInt("sql_type");
                int subSqlType = rs.getInt("sql_subtype");
                sqlViewModel.sqlId = sqlId;
                sqlViewModel.sqlTitle = sql_title;
                sqlViewModel.sql = sql_list;
                sqlViewModel.sqlCode = sqlCode;
                sqlViewModel.sqlType = sqlType;
                sqlViewModel.subSqlType = subSqlType;
                sqlViewModelList.add(sqlViewModel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeSql(preStmt, null);
        }
        return sqlViewModelList;
    }

    private void closeSql(Statement stmt, ResultSet rs) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
