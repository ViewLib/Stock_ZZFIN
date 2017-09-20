package com.xt.lxl.stock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xt.lxl.stock.config.StockConfig;
import com.xt.lxl.stock.model.model.StockIndexChangeModel;
import com.xt.lxl.stock.page.activity.StockSearchActivity;
import com.xt.lxl.stock.page.StockMainActivity;
import com.xt.lxl.stock.page.activity.StockRegisterActivity;
import com.xt.lxl.stock.util.StockShowUtil;
import com.xt.lxl.stock.widget.helper.HotelLabelDrawable;
import com.xt.lxl.stock.widget.view.StockChangeText;

public class TestActivity extends FragmentActivity implements View.OnClickListener {

    LinearLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_stock_test_layout);
        mContainer = (LinearLayout) findViewById(R.id.container);
//        findViewById(R.id.xt_go_stock_list).setOnClickListener(this);
        findViewById(R.id.xt_go_stock_edit).setOnClickListener(this);
        findViewById(R.id.xt_go_user_center).setOnClickListener(this);
        findViewById(R.id.xt_go_stock_detail).setOnClickListener(this);
        findViewById(R.id.xt_clear_save).setOnClickListener(this);
        findViewById(R.id.xt_go_stock_register).setOnClickListener(this);
        findViewById(R.id.xt_go_stock_home).setOnClickListener(this);
        addTest();
    }

    public void addTest() {
        HotelLabelDrawable leftDrawable = new HotelLabelDrawable(this);
        StockIndexChangeModel leftModel = new StockIndexChangeModel();
        leftModel.mShowText = "-1%";
        leftModel.mShowIndex = -0.1;
        leftModel.mBgColor = "#4CB774";
        leftModel.mTextColor = "#000000";
        leftDrawable.setLabelModel(leftModel);

        HotelLabelDrawable rightDrawable = new HotelLabelDrawable(this);
        StockIndexChangeModel rightModel = new StockIndexChangeModel();
        rightModel.mBgColor = "#EE7AE9";
        rightDrawable.setLabelModel(rightModel);

        StockChangeText text = new StockChangeText(this, null, 0);
        text.refreshLabelDrawables(leftDrawable, rightDrawable);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(400, 100);
        mContainer.addView(text, lp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        int id = v.getId();
        if (id == R.id.xt_go_user_center) {
            StockShowUtil.showToastOnMainThread(TestActivity.this, "暂不支持该动能");
        } else if (id == R.id.xt_go_stock_detail) {
            StockShowUtil.showToastOnMainThread(TestActivity.this, "暂不支持该动能");
        } else if (id == R.id.xt_go_stock_edit) {
            intent.setClass(this, StockSearchActivity.class);
        } else if (id == R.id.xt_clear_save) {
            SharedPreferences codeList = getSharedPreferences(StockConfig.STOCK_SAVE_DB_NAME, 0);
            codeList.edit().clear().apply();
            Toast.makeText(TestActivity.this, "清除成功", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.xt_go_stock_register) {
            intent.setClass(this, StockRegisterActivity.class);
        } else if (id == R.id.xt_go_stock_home) {
            intent.setClass(this, StockMainActivity.class);
        }
        if (intent.getComponent() == null) return;
        startActivity(intent);
    }
}
