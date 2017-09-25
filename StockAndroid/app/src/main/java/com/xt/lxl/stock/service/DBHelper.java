package com.xt.lxl.stock.service;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.xt.lxl.stock.model.model.StockSyncModel;
import com.xt.lxl.stock.util.DataSource;

import java.util.List;

/**
 * Created by xiangleiliu on 2017/9/25.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String DB_NAME = "stock_db";//数据库名字
    public static String TABLE_NAME = "stock_all_data";// 表名
    public static String STOCK_CODE = "stock_code";// 列名
    public static String STOCK_NAME = "stock_name";// 列名
    public static String STOCK_VERSION = "stock_version";// 列名

    public static String VERSION_TABLE = "version_table";//版本表
    public static String VERSION_VERSION = "version_version";//版本记录

    private static final int DB_VERSION = 1;   // 数据库版本

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * 创建数据库
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        String sql = "CREATE TABLE " + TABLE_NAME + "(" + STOCK_CODE + " text primary key , " + STOCK_NAME + " text not null, " + STOCK_VERSION + " integer not null);";
        String sql2 = "CREATE TABLE " + VERSION_TABLE + "(" + VERSION_VERSION + " integer primary key);";
        try {
            db.execSQL(sql);
            db.execSQL(sql2);
        } catch (SQLException e) {
            Log.e(TAG, "onCreate " + TABLE_NAME + "Error" + e.toString());
            return;
        }
        insertInitData(db);
    }

    /**
     * 数据库升级
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //升级数据库的话，删掉所有历史数据，然后覆盖成最新的aseet中的db数据
        db.delete(TABLE_NAME, "", new String[0]);
    }

    public void insertInitData(SQLiteDatabase db) {
        List<StockSyncModel> syncModelList = DataSource.getSearchAllData();
        SqliteService.addStockModel(db, syncModelList);
    }

}