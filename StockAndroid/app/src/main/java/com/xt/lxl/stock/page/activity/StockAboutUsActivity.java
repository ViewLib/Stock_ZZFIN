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
public class StockAboutUsActivity extends FragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_setting_layout);
        initData();
        initView();
        bindData();
        initListener();
    }

    private void initListener() {

    }

    private void bindData() {

    }

    private void initView() {

    }

    private void initData() {
    }




    @Override
    public void onClick(View v) {
    }


}
