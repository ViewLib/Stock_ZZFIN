package com.stock.dao;


import com.stock.model.model.StockUserModel;
import com.stock.util.Logger;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by xiangleiliu on 2017/5/4.
 */
@Component
public class UserDaoImpl implements UserDao {
    Connection conn;

    public UserDaoImpl() {
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

    @Override
    public int insertStockUserModel(StockUserModel stockUserModel) {
        String sql = "insert into stock_user (moblie) values (?)";
        PreparedStatement state = null;
        ResultSet rs = null;
        try {
            state = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            state.setString(1, stockUserModel.getMoblie());
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
        Logger.getLogger().showMessage("updateStockUserModel area:" + stockUserModel.getArea());
        String sql = "UPDATE stock_user SET nickname = ?,area = ?,age = ? WHERE userid = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, stockUserModel.getNickName());
            pstmt.setString(2, stockUserModel.getArea());
            pstmt.setInt(3, stockUserModel.getAge());
            pstmt.setInt(4, stockUserModel.getUserId());
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
    public StockUserModel selectStockUserModel(int userid) {
        String sql = "select * from stock_user where userid = " + userid;
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
                shopModel.setUserId(userId);
                shopModel.setMoblie(moblie);
                shopModel.setNickName(nickName);
                shopModel.setArea(area);
                shopModel.setAge(age);
                shopModel.setCreateTime(new Date(createTime.getTime()));

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
                userModel.setUserId(userid);
                userModel.setMoblie(moblie);
                userModel.setNickName(nickname);
                userModel.setArea(area);
                userModel.setAge(age);
                userModel.setCreateTime(new Date(createTime.getTime()));

                return userModel;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSql(preStmt, null);
        }
        return null;
    }

    @Override
    public ArrayList<StockUserModel> selectUserInfoList(int startIndex, int count) {
        ArrayList<StockUserModel> userList = new ArrayList<>();
        String sql = "select * from stock_user limit " + (startIndex - 1) * count + "," + count;
        PreparedStatement preStmt = null;
        try {
            preStmt = conn.prepareStatement(sql);
//            preStmt.setInt(1, startIndex);
//            preStmt.setInt(2, count);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                int userid = rs.getInt("userid");
                String nickname = rs.getString("nickname");
                String moblie = rs.getString("moblie");
                String area = rs.getString("area");
                int age = rs.getInt("age");
                Timestamp createTime = rs.getTimestamp("createTime");

                StockUserModel userModel = new StockUserModel();
                userModel.setUserId(userid);
                userModel.setMoblie(moblie);
                userModel.setNickName(nickname);
                userModel.setArea(area);
                userModel.setAge(age);
                userModel.setCreateTime(new Date(createTime.getTime()));
                userList.add(userModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSql(preStmt, null);
        }
        return userList;
    }

    public int getUserTotalCount() {
        String sql = "select count(*) as totalcount from stock_user";
        PreparedStatement preStmt = null;
        try {
            preStmt = conn.prepareStatement(sql);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                return Integer.parseInt(rs.getString("totalcount"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSql(preStmt, null);
        }

        return 0;
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
