package com.stock.dao;


import com.stock.model.model.StockUserModel;
import com.stock.model.viewmodel.StockRankSQLViewModel;
import com.stock.model.viewmodel.StockSearchRankViewModel;
import com.stock.util.DateUtil;
import com.stock.util.Logger;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
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
        String sql = "UPDATE stock_user SET nickname = ?,area = ?,age = ?,moblie=? WHERE userid = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, stockUserModel.getNickName());
            pstmt.setString(2, stockUserModel.getArea());
            pstmt.setInt(3, stockUserModel.getAge());
            pstmt.setString(4, stockUserModel.getMoblie());
            pstmt.setInt(5, stockUserModel.getUserId());
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

                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date(createTime.getTime()));
                shopModel.setCreateTime(DateUtil.calendar2Time(cal, "yyyy-MM-dd hh:mm:ss"));

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

                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date(createTime.getTime()));
                userModel.setCreateTime(DateUtil.calendar2Time(cal, "yyyy-MM-dd hh:mm:ss"));

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

                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date(createTime.getTime()));
                userModel.setCreateTime(DateUtil.calendar2Time(cal, "yyyy-MM-dd hh:mm:ss"));
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

    @Override
    public int insertSearchRankSetting(StockSearchRankViewModel stockSearchRankViewModel) {
        String sql = "insert into stock_search_rank (show_type,search_type,search_title,search_desc,search_relation,search_weight) values (?,?,?,?,?,?)";
        PreparedStatement state = null;
        ResultSet rs = null;
        try {
            state = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            state.setInt(1, stockSearchRankViewModel.show_type);
            state.setInt(2, stockSearchRankViewModel.search_type);
            state.setString(3, stockSearchRankViewModel.search_title);
            state.setString(4, stockSearchRankViewModel.search_desc);
            state.setInt(5, stockSearchRankViewModel.search_relation);
            state.setInt(6, stockSearchRankViewModel.search_weight);
            int i = state.executeUpdate();
            if (i <= 0) {
                return 0;
            }
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            closeSql(state, rs);
        }
    }

    @Override
    public int insertSearchRankSql(StockRankSQLViewModel stockRankSQLViewModel) {
        String sql = "insert into stock_rank (search_relation,rank_title,submission_date,rank_sql) values (?,?,?,?)";
        PreparedStatement state = null;
        ResultSet rs = null;
        try {
            state = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            state.setInt(1, stockRankSQLViewModel.search_relation);
            state.setString(2, stockRankSQLViewModel.rank_title);
            state.setString(3, stockRankSQLViewModel.submission_date);
            state.setString(4, stockRankSQLViewModel.rank_sql);
            int i = state.executeUpdate();
            if (i <= 0) {
                return 0;
            }
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            closeSql(state, rs);
        }
    }

    @Override
    public boolean updateSearchRankSetting(StockSearchRankViewModel stockSearchRankViewModel) {
        String sql = "UPDATE stock_search_rank SET show_type = ?,search_type = ?,search_title = ?,search_desc=? ,search_relation=?,search_weight=? WHERE search_id = ?";
        PreparedStatement state = null;
        ResultSet rs = null;
        try {
            state = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            state.setInt(1, stockSearchRankViewModel.show_type);
            state.setInt(2, stockSearchRankViewModel.search_type);
            state.setString(3, stockSearchRankViewModel.search_title);
            state.setString(4, stockSearchRankViewModel.search_desc);
            state.setInt(5, stockSearchRankViewModel.search_relation);
            state.setInt(6, stockSearchRankViewModel.search_weight);
            state.setInt(7, stockSearchRankViewModel.search_id);
            int i = state.executeUpdate();
            if (i <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeSql(state, rs);
        }
    }

    @Override
    public boolean updateSearchRankSql(StockRankSQLViewModel stockRankSQLViewModel) {
        String sql = "UPDATE stock_rank SET rank_title = ?,submission_date = ?,rank_sql = ? WHERE search_relation = ?";
        PreparedStatement state = null;
        ResultSet rs = null;
        try {
            state = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            state.setString(1, stockRankSQLViewModel.rank_title);
            state.setString(2, stockRankSQLViewModel.submission_date);
            state.setString(3, stockRankSQLViewModel.rank_sql);
            state.setInt(4, stockRankSQLViewModel.search_relation);
            int i = state.executeUpdate();
            if (i <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeSql(state, rs);
        }
    }

    @Override
    public StockSearchRankViewModel selectSearchRankSetting(int search_id) {
        StockSearchRankViewModel stockSearchRankViewModel = new StockSearchRankViewModel();
        String sql = "select * from stock_search_rank where search_id = ?";
        PreparedStatement preStmt = null;
        try {
            preStmt = conn.prepareStatement(sql);
            preStmt.setInt(1, search_id);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                int show_type = rs.getInt("show_type");
                int search_type = rs.getInt("search_type");
                String search_title = rs.getString("search_title");
                String search_desc = rs.getString("search_desc");
                int search_relation = rs.getInt("search_relation");
                int search_weight = rs.getInt("search_weight");

                stockSearchRankViewModel.search_id = search_id;
                stockSearchRankViewModel.search_type = search_type;
                stockSearchRankViewModel.search_title = search_title;
                stockSearchRankViewModel.search_desc = search_desc;
                stockSearchRankViewModel.search_relation = search_relation;
                stockSearchRankViewModel.search_weight = search_weight;
                stockSearchRankViewModel.search_id = search_id;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSql(preStmt, null);
        }
        return stockSearchRankViewModel;
    }


    @Override
    public StockRankSQLViewModel selectSearchRankSql(int searchRelation) {
        StockRankSQLViewModel stockRankSQLViewModel = new StockRankSQLViewModel();
        String sql = "select * from stock_rank where search_relation = ?";
        PreparedStatement preStmt = null;
        try {
            preStmt = conn.prepareStatement(sql);
            preStmt.setInt(1, searchRelation);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                int rank_id = rs.getInt("rank_id");
                int search_relation = rs.getInt("search_relation");
                String rank_title = rs.getString("rank_title");
                String submission_date = rs.getString("submission_date");
                String rank_sql = rs.getString("rank_sql");

                stockRankSQLViewModel.rank_id = rank_id;
                stockRankSQLViewModel.search_relation = search_relation;
                stockRankSQLViewModel.rank_title = rank_title;
                stockRankSQLViewModel.submission_date = submission_date;
                stockRankSQLViewModel.rank_sql = rank_sql;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSql(preStmt, null);
        }
        return stockRankSQLViewModel;
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
