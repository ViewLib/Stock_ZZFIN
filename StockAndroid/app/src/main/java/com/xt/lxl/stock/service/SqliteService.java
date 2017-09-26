package com.xt.lxl.stock.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xt.lxl.stock.model.model.StockSyncModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by xiangleiliu on 2017/9/25.
 */
public class SqliteService {

    private DBHelper dbhelper;

    public SqliteService(Context context) {
        this.dbhelper = new DBHelper(context);
    }


    public List<StockSyncModel> selectAllStockSyncModelList() {
        List<StockSyncModel> list = new ArrayList<>();
        SQLiteDatabase localSQLiteDatabase = this.dbhelper.getWritableDatabase();
        localSQLiteDatabase.beginTransaction();
        String format = String.format(Locale.CHINA, "select * from %s", DBHelper.TABLE_NAME);
        Cursor localCursor = localSQLiteDatabase.rawQuery(format, new String[]{});
        while (localCursor.moveToNext()) {
            StockSyncModel syncModel = new StockSyncModel();
            syncModel.stockCode = localCursor.getString(localCursor.getColumnIndex(DBHelper.STOCK_CODE));
            syncModel.stockName = localCursor.getString(localCursor.getColumnIndex(DBHelper.STOCK_NAME));
            syncModel.version = localCursor.getInt(localCursor.getColumnIndex(DBHelper.STOCK_VERSION));
            list.add(syncModel);
        }
        localSQLiteDatabase.setTransactionSuccessful();
        localSQLiteDatabase.endTransaction();
        return list;
    }

    public static void addStockModel(SQLiteDatabase localSQLiteDatabase, List<StockSyncModel> syncModelList) {
        localSQLiteDatabase.beginTransaction();
        for (StockSyncModel syncModel : syncModelList) {
            String stockCode = syncModel.stockCode;
            String stockName = syncModel.stockName;
            int version = syncModel.version;
            Object[] arrayOfObject = new Object[]{
                    stockCode, stockName, version
            };
            String format = String.format(Locale.CHINA, "insert into %s (%s,%s,%s) values(?,?,?)", DBHelper.TABLE_NAME, DBHelper.STOCK_CODE, DBHelper.STOCK_NAME, DBHelper.STOCK_VERSION);
            localSQLiteDatabase.execSQL(format, arrayOfObject);
        }
        localSQLiteDatabase.setTransactionSuccessful();
        localSQLiteDatabase.endTransaction();
    }

    /**
     */
    public void addStockModel(List<StockSyncModel> syncModelList) {
        addStockModel(this.dbhelper.getWritableDatabase(), syncModelList);
    }

    /**
     */
    public void addStockModel(StockSyncModel syncModel) {
        SQLiteDatabase localSQLiteDatabase = this.dbhelper.getWritableDatabase();
        String stockCode = syncModel.stockCode;
        String stockName = syncModel.stockName;
        int version = syncModel.version;
        Object[] arrayOfObject = new Object[]{
                stockCode, stockName, version
        };
        String format = String.format(Locale.CHINA, "insert into %s (%s,%s,%s) values(?,?,?)", DBHelper.TABLE_NAME, DBHelper.STOCK_CODE, DBHelper.STOCK_NAME, DBHelper.STOCK_VERSION);
        localSQLiteDatabase.execSQL(format, arrayOfObject);
    }

    public int selectCurrentVersion() {
        SQLiteDatabase localSQLiteDatabase = this.dbhelper.getWritableDatabase();
        String format = String.format(Locale.CHINA, "select %s from %s", DBHelper.VERSION_VERSION, DBHelper.VERSION_TABLE);
        Cursor localCursor = localSQLiteDatabase.rawQuery(format, new String[]{});
        int version = 0;
        while (localCursor.moveToNext()) {
            version = localCursor.getInt(localCursor.getColumnIndex(DBHelper.VERSION_VERSION));
            break;
        }
        return version;
    }

    public void updateCurrentVersion(int version) {
        SQLiteDatabase localSQLiteDatabase = this.dbhelper.getWritableDatabase();
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = version;
        localSQLiteDatabase.execSQL("update students set student_name=?,score=?,class_id=?  where student_id=?", arrayOfObject);
    }


}
