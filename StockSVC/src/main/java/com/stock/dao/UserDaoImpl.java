package com.stock.dao;


import com.stock.model.StockUserModel;
import com.stock.util.Logger;

import java.io.Serializable;
import java.sql.*;
import java.util.*;
import java.util.Date;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

/**
 * Created by xiangleiliu on 2017/5/4.
 */
public class UserDaoImpl implements UserDao {
    Connection conn;

    public UserDaoImpl() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/stock_zzfin";
            String user = "lxl";
            String password = "lxl301lxl";
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int insertStockUserModel(StockUserModel stockUserModel) {
        String sql = "insert into stock_user (moblie) values (?)";
        PreparedStatement state = null;
        ResultSet rs = null;
        try {
            state = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            state.setString(1, stockUserModel.mMoblie);
            int i = state.executeUpdate();
            if (i <= 0) {
                return 0;
            }
            rs = state.getGeneratedKeys();
            if (rs.next()) {
                int userId = rs.getInt(1);
                return userId;
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            closeSql(state, rs);
        }
    }

    @Override
    public boolean updateStockUserModel(StockUserModel stockUserModel) {
        PreparedStatement pstmt = null;
        Logger.getLogger().showMessage("updateStockUserModel area:" + stockUserModel.mArea);
        String sql = "UPDATE stock_user SET nickname = ?,area = ?,age = ? WHERE userid = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, stockUserModel.mNickName);
            pstmt.setString(2, stockUserModel.mArea);
            pstmt.setInt(3, stockUserModel.mAge);
            pstmt.setInt(4, stockUserModel.mUserId);
            return pstmt.executeUpdate() > 0 ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeSql(pstmt, null);
        }
    }

    @Override
    public boolean deleteStockUserModel(int userId) {
        String sql = "delete from stock_user where userid =" + userId;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            return stmt.executeUpdate(sql) > 0 ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeSql(stmt, null);
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
                int userId = rs.getInt("userid");
                String moblie = rs.getString("moblie");
                String nickName = rs.getString("nickname");
                String area = rs.getString("area");
                int age = rs.getInt("age");
                Timestamp createTime = rs.getTimestamp("createTime");
                StockUserModel shopModel = new StockUserModel();
                shopModel.mUserId = userId;
                shopModel.mMoblie = moblie;
                shopModel.mNickName = nickName;
                shopModel.mArea = area;
                shopModel.mAge = age;
                shopModel.mCreateTime = new Date(createTime.getTime());
                return shopModel;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSql(preStmt, null);
        }
        return null;
    }

    @Override
    public StockUserModel selectStockUserModel(String moblie) {
        String sql = "select * from stock_user where moblie = ?";
        PreparedStatement preStmt = null;
        try {
            preStmt = conn.prepareStatement(sql);
            preStmt.setString(1, moblie);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                int userid = rs.getInt("userid");
                String nickname = rs.getString("nickname");
                String area = rs.getString("area");
                int age = rs.getInt("age");
                Timestamp createTime = rs.getTimestamp("createTime");

                StockUserModel userModel = new StockUserModel();
                userModel.mUserId = userid;
                userModel.mMoblie = moblie;
                userModel.mNickName = nickname;
                userModel.mArea = area;
                userModel.mAge = age;
                userModel.mCreateTime = new Date(createTime.getTime());
                return userModel;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSql(preStmt, null);
        }
        return null;
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
