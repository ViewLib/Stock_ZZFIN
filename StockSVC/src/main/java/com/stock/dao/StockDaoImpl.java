package com.stock.dao;

import com.stock.model.model.*;
import com.stock.util.StringUtil;

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
                Connection con = null;
                StockLinkDaoImpl stockLinkDao = new StockLinkDaoImpl();
                PreparedStatement preStmt2 = null;
                try {
                    con = stockLinkDao.getConnection();
                    preStmt2 = con.prepareStatement(rank_sql);
                    ResultSet rslist = preStmt2.executeQuery();
                    StockRankResultModel resultModelHeader = null;
                    while (rslist.next()) {
                        ResultSetMetaData metaData = rslist.getMetaData();
                        String stockName = metaData.getColumnName(1);
                        String stockCode = metaData.getColumnName(2);
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
                    stockLinkDao.close(rs, preStmt2, con);
                }
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
    public List<StockRankFilterModel> selectStockFilterList(int first_type){
        List<StockRankFilterModel> stockRankFilterModelList=new ArrayList<>();
        String sql = "select * from stock_type where status=1 and first_type=?";
        PreparedStatement preStmt = null;
        try {
            preStmt = conn.prepareStatement(sql);
            preStmt.setInt(1, first_type);
            //System.out.println("已顺利连接到数据库:"+sql);
           // System.out.println("已顺利连接到数据库:"+first_type);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                Integer fiter_type = rs.getInt("filter_type");
                String filter_title = rs.getString("filter_title");
                StockRankFilterModel stockRankFilterModel = new StockRankFilterModel();
                stockRankFilterModel.filter_title = filter_title;
                stockRankFilterModel.filter_type = fiter_type;
                //System.out.println("已顺利连接到数据库:"+filter_title);
                String subsql="select  * from stock_filter_type where status=1 and filter_type="+fiter_type;
                List<StockFilterViewModel> viewModelList=new ArrayList<>();
                  try{
                      preStmt = conn.prepareStatement(subsql);
                      ResultSet rslist = preStmt.executeQuery();

                      while(rslist.next()){
                          StockFilterViewModel temp=new StockFilterViewModel();
                          temp.filter_title=rslist.getString("filter_title");
                          temp.filter_datail_type=rslist.getInt("filter_datail_type");
                          temp.filter_name=rslist.getString("filter_name");
                          temp.filter_type=rslist.getInt("filter_type");
                          viewModelList.add(temp);
                        //  System.out.println("viewModelList:"+viewModelList.size());
                      }
                      stockRankFilterModel.stockFilterViewModel=viewModelList;
                  } catch (Exception e) {
                      e.printStackTrace();
                  } finally {
                     // closeSql(preStmt, null);
                  }
                stockRankFilterModelList.add(stockRankFilterModel);
                //System.out.println("stockRankFilterModel:"+stockRankFilterModel.toString());
            }
            return stockRankFilterModelList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSql(preStmt, null);
        }
        return stockRankFilterModelList;
    }
    //获取filter大类
    public List<StockFirstTypeModel> selectStockFirstTypList(int version){
        List<StockFirstTypeModel> stockFirstTypeModels=new ArrayList<>();
        String sql="select * from stock_first_type where status=1";
        PreparedStatement preStmt = null;
        try {
            preStmt = conn.prepareStatement(sql);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                StockFirstTypeModel stockFirstTypeModel=new StockFirstTypeModel();
                stockFirstTypeModel.first_title=rs.getString("first_title");
                stockFirstTypeModel.first_type=rs.getInt("first_type");
                stockFirstTypeModels.add(stockFirstTypeModel);
            }
            return stockFirstTypeModels;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        closeSql(preStmt, null);
       }
        return stockFirstTypeModels;
    }
    @Override
    public List<StockDateDataModel> selectStocDetailkDataList(String stockCode,String sqlCode){
        List<StockDateDataModel> stockDetailDataModels=new  ArrayList<>();
        String sql = "select * from stock_sqlsetup where sql_type=1 and sql_code="+"'"+sqlCode+"'";
        //System.out.println("StockDetailDataModel:"+sql);
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        try{
            preStmt = conn.prepareStatement(sql);
            rs=preStmt.executeQuery();
            while (rs.next()){
                String sql_title = rs.getString("sql_title");
                String sql_list = rs.getString("sql_list");
                Connection con = null;
                StockLinkDaoImpl stockLinkDao = new StockLinkDaoImpl();
                PreparedStatement preStmt2 = null;
                con = stockLinkDao.getConnection();
                try{
                    //preStmt = conn.prepareStatement(sql_list);
                    //preStmt.setString(1, stockCode);
                    //rs=preStmt.executeQuery();
                    preStmt2 = con.prepareStatement(sql_list);
                    preStmt2.setString(1, stockCode);
                    ResultSet rslist = preStmt2.executeQuery();
                   while (rslist.next()){
                      // ResultSetMetaData metaData = rslist.getMetaData();
                       StockDateDataModel stockDetailDataModel=new StockDateDataModel();
                       //stockDetailDataModel.stockCode=rslist.getString(metaData.getColumnName(0));
                      // stockDetailDataModel.stockCode=rslist.getString("ts_code");
                      // System.out.println("0:"+stockDetailDataModel.stockCode);
                      // stockDetailDataModel.tradeDate=rslist.getDate(metaData.getColumnName(1));
                       stockDetailDataModel.dateStr=rslist.getString("trade_date");
                      // System.out.println("1:"+stockDetailDataModel.tradeDate);
                      // stockDetailDataModel.preClose=rslist.getDouble(metaData.getColumnName(2));
                       //stockDetailDataModel.closePrice=rslist.getInt("pre_close")*100;
                     //  System.out.println("2:"+stockDetailDataModel.preClose);
                      // stockDetailDataModel.Close=rslist.getDouble(metaData.getColumnName(3));
                       //stockDetailDataModel.=rslist.getDouble("close");
                      // System.out.println("3:"+stockDetailDataModel.Close);
                       //stockDetailDataModel.Open=rslist.getDouble(metaData.getColumnName(4));
                       stockDetailDataModel.openPrice=rslist.getInt("open")*100;
                       //stockDetailDataModel.High=rslist.getDouble(metaData.getColumnName(5));
                       stockDetailDataModel.maxPrice=rslist.getInt("high")*100;
                      // stockDetailDataModel.Low=rslist.getDouble(metaData.getColumnName(6));
                       stockDetailDataModel.minPrice=rslist.getInt("low")*100;
                       //stockDetailDataModel.Change=rslist.getDouble(metaData.getColumnName(7));
                       stockDetailDataModel.closePrice=rslist.getInt("close")*100;
                      // stockDetailDataModel.pctChange=rslist.getDouble(metaData.getColumnName(8));
                     //  stockDetailDataModel.pctChange=rslist.getDouble("pct_change");
                      // stockDetailDataModel.Volumn=rslist.getInt(metaData.getColumnName(9));
                       stockDetailDataModel.volume=rslist.getInt("volume");
                      // stockDetailDataModel.Amount=rslist.getDouble(metaData.getColumnName(10));
                      // stockDetailDataModel.Amount=rslist.getDouble("amount");
                       stockDetailDataModels.add(stockDetailDataModel);
                   }
                }catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return stockDetailDataModels;
    }

    public int getHoliday(String p_date){
        String sql = "select * from stock_date_holiday where holiday_date="+p_date;
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

    public StockDetailCompanyModel getCompanyInfo(String stockCode){
        StockDetailCompanyModel stockDetailCompanyModel=new StockDetailCompanyModel();
        String sql = "SELECT [PROVINCE],[CITY],[CHAIRMAN],[FOUNDDATE],[COMPANY_DESC] FROM [zzfin].[dbo].[TS_COMPANY]  where ts_code ="+"'"+stockCode+"'";
        Connection con = null;
        StockLinkDaoImpl stockLinkDao = new StockLinkDaoImpl();
        PreparedStatement preStmt = null;
        con = stockLinkDao.getConnection();
        try {
            preStmt = con.prepareStatement(sql);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                stockDetailCompanyModel.baseArea=rs.getString("province");
                stockDetailCompanyModel.companyBusiness=rs.getString("company_desc");
                stockDetailCompanyModel.companyName=rs.getString("chairman");
                stockDetailCompanyModel.establishDate=rs.getString("founddate");
            }
            return stockDetailCompanyModel;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSql(preStmt, null);
        }
        return stockDetailCompanyModel;
    }
    public List<StockDetailStockHolder> getStockHolder(String stockCode){
        List<StockDetailStockHolder> stockDetailStockHolders=new ArrayList<>();
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
                StockDetailStockHolder stockDetailStockHolder=new StockDetailStockHolder();
                stockDetailStockHolder.stockHolderAmount=rs.getString("hold_number");
                stockDetailStockHolder.stockHolderNmae=rs.getString("holder_name");
                stockDetailStockHolder.stockHolderRatio=rs.getString("hold_ratio");
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
