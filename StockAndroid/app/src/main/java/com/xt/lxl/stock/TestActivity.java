package com.xt.lxl.stock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.xt.lxl.stock.config.StockConfig;
import com.xt.lxl.stock.page.StockItemEditActivity;
import com.xt.lxl.stock.page.StockListActivity;
import com.xt.lxl.stock.page.StockRegisterActivity;
import com.xt.lxl.stock.util.StockShowUtil;

public class TestActivity extends FragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_main_layout);
        findViewById(R.id.xt_go_stock_list).setOnClickListener(this);
        findViewById(R.id.xt_go_stock_edit).setOnClickListener(this);
        findViewById(R.id.xt_go_user_center).setOnClickListener(this);
        findViewById(R.id.xt_go_stock_detail).setOnClickListener(this);
        findViewById(R.id.xt_clear_save).setOnClickListener(this);
        findViewById(R.id.xt_go_stock_register).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        int id = v.getId();
        if (id == R.id.xt_go_stock_list) {
            intent.setClass(this, StockListActivity.class);
        } else if (id == R.id.xt_go_user_center) {
            StockShowUtil.showToastOnMainThread(TestActivity.this, "暂不支持该动能");
        } else if (id == R.id.xt_go_stock_detail) {
            StockShowUtil.showToastOnMainThread(TestActivity.this, "暂不支持该动能");
        } else if (id == R.id.xt_go_stock_edit) {
            intent.setClass(this, StockItemEditActivity.class);
        } else if (id == R.id.xt_clear_save) {
            SharedPreferences codeList = getSharedPreferences(StockConfig.STOCK_SAVE_DB_NAME, 0);
            codeList.edit().clear().apply();
            Toast.makeText(TestActivity.this, "清除成功", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.xt_go_stock_register) {
            intent.setClass(this, StockRegisterActivity.class);
        }
        if (intent.getComponent() == null) return;
        startActivity(intent);
    }
}
