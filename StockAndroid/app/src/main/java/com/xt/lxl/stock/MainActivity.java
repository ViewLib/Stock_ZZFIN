package com.xt.lxl.stock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

/**
 * Created by xiangleiliu on 2017/8/24.
 */
public class MainActivity extends FragmentActivity {
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
        mTv.setText("测试界面");
    }

    private void initListener() {

    }


}
