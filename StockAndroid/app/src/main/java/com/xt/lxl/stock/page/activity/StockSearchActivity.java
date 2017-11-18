package com.xt.lxl.stock.page.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.listener.StockItemEditCallBacks;
import com.xt.lxl.stock.model.model.StockFoundRankModel;
import com.xt.lxl.stock.model.model.StockSearchModel;
import com.xt.lxl.stock.model.model.StockSyncModel;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.model.reponse.StockHotSearchResponse;
import com.xt.lxl.stock.model.reponse.StockSyncResponse;
import com.xt.lxl.stock.page.adapter.StoctHistoryAdapter;
import com.xt.lxl.stock.page.adapter.StoctResultAdapter;
import com.xt.lxl.stock.sender.StockSender;
import com.xt.lxl.stock.service.SqliteService;
import com.xt.lxl.stock.util.DataShowUtil;
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
    List<StockSearchModel> mResultList = new ArrayList<>();//搜索结果
    List<StockSearchModel> mHistoryList = new ArrayList<>();//历史搜索结果集
    List<StockSearchModel> mHotList = new ArrayList<>();//热门搜索结果集

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
        syncStockList();
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
        mResultListView.setOnItemClickListener(mCallBacks.mSearchResultItemCallBack);
        mHistoryListView.setOnItemClickListener(mCallBacks.mSearchHistoryItemCallBack);
    }

    public void bindHotSearch() {
        mHotContainer.removeAllViews();
        for (StockSearchModel stockSearchViewModel : mHotList) {
            View inflate = null;
            TextView textView = null;
            if (stockSearchViewModel.searchType == StockSearchModel.STOCK_FOUND_TYPE_RNAK) {
                inflate = View.inflate(this, R.layout.stock_search_hot_item, null);
                textView = (TextView) inflate.findViewById(R.id.hot_search_text);
                textView.setText(stockSearchViewModel.rankModel.title);
            } else if (stockSearchViewModel.searchType == StockSearchModel.STOCK_FOUND_TYPE_STOCK) {
                inflate = View.inflate(this, R.layout.stock_search_hot_item, null);
                textView = (TextView) inflate.findViewById(R.id.hot_search_text);
                textView.setText(stockSearchViewModel.stockViewModel.stockName + " " + stockSearchViewModel.stockViewModel.stockCode);
            }
            if (inflate == null) {
                continue;
            }
            textView.setTag(stockSearchViewModel);
            textView.setOnClickListener(mCallBacks.mHotSearchCallBack);
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
                mSaveList.add(model.stockCode);
                DataSource.addStockCode(StockSearchActivity.this, model.stockCode);
                if (v.getId() == R.id.stock_item_list_history_item_action) {
                    //历史搜索中点击添加
                    mHistoryAdapter.notifyDataSetChanged();
                } else {
                    mResultAdapter.notifyDataSetChanged();
                }
            }
        };
        mCallBacks.mHotSearchCallBack = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StockSearchModel searchModel = (StockSearchModel) v.getTag();
                gotoStockInfoPage(searchModel);
            }
        };

        mCallBacks.mSearchHistoryItemCallBack = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StockSearchModel searchModel = mHistoryList.get(position);
                gotoStockInfoPage(searchModel);
            }
        };
        mCallBacks.mSearchResultItemCallBack = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StockSearchModel searchModel = mResultList.get(position);
                gotoStockInfoPage(searchModel);
            }
        };
    }

    private void gotoStockInfoPage(StockSearchModel searchModel) {
        if (searchModel.searchType == StockSearchModel.STOCK_FOUND_TYPE_STOCK) {
            gotoStockDetailPage(searchModel.stockViewModel);
        } else if (searchModel.searchType == StockSearchModel.STOCK_FOUND_TYPE_RNAK) {
            StockFoundRankModel rankModel = searchModel.rankModel;
            gotoStockRankPage(rankModel);
        }
    }

    /**
     * 跳转股票详情界面
     */
    private void gotoStockDetailPage(StockViewModel stockViewModel) {
        Intent intent = new Intent(StockSearchActivity.this, StockDetailActivity.class);
        intent.putExtra(StockDetailActivity.STOCK_DETAIL, stockViewModel);
        startActivity(intent);
    }

    /**
     * 跳转股票排行界面
     */
    private void gotoStockRankPage(StockFoundRankModel rankModel) {
        Intent intent = new Intent(this, StockRankActivity.class);
        intent.putExtra(StockRankActivity.STOCK_FOUND_RANK_MODEL, rankModel);
        startActivity(intent);
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
        final SqliteService service = new SqliteService(StockSearchActivity.this);
        List<StockSyncModel> searchAllData = service.selectAllStockSyncModelList();
        mSearchAllDataList.addAll(DataShowUtil.stockList2stockSyncList(searchAllData));
        //List转成map形式
        for (StockViewModel model : mSearchAllDataList) {
            mSearchAllDataMap.put(model.stockCode, model);
        }

        //已经保存的集合
        mSaveList.clear();
        mSaveList.addAll(DataSource.getSaveStockCodeList(this));

        //TODO 热门搜索需要发服务
        new Thread(new Runnable() {
            @Override
            public void run() {
                StockHotSearchResponse hotSearchResponse = StockSender.getInstance().requestHosSearchList();
                mHotList.clear();
                mHotList.addAll(hotSearchResponse.hotSearchList);
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
            //股票类型
            StockSearchModel searchModel = new StockSearchModel();
            if (MatchUtil.matchAllNumber(code)) {
                StockViewModel stockViewModel = mSearchAllDataMap.get(code);
                if (stockViewModel == null) {
                    continue;
                }
                searchModel.searchType = StockSearchModel.STOCK_FOUND_TYPE_STOCK;
                searchModel.stockViewModel = stockViewModel;
            } else if (code.contains("_")) {
                //事件
                String[] split = code.split("_");
                String eventCode = split[0];
                String eventDesc = split[1];
                StockFoundRankModel foundRankModel = new StockFoundRankModel();
                foundRankModel.searchRelation = StringUtil.toInt(eventCode);
                foundRankModel.title = eventDesc;
            } else {
                continue;
            }
            mHistoryList.add(searchModel);
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
        List<StockSearchModel> searchResultList = new ArrayList<>();
        for (StockViewModel stockViewModel : mSearchAllDataList) {
            StockSearchModel stockSearchModel = new StockSearchModel();
            if (isAllNumber) {
                if (stockViewModel.stockCode.startsWith(searchKey)) {
                    stockSearchModel.stockViewModel = stockViewModel;
                    searchResultList.add(stockSearchModel);
                }
            } else {
                if (stockViewModel.stockName.startsWith(searchKey)) {
                    stockSearchModel.stockViewModel = stockViewModel;
                    searchResultList.add(stockSearchModel);
                }
            }
        }
        for (StockSearchModel searchModel : mHotList) {
            if (searchModel.rankModel.title.startsWith(searchKey)) {
                searchResultList.add(searchModel);
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
                List<StockSearchModel> searchResultList = new ArrayList<>();
                for (StockViewModel stockViewModel : stockViewModels) {
                    StockSearchModel stockSearchModel = new StockSearchModel();
                    stockSearchModel.stockViewModel = stockViewModel;
                    searchResultList.add(stockSearchModel);
                }
                mResultList.addAll(searchResultList);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        bindResultData();
                    }
                });
            }
        }).start();
    }

    private void syncStockList() {
        final SqliteService service = new SqliteService(StockSearchActivity.this);
        updateSearchAllData(service);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int version = service.selectCurrentVersion();
                StockSyncResponse stockSyncResponse = StockSender.getInstance().requestStockSync(version);
                if (stockSyncResponse.syncModelList.size() == 0) {
                    return;
                }
                service.addStockModel(stockSyncResponse.syncModelList);
                service.updateCurrentVersion(version);
                updateSearchAllData(service);
            }
        }).start();
    }

    private void updateSearchAllData(SqliteService service) {
        List<StockSyncModel> updateList = service.selectAllStockSyncModelList();
        List<StockViewModel> updateList2 = DataShowUtil.stockList2stockSyncList(updateList);
        mSearchAllDataList.clear();
        mSearchAllDataList.addAll(updateList2);
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
        } else if (id == R.id.stock_found_cancel) {
            finish();
        }
    }
}
