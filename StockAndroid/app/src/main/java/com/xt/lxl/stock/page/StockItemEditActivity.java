package com.xt.lxl.stock.page;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.listener.StockItemEditCallBacks;
import com.xt.lxl.stock.model.StockViewModel;
import com.xt.lxl.stock.page.list.StockEditAdapter;
import com.xt.lxl.stock.sender.StockSender;
import com.xt.lxl.stock.util.DataSource;
import com.xt.lxl.stock.widget.view.HotelTagsViewV2;
import com.xt.lxl.stock.widget.view.StockEditableBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/8/6.
 */
public class StockItemEditActivity extends Activity implements View.OnClickListener {

    private TextView mCancelText;
    private StockEditableBar mEditBar;

    private HotelTagsViewV2 mHotContainer;//热门搜索列表
    private ListView mListView;//历史搜索

    List<StockViewModel> stockList = new ArrayList<>();
    StockEditAdapter editAdapter;

    StockItemEditCallBacks mCallBacks = new StockItemEditCallBacks();
    List<String> mSaveList = new ArrayList<>();
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_item_edit_layout);
        initData();
        initView();
        initListener();
        bindData();
    }

    private void bindData() {
        editAdapter = new StockEditAdapter(this, mCallBacks);
        editAdapter.setData(stockList);
        editAdapter.setSaveList(mSaveList);
        mListView.setAdapter(editAdapter);
    }

    private void initListener() {
        mCancelText.setOnClickListener(this);
        mEditBar.setInputListener(new StockEditableBar.IgetInputContentListener() {
            @Override
            public void getInput(String inputString) {
                if (inputString != null && inputString.length() == 6) {
                    assoStockByCode(inputString);
                }
            }
        });

        mCallBacks.mActionCallBack = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!(v.getTag() instanceof StockViewModel)) {
                    return;
                }
                StockViewModel model = (StockViewModel) v.getTag();
                //添加操作
                mSaveList.add(model.mStockCode);
                DataSource.addStockCode(StockItemEditActivity.this, model.mStockCode);
                editAdapter.notifyDataSetChanged();
            }
        };
    }

    private void initView() {
        mCancelText = (TextView) findViewById(R.id.stock_found_cancel);
        mEditBar = (StockEditableBar) findViewById(R.id.stock_keyword_edit_text);
        mHotContainer = (HotelTagsViewV2) findViewById(R.id.stock_hot_found_view);

        mListView = (ListView) findViewById(R.id.stock_asso_list);
    }

    private void initData() {
        //空
        mSaveList.clear();
        mSaveList.addAll(DataSource.getSaveStockCodeList(this));
    }

    private void assoStockByCode(final String code) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<StockViewModel> stockViewModels = StockSender.getInstance().requestStockModelByCode(code);
                stockList.clear();
                stockList.addAll(stockViewModels);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        editAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back_btn) {
            finish();
        }
    }
}
