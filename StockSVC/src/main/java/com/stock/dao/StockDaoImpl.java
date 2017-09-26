package com.stock.dao;

import com.stock.model.model.StockRankDetailModel;
import com.stock.model.model.StockRankResultModel;
import com.stock.model.model.StockSearchModel;
import com.stock.model.model.StockSyncModel;

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
//        select search_id,search_weight from stock_search_rank where search_type = 1 order by search_weight desc limit 3
        String sql = "select * from stock_search_rank where show_type = ? order by search_weight desc limit ?";
        PreparedStatement preStmt = null;
        try {
            preStmt = conn.prepareStatement(sql);
            preStmt.setInt(1, showType);
            //  preStmt.setInt(2, limit);
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
