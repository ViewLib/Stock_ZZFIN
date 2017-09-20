package com.xt.lxl.stock.page.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.listener.StockItemEditCallBacks;
import com.xt.lxl.stock.model.model.StockSearchViewModel;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.model.reponse.StockHotSearchResponse;
import com.xt.lxl.stock.page.list.StoctHistoryAdapter;
import com.xt.lxl.stock.page.list.StoctResultAdapter;
import com.xt.lxl.stock.sender.StockSender;
import com.xt.lxl.stock.util.DataSource;
import com.xt.lxl.stock.util.MatchUtil;
import com.xt.lxl.stock.util.StringUtil;
import com.xt.lxl.stock.widget.view.HotelTagsViewV2;
import com.xt.lxl.stock.widget.view.StockEditableBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiangleiliu on 2017/8/6.
 */
public class StockSearchActivity extends Activity implements View.OnClickListener {

    private TextView mCancelText;
    private StockEditableBar mEditBar;

    private LinearLayout mHistoryView;//历史搜索界面
    private HotelTagsViewV2 mHotContainer;//热门搜索列表
    private ListView mHistoryListView;//历史搜索

    private LinearLayout mResultView;//搜索结果界面
    private ListView mResultListView;//搜索结果List

    StoctResultAdapter mResultAdapter;//搜索结果的adapter
    StoctHistoryAdapter mHistoryAdapter;//搜索结果的adapter

    List<StockViewModel> mSearchAllDataList = new ArrayList<>();//缓存的全部股票
    Map<String, StockViewModel> mSearchAllDataMap = new HashMap<>();//缓存的全部股票
    List<String> mSaveList = new ArrayList<>();
    List<StockViewModel> mResultList = new ArrayList<>();//搜索结果
    List<StockSearchViewModel> mHotList = new ArrayList<>();//热门搜索结果集
    List<StockViewModel> mHistoryList = new ArrayList<>();//历史搜索结果集

    Handler mHandler = new Handler();
    StockItemEditCallBacks mCallBacks = new StockItemEditCallBacks();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_item_edit_layout);
        initData();
        initView();
        initListener();
        bindAdapter();
        bindHotSearch();
        bindHistoryData();
    }

    private void bindAdapter() {
        mResultAdapter = new StoctResultAdapter(this, mCallBacks);
        mResultAdapter.setSaveList(mSaveList);
        mResultAdapter.setData(mResultList);
        mResultListView.setAdapter(mResultAdapter);

        mHistoryAdapter = new StoctHistoryAdapter(this, mCallBacks);
        mHistoryAdapter.setSaveList(mSaveList);
        mHistoryAdapter.setData(mHistoryList);
        mHistoryListView.setAdapter(mHistoryAdapter);
    }

    public void bindHotSearch() {
        mHotContainer.removeAllViews();
        for (StockSearchViewModel stockSearchViewModel : mHotList) {
            View inflate = null;
            if (stockSearchViewModel.mSearchType == StockSearchViewModel.STOCK_FOUND_TYPE_RNAK) {
                inflate = View.inflate(this, R.layout.stock_search_hot_item, null);
                TextView textView = (TextView) inflate.findViewById(R.id.hot_search_text);
                textView.setText(stockSearchViewModel.rankModel.mTitle);
            } else if (stockSearchViewModel.mSearchType == StockSearchViewModel.STOCK_FOUND_TYPE_STOCK) {
                inflate = View.inflate(this, R.layout.stock_search_hot_item, null);
                TextView textView = (TextView) inflate.findViewById(R.id.hot_search_text);
                textView.setText(stockSearchViewModel.stockViewModel.mStockName + " " + stockSearchViewModel.stockViewModel.mStockCode);
            }
            if (inflate == null) {
                continue;
            }
            mHotContainer.addView(inflate);
        }
    }

    /**
     *
     */
    public void bindResultData() {
        mHistoryView.setVisibility(View.GONE);
        mResultView.setVisibility(View.VISIBLE);
        mResultAdapter.notifyDataSetChanged();
    }

    private void bindHistoryData() {
        mHistoryView.setVisibility(View.VISIBLE);
        mResultView.setVisibility(View.GONE);
        mHistoryAdapter.notifyDataSetChanged();
    }

    private void initListener() {
        mCancelText.setOnClickListener(this);
        mEditBar.setInputListener(new StockEditableBar.IgetInputContentListener() {
            @Override
            public void getInput(String inputString) {
                //输入开始联想，6个数字或者匹配本地成功触发全量的联想
                if (StringUtil.emptyOrNull(inputString)) {
                    bindHistoryData();
                    return;
                }
                boolean searchResult = handleAssoSearchKey(inputString);
                //本地联想失败
                if (!searchResult) {
                    assoStockByCode(inputString);
                    //添加到历史列表
                    DataSource.addHistrySearch(StockSearchActivity.this, inputString);
                } else {
                    bindResultData();
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
                if (v.getId() == R.id.stock_item_list_history_item_action) {
                    //历史搜索中点击添加
                    mHistoryAdapter.notifyDataSetChanged();
                } else {
                    mResultAdapter.notifyDataSetChanged();
                }
            }
        };
    }

    private void initView() {
        mCancelText = (TextView) findViewById(R.id.stock_found_cancel);
        mEditBar = (StockEditableBar) findViewById(R.id.stock_keyword_edit_text);

        //历史搜索
        mHistoryView = (LinearLayout) findViewById(R.id.stock_search_history_layout);
        mHotContainer = (HotelTagsViewV2) findViewById(R.id.stock_hot_found_view);
        mHistoryListView = (ListView) findViewById(R.id.stock_asso_list);

        //搜索结果
        mResultView = (LinearLayout) findViewById(R.id.stock_search_result_layout);
        mResultListView = (ListView) findViewById(R.id.stock_result_list);
    }

    private void initData() {
        mSearchAllDataList.clear();
        mSearchAllDataList.addAll(DataSource.getSearchAllData());
        //List转成map形式
        for (StockViewModel model : mSearchAllDataList) {
            mSearchAllDataMap.put(model.mStockCode, model);
        }

        //已经保存的集合
        mSaveList.clear();
        mSaveList.addAll(DataSource.getSaveStockCodeList(this));

        //TODO 热门搜索需要发服务
        new Thread(new Runnable() {
            @Override
            public void run() {
                StockHotSearchResponse stockHotSearchResponse = DataSource.getStockHotSearchResponse(StockSearchActivity.this);
                mHotList.clear();
                mHotList.addAll(stockHotSearchResponse.mHotSearchList);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        bindHotSearch();
                    }
                });
            }
        }).start();

        mHistoryList.clear();
        List<String> historyStockCodeList = DataSource.getHistoryStockCodeList(this);//这里记录的都是code
        for (String code : historyStockCodeList) {
            StockViewModel stockViewModel = mSearchAllDataMap.get(code);
            mHistoryList.add(stockViewModel);
        }
    }

    //联想输入的关键词
    private boolean handleAssoSearchKey(String searchKey) {
        //如果是纯数字并且6位
        boolean isAllNumber = MatchUtil.matchAllNumber(searchKey);
        if (isAllNumber && searchKey.length() == 6) {
            return false;
        }

        //如果纯数字进行联想
        List<StockViewModel> searchResultList = new ArrayList<>();
        for (StockViewModel stockViewModel : mSearchAllDataList) {
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
            //这里构造数据，外部刷新
            mResultList.clear();
            mResultList.addAll(searchResultList);
        }
        return true;
    }

    private void assoStockByCode(final String code) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<StockViewModel> stockViewModels = StockSender.getInstance().requestStockModelByCode(code);
                mResultList.clear();
                mResultList.addAll(stockViewModels);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        bindResultData();
//                        mResultAdapter.notifyDataSetChanged();
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
