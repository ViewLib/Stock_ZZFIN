package com.xt.lxl.stock.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.xt.lxl.stock.config.StockConfig;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by xiangleiliu on 2017/9/7.
 */
public class StockUser {

    /**
     * 不提供set方法
     */
    private int userId;
    private String moblie;
    private String nickName;
    private String area;
    private int age;
    private Date createTime;
    private boolean isExit;//true代表退出状态

    private static StockUser user = null;

    private StockUser() {

    }

    public synchronized static StockUser getStockUser(Context context) {
        if (user == null) {
            user = new StockUser();
            user.initUser(context);
        }
        return user;
    }

    private void initUser(Context context) {
        if (context == null) {
            return;
        }
        SharedPreferences db = context.getSharedPreferences(StockConfig.STOCK_USER_DB_NAME, 0);
        userId = db.getInt(StockConfig.STOCK_USER_DATA_USERID, 0);
        if (userId == 0) {
            return;
        }
        moblie = db.getString(StockConfig.STOCK_USER_DATA_MOBLIE, "");
        nickName = db.getString(StockConfig.STOCK_USER_DATA_NICKNAME, "");
        area = db.getString(StockConfig.STOCK_USER_DATA_AREA, "");
        age = db.getInt(StockConfig.STOCK_USER_DATA_AGE, 0);
        long createTimeL = db.getLong(StockConfig.STOCK_USER_DATA_CREATETIME, 0l);
        createTime = new Date(createTimeL);
    }

    public void saveUser(Context context, JSONObject userJson) {
        if (context == null) {
            return;
        }
        int mUserId = userJson.optInt("mUserId");
        if (mUserId == 0) {
            return;
        }
        String mMoblie = userJson.optString("mMoblie");
        String mClientId = userJson.optString("mClientId");
        String mNickName = userJson.optString("mNickName");
        String mArea = userJson.optString("mArea");
        int mAge = userJson.optInt("mAge");
        long mCreateTime = userJson.optLong("mCreateTime");

        SharedPreferences db = context.getSharedPreferences(StockConfig.STOCK_USER_DB_NAME, 0);
        SharedPreferences.Editor edit = db.edit();
        edit.putInt(StockConfig.STOCK_USER_DATA_USERID, mUserId);
        edit.putString(StockConfig.STOCK_USER_DATA_MOBLIE, mMoblie);
        edit.putString(StockConfig.STOCK_USER_DATA_NICKNAME, mNickName);
        edit.putString(StockConfig.STOCK_USER_DATA_AREA, mArea);
        edit.putInt(StockConfig.STOCK_USER_DATA_AGE, mAge);
        edit.putLong(StockConfig.STOCK_USER_DATA_CREATETIME, mCreateTime);
        if (edit.commit()) {
            initUser(context);
        }
    }

    public void clearUser(Context context) {
        if (context == null) {
            return;
        }
        SharedPreferences db = context.getSharedPreferences(StockConfig.STOCK_USER_DB_NAME, 0);
        db.edit().clear().apply();
        user.userId = 0;
        user.moblie = "";
        user.nickName = "";
        user.area = "";
        user.age = 0;
        user.createTime = null;
    }

    public int getUserId() {
        return userId;
    }

    public String getMoblie() {
        return moblie;
    }

    public String getNickName() {
        return nickName;
    }

    public String getArea() {
        return area;
    }

    public int getAge() {
        return age;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setExit(boolean isExit) {
        this.isExit = isExit;
    }

    public boolean isExit() {
        return isExit || userId == 0;
    }
}
