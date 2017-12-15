package com.xt.lxl.stock.page.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.page.adapter.StockEditAdapter;
import com.xt.lxl.stock.util.DataSource;
import com.xt.lxl.stock.util.StockUtil;
import com.xt.lxl.stock.widget.dialog.HotelCustomDialog;
import com.xt.lxl.stock.widget.view.StockTitleView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/11/19.
 */
public class StockEditActivity extends FragmentActivity implements View.OnClickListener {
    public static final String StockListTag = "StockListTag";
    StockTitleView mStockTitleView;
    ListView mEditList;
    TextView mSelectAllBtn, mDeleteBtn;

    StockEditAdapter mEditAdapter;

    ArrayList<StockViewModel> mStockList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_edit_layout);
        initData();
        initView();
        intListener();
        bindDate();
    }

    private void initData() {
        Serializable serializable = getIntent().getExtras().getSerializable(StockListTag);
        if (serializable instanceof ArrayList) {
            mStockList = (ArrayList<StockViewModel>) serializable;
        }
        if (mStockList == null) {
            finish();
        }
    }

    private void initView() {
        mStockTitleView = (StockTitleView) findViewById(R.id.stock_title_view);
        mEditList = (ListView) findViewById(R.id.stock_edit_list);
        mSelectAllBtn = (TextView) findViewById(R.id.stock_edit_select_all);
        mDeleteBtn = (TextView) findViewById(R.id.stock_edit_select_delete);
        mEditAdapter = new StockEditAdapter(this, null);
        mEditList.setAdapter(mEditAdapter);
    }


    private void intListener() {
        mSelectAllBtn.setOnClickListener(this);
        mDeleteBtn.setOnClickListener(this);
        TextView actionBtn = mStockTitleView.getActionBtn();
        actionBtn.setText("完成");
        actionBtn.setOnClickListener(this);
    }


    private void bindDate() {
        mEditAdapter.setStockList(mStockList);
        mEditAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.action_btn) {

        } else if (id == R.id.stock_edit_select_all) {
            List<String> selectList = mEditAdapter.getSelectList();
            for (StockViewModel stockViewModel : mStockList) {
                selectList.add(stockViewModel.stockCode);
            }
            mEditAdapter.notifyDataSetChanged();
        } else if (id == R.id.stock_edit_select_delete) {
            List<String> selectList = mEditAdapter.getSelectList();
            if (selectList.size() == 0) {
                StockUtil.showToastOnMainThread(StockEditActivity.this, "请先选择股票！");
                return;
            }
            HotelCustomDialog dialog = new HotelCustomDialog();
            dialog.setContent("确定删除所选中的股票嘛？", "确定", "取消");
            dialog.setDialogBtnClick(new HotelCustomDialog.HotelDialogBtnClickListener() {
                @Override
                public void leftBtnClick(HotelCustomDialog dialog) {
                    deleteSelectStock();

                    StockUtil.showToastOnMainThread(StockEditActivity.this, "删除成功！");
                }

                @Override
                public void rightBtnClick(HotelCustomDialog dialog) {
                    dialog.dismiss();
                }
            });
            dialog.show(getSupportFragmentManager(), "dialog");
        }
    }

    private void deleteSelectStock() {
        List<String> selectList = mEditAdapter.getSelectList();
        List<String> saveStockCodeList = DataSource.getSaveStockCodeList(this);
        for (String stockCode : selectList) {
            if (saveStockCodeList.contains(stockCode)) {
                DataSource.deleteStockCode(this, stockCode);
            }
            for (StockViewModel stockViewModel : mStockList) {
                if (stockViewModel.stockCode.equals(stockCode)) {
                    mStockList.remove(stockViewModel);
                    break;
                }
            }
        }
        selectList.clear();
        mEditAdapter.notifyDataSetChanged();
        setResult(Activity.RESULT_OK);
    }
}
