package com.xt.lxl.stock.page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.list.StockAdapter;
import com.xt.lxl.stock.listener.StockListCallBacks;
import com.xt.lxl.stock.model.StockViewModel;
import com.xt.lxl.stock.sender.StockSender;
import com.xt.lxl.stock.util.DataSource;
import com.xt.lxl.stock.util.StockShowUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/8/2.
 */
public class StockListActivity extends Activity implements View.OnClickListener {
    public static final int RequestCodeForSearch = 1;
    StockListCallBacks mCallBacks = new StockListCallBacks();
    RelativeLayout mStockListTitle;
    RelativeLayout mStockListTopView;
    ListView mStockListView;
    StockAdapter mAdapter;
    Handler mHander = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_list_layout);
        initData();
        initView();
        initListener();
        bindData();
    }

    private void initListener() {
        findViewById(R.id.stock_list_search).setOnClickListener(this);
        findViewById(R.id.stock_list_edit).setOnClickListener(this);
        findViewById(R.id.stock_self_optional).setOnClickListener(this);
        findViewById(R.id.stock_self_quotation).setOnClickListener(this);
        findViewById(R.id.back_btn).setOnClickListener(this);
        mCallBacks.mAddCallBack = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(StockListActivity.this, StockItemEditActivity.class);
                startActivityForResult(intent, RequestCodeForSearch);
            }
        };
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<StockViewModel> stockList = new ArrayList<>();
                List<String> saveStockCodeList = DataSource.getSaveStockCodeList(StockListActivity.this);
                for (String code : saveStockCodeList) {
                    StockViewModel stockViewModel = StockSender.requestStockModelByCode(code);
                    stockList.add(stockViewModel);
                }
                mHander.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.initOneStockViewModel(stockList);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    private void initView() {
        mStockListTitle = (RelativeLayout) findViewById(R.id.stock_detail_titlebar_container);
        mStockListTopView = (RelativeLayout) findViewById(R.id.stock_list_top_view);
        mStockListView = (ListView) findViewById(R.id.stock_list);
    }

    private void bindData() {
        mAdapter = new StockAdapter(this, mCallBacks);
        mStockListView.setAdapter(mAdapter);
        mAdapter.initOneStockViewModel(new ArrayList<StockViewModel>());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.stock_list_search) {
            mCallBacks.mAddCallBack.onClick(null);
        } else if (id == R.id.stock_list_edit) {
            StockShowUtil.showTextOnMainThread(StockListActivity.this, "暂不支持该动能");
        } else if (id == R.id.stock_self_optional) {

        } else if (id == R.id.stock_self_quotation) {
            StockShowUtil.showTextOnMainThread(StockListActivity.this, "暂不支持该动能");
        } else if (id == R.id.back_btn) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCodeForSearch) {
            initData();
        }
    }
}
