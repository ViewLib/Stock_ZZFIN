package com.xt.lxl.stock.page.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.widget.dialog.HotelCustomDialog;
import com.xt.lxl.stock.widget.view.StockInfoBar;

/**
 * Created by xiangleiliu on 2017/9/5.
 */
public class StockSettingActivity extends FragmentActivity implements View.OnClickListener {

    StockInfoBar mClearcache;
    StockInfoBar mRefresh;
    StockInfoBar mStatement;
    StockInfoBar mPush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_setting_layout);
        initView();
        bindData();
        initListener();
    }

    private void initListener() {
        mClearcache.setOnClickListener(this);
        mRefresh.setOnClickListener(this);
        mStatement.setOnClickListener(this);
        mPush.setOnClickListener(this);
    }

    private void bindData() {
        mClearcache.bindInfoData("清除缓存", "10M");//清除缓存
        mRefresh.bindInfoData("刷新频率", "");//
        mStatement.bindInfoData("免责声明", "");//免责声明
        mPush.bindInfoData("推送设置", "开启");//关闭
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
        dialog.setContent("确定清除缓存么？", "确定", "取消");
        dialog.setDialogBtnClick(new HotelCustomDialog.HotelDialogBtnClickListener() {
            @Override
            public void leftBtnClick(HotelCustomDialog dialog) {

            }

            @Override
            public void rightBtnClick(HotelCustomDialog dialog) {

            }
        });
        dialog.show(getSupportFragmentManager(), "HotelCustomDialog");
    }

    private void showReFreshDialog() {

    }

    private void gotoStatementPage() {

    }

    private void showPushDialog() {

    }

}
