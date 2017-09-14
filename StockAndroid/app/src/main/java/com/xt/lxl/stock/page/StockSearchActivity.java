package com.xt.lxl.stock.page;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.listener.StockItemEditCallBacks;
import com.xt.lxl.stock.model.StockViewModel;
import com.xt.lxl.stock.page.list.StockEditAdapter;
import com.xt.lxl.stock.sender.StockSender;
import com.xt.lxl.stock.util.DataSource;
import com.xt.lxl.stock.util.MatchUtil;
import com.xt.lxl.stock.util.StringUtil;
import com.xt.lxl.stock.widget.view.HotelTagsViewV2;
import com.xt.lxl.stock.widget.view.StockEditableBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/8/6.
 */
public class StockSearchActivity extends Activity implements View.OnClickListener {

    private TextView mCancelText;
    private StockEditableBar mEditBar;

    private LinearLayout mHistoryView;//历史搜索界面
    private HotelTagsViewV2 mHotContainer;//热门搜索列表
    private ListView mHistoryList;//历史搜索

    private LinearLayout mResultView;//搜索结果界面
    private ListView mResultList;//搜索结果List

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
        mResultList.setAdapter(editAdapter);
    }

    private void initListener() {
        mCancelText.setOnClickListener(this);
        mEditBar.setInputListener(new StockEditableBar.IgetInputContentListener() {
            @Override
            public void getInput(String inputString) {
                //输入开始联想，6个数字或者匹配本地成功触发全量的联想
                if (StringUtil.emptyOrNull(inputString)) {
                    return;
                }
                boolean searchResult = handleAssoSearchKey(inputString);
                //本地联想失败
                if (!searchResult) {
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
                DataSource.addStockCode(StockSearchActivity.this, model.mStockCode);
                editAdapter.notifyDataSetChanged();
            }
        };
    }

    private void initView() {
        mCancelText = (TextView) findViewById(R.id.stock_found_cancel);
        mEditBar = (StockEditableBar) findViewById(R.id.stock_keyword_edit_text);

        //历史搜索
        mHistoryView = (LinearLayout) findViewById(R.id.stock_search_history_layout);
        mHotContainer = (HotelTagsViewV2) findViewById(R.id.stock_hot_found_view);
        mHistoryList = (ListView) findViewById(R.id.stock_asso_list);

        //搜索结果
        mResultView = (LinearLayout) findViewById(R.id.stock_search_result_layout);
        mResultList = (ListView) findViewById(R.id.stock_result_list);
    }

    private void initData() {
        mSaveList.clear();
        mSaveList.addAll(DataSource.getSaveStockCodeList(this));
    }

    //联想输入的关键词
    private boolean handleAssoSearchKey(String searchKey) {
        //如果是纯数字并且6位
        boolean isAllNumber = MatchUtil.matchAllNumber(searchKey);
        if (isAllNumber && searchKey.length() == 6) {
            return false;
        }
        List<StockViewModel> searchAllData = DataSource.getSearchAllData();
        //如果纯数字进行联想
        List<StockViewModel> searchResultList = new ArrayList<>();
        for (StockViewModel stockViewModel : searchAllData) {
            if (isAllNumber) {
                if (stockViewModel.mStockCode.startsWith(searchKey)) {
                    searchResultList.add(stockViewModel);
                }
            } else {
                if (stockViewModel.mStockName.startsWith(searchKey)) {
                    searchResultList.add(stockViewModel);
                }
            }
        }
        if (searchResultList.size() > 0) {
            stockList.clear();
            stockList.addAll(searchResultList);
            editAdapter.notifyDataSetChanged();
        }
        return true;
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
