package com.xt.lxl.stock.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.model.reponse.StockUserRegisterResponse;
import com.xt.lxl.stock.page.fragment.StockMainFoundFragment;
import com.xt.lxl.stock.page.fragment.StockMainListFragment;
import com.xt.lxl.stock.page.fragment.StockMainUserFragment;
import com.xt.lxl.stock.sender.StockSender;
import com.xt.lxl.stock.util.StockUser;

/**
 * Created by xiangleiliu on 2017/9/1.
 * 主界面，包含 自选股/发现/用户 三个界面
 */
public class StockMainActivity extends FragmentActivity implements View.OnClickListener {

    RadioButton mStockHomeSelf, mStockHomeFound, mStockHomeUser;
    FrameLayout mStockMainFragmentContainer;
    Fragment mStockListFragment;
    Fragment mStockFoundFragment;
    Fragment mStockUserFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.stock_main_layout);
        mStockMainFragmentContainer = (FrameLayout) findViewById(R.id.stock_main_fragment_container);
        mStockHomeSelf = (RadioButton) findViewById(R.id.stock_home_self);
        mStockHomeFound = (RadioButton) findViewById(R.id.stock_home_found);
        mStockHomeUser = (RadioButton) findViewById(R.id.stock_home_user);
        initListener();
        initFragment();
        setDefaultFragment();
        initCheck();
    }

    private void initListener() {
        mStockHomeSelf.setOnClickListener(this);
        mStockHomeFound.setOnClickListener(this);
        mStockHomeUser.setOnClickListener(this);
    }

    private void initFragment() {
        mStockListFragment = new StockMainListFragment();
        mStockFoundFragment = new StockMainFoundFragment();
        mStockUserFragment = new StockMainUserFragment();
    }

    private void setDefaultFragment() {
        mStockHomeSelf.setSelected(true);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.stock_main_fragment_container, mStockListFragment);
        transaction.commit();
    }

    private void initCheck() {
        syncUserInfo();
    }

    private void syncUserInfo() {
        final StockUser stockUser = StockUser.getStockUser(this);
        if (stockUser.isExit()) {
            return;
        }
        //更新用户资料
        new Thread(new Runnable() {
            @Override
            public void run() {
                //更新用户资料
                StockUserRegisterResponse registerResponse = StockSender.getInstance().requestRegister(stockUser.getMoblie(), "");
                stockUser.saveUser(StockMainActivity.this, registerResponse.userModel);
            }
        }).start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        mStockHomeSelf.setSelected(false);
        mStockHomeFound.setSelected(false);
        mStockHomeUser.setSelected(false);
        v.setSelected(true);
        int id = v.getId();
        FragmentManager fm = getSupportFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        if (id == R.id.stock_home_self) {
            transaction.replace(R.id.stock_main_fragment_container, mStockListFragment);
        } else if (id == R.id.stock_home_found) {
            transaction.replace(R.id.stock_main_fragment_container, mStockFoundFragment);
        } else if (id == R.id.stock_home_user) {
            transaction.replace(R.id.stock_main_fragment_container, mStockUserFragment);
        }
        transaction.commit();
    }
}
