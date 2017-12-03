package com.xt.lxl.stock.page.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.util.StockUtil;
import com.xt.lxl.stock.widget.dialog.HotelCustomDialog;
import com.xt.lxl.stock.widget.view.StockInfoBar;

/**
 * Created by xiangleiliu on 2017/9/5.
 */
public class StockAboutUsActivity extends FragmentActivity implements View.OnClickListener {

    ImageView icon;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_aboutus_layout);
        initData();
        initView();
        bindData();
        initListener();
    }

    private void initListener() {

    }

    private void bindData() {
        icon.setBackgroundResource(R.drawable.stock_icon);
        text.setText("版本号：" + getVersion());
    }

    private void initView() {
        icon = (ImageView) findViewById(R.id.stock_version_icon);
        text = (TextView) findViewById(R.id.stock_version_text);
    }

    private void initData() {


    }


    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0";
        }
    }

    @Override
    public void onClick(View v) {
    }


}
