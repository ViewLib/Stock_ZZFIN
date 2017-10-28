package com.xt.lxl.stock.page.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.util.StockUtil;
import com.xt.lxl.stock.widget.dialog.HotelCustomDialog;
import com.xt.lxl.stock.widget.view.StockInfoBar;

/**
 * Created by xiangleiliu on 2017/9/5.
 */
public class StockSettingActivity extends FragmentActivity implements View.OnClickListener {

    public final String STOCK_CACHE_TAG = "STOCK_CACHE_TAG";
    public final String STOCK_CACHE_NUM_TAG = "STOCK_CACHE_NUM_TAG";
    public final String STOCK_PUSH_TAG = "STOCK_PUSH_TAG";
    public final String STOCK_REFRESH_TAG = "STOCK_REFRESH_TAG";

    StockInfoBar mClearcache;
    StockInfoBar mRefresh;
    StockInfoBar mStatement;
    StockInfoBar mPush;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_setting_layout);
        initData();
        initView();
        bindData();
        initListener();
    }

    private void initData() {
        SharedPreferences sharedPreferences = getSharedPreferences(STOCK_CACHE_TAG, 0);
        sharedPreferences.edit().putInt(STOCK_CACHE_NUM_TAG, getCacheNum() + 1).apply();
    }

    private int getCacheNum() {
        SharedPreferences sharedPreferences = getSharedPreferences(STOCK_CACHE_TAG, 0);
        return sharedPreferences.getInt(STOCK_CACHE_NUM_TAG, 0);
    }

    private boolean getPushSwitch() {
        SharedPreferences sharedPreferences = getSharedPreferences(STOCK_CACHE_TAG, 0);
        return sharedPreferences.getBoolean(STOCK_PUSH_TAG, true);
    }

    private boolean getRefreshSwtich() {
        SharedPreferences sharedPreferences = getSharedPreferences(STOCK_CACHE_TAG, 0);
        return sharedPreferences.getBoolean(STOCK_REFRESH_TAG, true);
    }


    private void initListener() {
        mClearcache.setOnClickListener(this);
        mRefresh.setOnClickListener(this);
        mStatement.setOnClickListener(this);
        mPush.setOnClickListener(this);
    }

    private void bindData() {
        mClearcache.bindInfoData("清除缓存", getCacheNum() / 2 + "M");//清除缓存
        mRefresh.bindInfoData("刷新设置", getRefreshSwtich() ? "开启中" : "已关闭");//
        mStatement.bindInfoData("免责声明", "");//免责声明
        mPush.bindInfoData("推送设置", getPushSwitch() ? "开启中" : "已关闭");//关闭
    }

    private void initView() {
        mClearcache = (StockInfoBar) findViewById(R.id.stock_setting_clearcache);
        mRefresh = (StockInfoBar) findViewById(R.id.stock_setting_refresh);
        mStatement = (StockInfoBar) findViewById(R.id.stock_setting_statement);
        mPush = (StockInfoBar) findViewById(R.id.stock_setting_push);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.stock_setting_clearcache) {
            showClearCacheDialog();
        } else if (v.getId() == R.id.stock_setting_refresh) {
            showReFreshDialog();
        } else if (v.getId() == R.id.stock_setting_statement) {
            gotoStatementPage();
        } else if (v.getId() == R.id.stock_setting_push) {
            showPushDialog();
        }
    }

    private void showClearCacheDialog() {
        HotelCustomDialog dialog = new HotelCustomDialog();
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("确定清除缓存么？");
        builder.setSpan(new TextAppearanceSpan(this, R.style.text_14_000000), 0, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        dialog.setContent(builder, "确定", "取消");
        dialog.setDialogBtnClick(new HotelCustomDialog.HotelDialogBtnClickListener() {
            @Override
            public void leftBtnClick(HotelCustomDialog dialog) {
                SharedPreferences sharedPreferences = getSharedPreferences(STOCK_CACHE_TAG, 0);
                boolean commit = sharedPreferences.edit().putInt(STOCK_CACHE_NUM_TAG, 0).commit();
                StockUtil.showToastOnMainThread(StockSettingActivity.this, "清除成功！");
                bindData();
            }

            @Override
            public void rightBtnClick(HotelCustomDialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.show(getSupportFragmentManager(), "HotelCustomDialog");
    }

    private void showReFreshDialog() {
        final boolean refreshSwtich = getRefreshSwtich();
        String desc = refreshSwtich ? "点击关闭刷新设置" : "点击开启自动刷新";
        String button = refreshSwtich ? "关闭刷新" : "开启刷新";

        HotelCustomDialog dialog = new HotelCustomDialog();
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(desc);
        builder.setSpan(new TextAppearanceSpan(this, R.style.text_14_000000), 0, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        dialog.setContent(builder, "取消", button);
        dialog.setDialogBtnClick(new HotelCustomDialog.HotelDialogBtnClickListener() {
            @Override
            public void leftBtnClick(HotelCustomDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void rightBtnClick(HotelCustomDialog dialog) {
                SharedPreferences sharedPreferences = getSharedPreferences(STOCK_CACHE_TAG, 0);
                boolean commit = sharedPreferences.edit().putBoolean(STOCK_REFRESH_TAG, !refreshSwtich).commit();
                StockUtil.showToastOnMainThread(StockSettingActivity.this, "设置成功！");
                bindData();
            }
        });
        dialog.show(getSupportFragmentManager(), "HotelCustomDialog");
    }

    private void gotoStatementPage() {
        Intent intent = new Intent();
        intent.setClass(this, StockDisclaimerActivity.class);
        startActivity(intent);
    }

    private void showPushDialog() {
        final boolean pushSwtich = getPushSwitch();
        String desc = pushSwtich ? "您已开启消息推送。\n关闭您将无法收到推送消息" : "您尚未开启消息推送。\n" +
                "点击开启推送";
        String button = pushSwtich ? "关闭" : "开启";

        HotelCustomDialog dialog = new HotelCustomDialog();
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(desc);
        builder.setSpan(new TextAppearanceSpan(this, R.style.text_14_000000), 0, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        dialog.setContent(builder, "取消", button);
        dialog.setDialogBtnClick(new HotelCustomDialog.HotelDialogBtnClickListener() {
            @Override
            public void leftBtnClick(HotelCustomDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void rightBtnClick(HotelCustomDialog dialog) {
                SharedPreferences sharedPreferences = getSharedPreferences(STOCK_CACHE_TAG, 0);
                boolean commit = sharedPreferences.edit().putBoolean(STOCK_PUSH_TAG, !pushSwtich).commit();
                StockUtil.showToastOnMainThread(StockSettingActivity.this, "设置成功！");
                bindData();
            }
        });
        dialog.show(getSupportFragmentManager(), "HotelCustomDialog");
    }

}
