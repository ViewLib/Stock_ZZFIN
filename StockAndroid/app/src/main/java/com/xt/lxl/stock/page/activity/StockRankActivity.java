package com.xt.lxl.stock.page.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.xt.lxl.stock.R;

/**
 * Created by xiangleiliu on 2017/8/24.
 */
public class StockRankActivity extends FragmentActivity {

    TextView mTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_stock_test_text);
        initData();
        initView();
        bindData();
        initListener();
    }

    private void initData() {

    }

    private void initView() {
        mTv = (TextView) findViewById(R.id.main_show_text);
    }

    private void bindData() {
        mTv.setText("股票排行界面");
    }

    private void initListener() {

    }

}
