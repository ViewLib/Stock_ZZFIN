package com.stock.dao;

import com.stock.model.model.StockSearchModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockDaoImpl implements StockDao {
    Connection conn;

    public StockDaoImpl() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/stock_zzfin?useUnicode=true&characterEncoding=utf-8";
            String user = "lxl";
            String password = "lxl301lxl";
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
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