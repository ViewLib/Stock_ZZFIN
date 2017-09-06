package com.stock.dao;


import com.stock.model.StockUserModel;

import java.sql.*;

/**
 * Created by xiangleiliu on 2017/5/4.
 */
public class UserDaoImpl implements UserDao {
    Connection conn;

    public UserDaoImpl() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/stock_zzfin";
            String user = "root";
            String password = "lxl301lxl";
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean insertStockUserModel(StockUserModel stockUserModel) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE stock_user SET ");
//        sql.append(" userid =" + stockUserModel.mUserId);
        sql.append(" moblie =" + stockUserModel.mMoblie);
        sql.append(" nickname =" + stockUserModel.mNickName);
        sql.append(" are =" + stockUserModel.mArea);
        sql.append(" age =" + stockUserModel.mAge);
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            return stmt.executeUpdate(sql.toString()) > 0 ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean updateStockUserModel(StockUserModel shopModel) {

        return false;
    }

    @Override
    public boolean deleteStockUserModel(int shopId) {
        String sql = "delete from shop where shopid =" + shopId;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            return stmt.executeUpdate(sql) > 0 ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public StockUserModel selectStockUserModel(int shopId) {
        String sql = "select * from trading where shopid = " + shopId;
        PreparedStatement preStmt = null;
        try {
            preStmt = conn.prepareStatement(sql);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                int shopid = rs.getInt("shopid");
                int tradingid = rs.getInt("tradingid");
                String shopname = rs.getString("shopname");
                int saletype = rs.getInt("saletype");
                String location = rs.getString("location");
                int paymentmethod = rs.getInt("paymentmethod");
                String describes = rs.getString("describes");
                Timestamp createtime = rs.getTimestamp("createtime");
                StockUserModel shopModel = new StockUserModel();


                return shopModel;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (preStmt != null) {
                try {
                    preStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
