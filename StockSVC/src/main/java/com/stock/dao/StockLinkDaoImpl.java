package com.stock.dao;

import com.stock.model.model.StockRankDetailModel;
import com.stock.model.model.StockRankResultModel;
import  com.stock.dao.StockDaoImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
/**
 * Created by hp on 2017/9/26.
 */
public class StockLinkDaoImpl implements StockLinkDao{
    Connection conn;
    private  static StockLinkDaoImpl dao;
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

    public Connection getConnection() {
        Connection conn = null;
        logger.debug("开始连接数据库");
        System.out.println("开始连接数据库。");
        try{
            conn=DriverManager.getConnection(URL, USERNAME, PASSWORD);

        }catch(SQLException e){
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
        if(rs!=null){
            try{
                rs.close();
                rs=null;
            }catch(SQLException e){
                e.printStackTrace();
                logger.error("关闭ResultSet失败");
            }
        }
        if(ps!=null){
            try{
                ps.close();
                ps=null;
            }catch(SQLException e){
                e.printStackTrace();
                logger.error("关闭PreparedStatement失败");
            }
        }
        if(conn!=null){
            try{
                conn.close();
                conn=null;
            }catch(SQLException e){
                e.printStackTrace();
                logger.error("关闭Connection失败");
            }
        }
    }



    public List<StockRankResultModel> selectRankDetailModelList(int search_relation){
        List<StockRankResultModel> searchModelList = new ArrayList<>();
        String sql = "select * from stock_rank where search_relation = ?";
        PreparedStatement preStmt = null;
        ResultSet rs=null;
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
                    StockRankResultModel resultModel=new StockRankResultModel();
                    resultModel.stockCode=rslist.getString("stockCode");
                    resultModel.stockName=rslist.getString("stockName");
                    resultModel.attr1=rslist.getString("attr1");
                    resultModel.attr2=rslist.getString("attr2");
                    resultModel.attr3=rslist.getString("attr3");
                    searchModelList.add(resultModel);
                }
            }
            return searchModelList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close( rs,preStmt,conn);
        }

        return  searchModelList;
    }
}
