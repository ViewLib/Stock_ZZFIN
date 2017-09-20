package com.xt.lxl.stock.page.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.listener.StockListCallBacks;
import com.xt.lxl.stock.model.model.StockRankFilterModel;
import com.xt.lxl.stock.model.model.StockRankResultModel;
import com.xt.lxl.stock.model.reponse.StockRankDetailFilterlResponse;
import com.xt.lxl.stock.model.reponse.StockRankDetailResponse;
import com.xt.lxl.stock.page.list.StockRankAdapter;
import com.xt.lxl.stock.util.DataSource;
import com.xt.lxl.stock.util.HotelViewHolder;
import com.xt.lxl.stock.util.StockShowUtil;
import com.xt.lxl.stock.widget.view.StockTextView;
import com.xt.lxl.stock.widget.view.StockTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/8/24.
 */
public class StockRankActivity extends FragmentActivity {

    StockTitleView mTitleTv;
    LinearLayout mStockRankFilterContainer;
    LinearLayout mStockFilterHeaderContainer;
    ListView mStockRankListView;
    StockListCallBacks callBacks = new StockListCallBacks();

    StockRankAdapter mRankAdapter;

    //数据
    public String mTitle;
    public List<StockRankFilterModel> mRankFilerList = new ArrayList<>();
    public List<StockRankResultModel> mRankList = new ArrayList<>();


    Handler mHander = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_rank_layout);
        initData();
        initView();
        initListener();
        bindData();
    }

    private void initData() {
    }

    private void bindData() {
        mRankAdapter = new StockRankAdapter(this, callBacks);
        mRankAdapter.setRankResultList(mRankList);
        mRankAdapter.setSaveList(DataSource.getSaveStockCodeList(this));
        mStockRankListView.setAdapter(mRankAdapter);
        //排行
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHander.post(new Runnable() {
                    @Override
                    public void run() {
                        //赋值逻辑
                        StockRankDetailResponse rankDetailResponse = DataSource.getRankDetailResponse();
                        mRankList.clear();
                        mRankList.addAll(rankDetailResponse.mRankResultList);
                        bindRankData();
                    }
                });
            }
        }).start();

        //筛选项目列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHander.post(new Runnable() {
                    @Override
                    public void run() {
                        //赋值逻辑
                        StockRankDetailFilterlResponse rankDetailFilterResponse = DataSource.getRankDetailFilterResponse();
                        mRankFilerList.clear();
                        mRankFilerList.addAll(rankDetailFilterResponse.mRankFilterList);
                        bindFilterData();
                    }
                });
            }
        }).start();
    }

    private void initView() {
        mTitleTv = (StockTitleView) findViewById(R.id.stock_title_view);
        mStockRankFilterContainer = (LinearLayout) findViewById(R.id.stock_rank_filter);
        mStockFilterHeaderContainer = (LinearLayout) findViewById(R.id.stock_filter_header);
        mStockRankListView = (ListView) findViewById(R.id.stock_rank_list);
    }

    private void bindFilterData() {
        StockTextView filter1 = (StockTextView) mStockRankFilterContainer.findViewById(R.id.filter1);
        StockTextView filter2 = (StockTextView) mStockRankFilterContainer.findViewById(R.id.filter2);
        StockTextView filter3 = (StockTextView) mStockRankFilterContainer.findViewById(R.id.filter3);
        StockTextView filter4 = (StockTextView) mStockRankFilterContainer.findViewById(R.id.filter4);

        for (int i = 0; i < mRankFilerList.size(); i++) {
            StockRankFilterModel filterModel = mRankFilerList.get(i);
            StockTextView filter = null;
            if (i == 0) {
                filter = filter1;
            } else if (i == 1) {
                filter = filter2;
            } else if (i == 2) {
                filter = filter3;
            } else if (i == 3) {
                filter = filter4;
            }
            HotelViewHolder.showText(filter, filterModel.mFilteList.get(filterModel.mDefaultPosition));
        }

    }

    private void bindRankData() {
        mTitleTv.setTitle("本日融资融券前十家公司", R.style.text_18_ffffff);

        TextView name = (TextView) mStockFilterHeaderContainer.findViewById(R.id.stock_rank_header_name);
        TextView add = (TextView) mStockFilterHeaderContainer.findViewById(R.id.stock_rank_header_add);
        TextView attr1 = (TextView) mStockFilterHeaderContainer.findViewById(R.id.stock_rank_header_attr1);
        TextView attr2 = (TextView) mStockFilterHeaderContainer.findViewById(R.id.stock_rank_header_attr2);
        TextView attr3 = (TextView) mStockFilterHeaderContainer.findViewById(R.id.stock_rank_header_attr3);
        //header
        StockRankResultModel stockRankResultModel = mRankList.get(0);
        HotelViewHolder.showText(name, stockRankResultModel.mStockName);
        HotelViewHolder.showText(add, "加入自选");
        HotelViewHolder.showText(attr1, stockRankResultModel.mAttr1);
        HotelViewHolder.showText(attr2, stockRankResultModel.mAttr2);
        HotelViewHolder.showText(attr3, stockRankResultModel.mAttr3);


        mRankAdapter.notifyDataSetChanged();
    }

    private void initListener() {

    }

}
