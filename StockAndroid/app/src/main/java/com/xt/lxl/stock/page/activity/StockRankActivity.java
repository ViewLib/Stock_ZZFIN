package com.xt.lxl.stock.page.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.listener.StockItemEditCallBacks;
import com.xt.lxl.stock.model.model.StockFoundRankModel;
import com.xt.lxl.stock.model.model.StockRankFilterGroupModel;
import com.xt.lxl.stock.model.model.StockRankFilterItemModel;
import com.xt.lxl.stock.model.model.StockRankResultModel;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.model.reponse.StockRankDetailFilterlResponse;
import com.xt.lxl.stock.model.reponse.StockRankDetailResponse;
import com.xt.lxl.stock.page.adapter.StockRankAdapter;
import com.xt.lxl.stock.page.fragment.StockRankFilterBaseFragment;
import com.xt.lxl.stock.page.fragment.StockRankFilterBlockFragment;
import com.xt.lxl.stock.page.fragment.StockRankFilterGroupFragment;
import com.xt.lxl.stock.sender.StockSender;
import com.xt.lxl.stock.util.DataSource;
import com.xt.lxl.stock.util.DeviceUtil;
import com.xt.lxl.stock.util.HotelViewHolder;
import com.xt.lxl.stock.widget.view.StockTextView;
import com.xt.lxl.stock.widget.view.StockTitleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/8/24.
 */
public class StockRankActivity extends FragmentActivity {
    public static final String STOCK_FOUND_RANK_MODEL = "STOCK_FOUND_RANK_MODEL";

    StockTitleView mTitleTv;
    LinearLayout mStockRankFilterContainer;
    LinearLayout mStockFilterHeaderContainer;
    FrameLayout mStockDetailFilterFragment;
    ListView mStockRankListView;
    StockItemEditCallBacks mCallBacks = new StockItemEditCallBacks();

    StockRankAdapter mRankAdapter;

    //数据
    public StockFoundRankModel mRankModel;
    public List<StockRankFilterGroupModel> mRankFilerList = new ArrayList<>();
    public List<StockRankResultModel> mRankList = new ArrayList<>();
    private List<String> mSaveList = new ArrayList<>();

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
        mRankModel = (StockFoundRankModel) getIntent().getExtras().getSerializable(STOCK_FOUND_RANK_MODEL);
    }

    private void bindData() {
        mSaveList.clear();
        mSaveList.addAll(DataSource.getSaveStockCodeList(this));
        mRankAdapter = new StockRankAdapter(this, mCallBacks);
        mRankAdapter.setRankResultList(mRankList);
        mRankAdapter.setSaveList(mSaveList);
        mStockRankListView.setAdapter(mRankAdapter);
        //排行
        sendRankdDetailService();
        //筛选项目列表
        sendGetFilterService();

    }

    private void sendRankdDetailService() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<StockRankFilterItemModel> searchList = new ArrayList<StockRankFilterItemModel>();
                for (StockRankFilterGroupModel groupModel : mRankFilerList) {
                    searchList.addAll(groupModel.getAllSelectItemModel());
                }
                final StockRankDetailResponse rankDetailResponse = StockSender.getInstance().requestRankDetailList(mRankModel.title, mRankModel.searchRelation, searchList);
                mHander.post(new Runnable() {
                    @Override
                    public void run() {
                        if (rankDetailResponse.resultCode != 200) {
                            return;
                        }
                        mRankList.clear();
                        mRankList.addAll(rankDetailResponse.rankResultList);
                        bindRankData();
                    }
                });
            }
        }).start();
    }

    private void sendGetFilterService() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //赋值逻辑
                final StockRankDetailFilterlResponse filterlResponse = StockSender.getInstance().requestFilterList();
                if (filterlResponse == null) {
                    return;
                }
                mRankFilerList.clear();
                mRankFilerList.addAll(filterlResponse.rankFilterList);
                mHander.post(new Runnable() {
                    @Override
                    public void run() {
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
        mStockDetailFilterFragment = (FrameLayout) findViewById(R.id.stock_detail_filter_fragment);
    }

    private void bindFilterData() {
        StockTextView filter1 = (StockTextView) mStockRankFilterContainer.findViewById(R.id.filter1);
        StockTextView filter2 = (StockTextView) mStockRankFilterContainer.findViewById(R.id.filter2);
        StockTextView filter3 = (StockTextView) mStockRankFilterContainer.findViewById(R.id.filter3);
        StockTextView filter4 = (StockTextView) mStockRankFilterContainer.findViewById(R.id.filter4);

        for (int i = 0; i < mRankFilerList.size(); i++) {
            StockRankFilterGroupModel filterModel = mRankFilerList.get(i);
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
            if (filter == null) {
                return;
            }
            HotelViewHolder.showText(filter, filterModel.groupName);
            //设置样式和监听
            int height = DeviceUtil.getPixelFromDip(this, 7);
            int width = DeviceUtil.getPixelFromDip(this, 11);
            if (filterModel.filterGroupList.size() > 0) {
                //设置监听
                filter.setTag(filterModel);
                filter.setOnClickListener(mCallBacks.mFilterCallBack);
                filter.setTextAppearance(StockRankActivity.this, R.style.text_13_186db7);
                filter.setCompoundDrawable(getResources().getDrawable(R.drawable.stock_rank_filter_enable), 2, width, height);
            } else {
                filter.setTextAppearance(StockRankActivity.this, R.style.text_13_6c6c6c);
                filter.setCompoundDrawable(getResources().getDrawable(R.drawable.stock_rank_filter_disable), 2, width, height);
            }
        }

    }

    private void bindRankData() {
        mTitleTv.setTitle(mRankModel.title, R.style.text_18_ffffff);

        TextView name = (TextView) mStockFilterHeaderContainer.findViewById(R.id.stock_rank_header_name);
        TextView add = (TextView) mStockFilterHeaderContainer.findViewById(R.id.stock_rank_header_add);
        TextView attr1 = (TextView) mStockFilterHeaderContainer.findViewById(R.id.stock_rank_header_attr1);
        TextView attr2 = (TextView) mStockFilterHeaderContainer.findViewById(R.id.stock_rank_header_attr2);
        TextView attr3 = (TextView) mStockFilterHeaderContainer.findViewById(R.id.stock_rank_header_attr3);

        if (mRankList.size() == 0) {
            return;
        }
        //header
        StockRankResultModel stockRankResultModel = mRankList.get(0);
        HotelViewHolder.showText(name, stockRankResultModel.stockName);
        HotelViewHolder.showText(add, "加入自选");
        HotelViewHolder.showText(attr1, stockRankResultModel.attr1);
        HotelViewHolder.showText(attr2, stockRankResultModel.attr2);
        HotelViewHolder.showText(attr3, stockRankResultModel.attr3);

        mStockFilterHeaderContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int nameWidth = 0;
                int addWidth = 0;
                int attr1Width = 0;
                int attr2Width = 0;
                int attr3Width = 0;
                for (int i = 0; i < mStockFilterHeaderContainer.getChildCount(); i++) {
                    View childAt = mStockFilterHeaderContainer.getChildAt(i);
                    if (i == 0) {
                        nameWidth = childAt.getWidth();
                    } else if (i == 1) {
                        addWidth = childAt.getWidth();
                    } else if (i == 2) {
                        attr1Width = childAt.getWidth();
                    } else if (i == 3) {
                        attr2Width = childAt.getWidth();
                    } else if (i == 4) {
                        attr3Width = childAt.getWidth();
                    }
                }

                if (!checkValue(nameWidth, addWidth, attr1Width, attr2Width, attr3Width)) {
                    return;
                }
                mWidthMap.put(0, nameWidth);
                mWidthMap.put(1, addWidth);
                mWidthMap.put(2, attr1Width);
                mWidthMap.put(3, attr2Width);
                mWidthMap.put(4, attr3Width);

                mRankAdapter.setChildWidthValue(mWidthMap);
                //设置各行各列的宽度
                mRankAdapter.notifyDataSetChanged();
            }
        });
    }

    HashMap<Integer, Integer> mWidthMap = new HashMap<>();

    private boolean checkValue(int nameWidth, int addWidth, int attr1Width, int attr2Width, int attr3Width) {
        boolean isChange = false;
        if (mWidthMap.get(0) == null || nameWidth != mWidthMap.get(0)) {
            isChange = true;
        }
        if (mWidthMap.get(1) == null || addWidth != mWidthMap.get(1)) {
            isChange = true;
        }
        if (mWidthMap.get(2) == null || attr1Width != mWidthMap.get(2)) {
            isChange = true;
        }
        if (mWidthMap.get(3) == null || attr2Width != mWidthMap.get(3)) {
            isChange = true;
        }
        if (mWidthMap.get(4) == null || attr3Width != mWidthMap.get(4)) {
            isChange = true;
        }
        return isChange;
    }


    private void initListener() {
        mCallBacks.mActionCallBack = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!(v.getTag() instanceof StockViewModel)) {
                    return;
                }
                StockViewModel model = (StockViewModel) v.getTag();
                //添加操作
                mSaveList.add(model.stockCode);
                DataSource.addStockCode(StockRankActivity.this, model.stockCode);
                mRankAdapter.notifyDataSetChanged();
            }
        };

        mCallBacks.mFilterCallBack = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StockRankFilterGroupModel subGroupModel = (StockRankFilterGroupModel) v.getTag();
                int id = v.getId();
                StockRankFilterBaseFragment fragment = null;
                if (id == R.id.filter1) {
                    fragment = new StockRankFilterGroupFragment();
                } else if (id == R.id.filter2) {
                    fragment = new StockRankFilterBlockFragment();
                } else if (id == R.id.filter3) {
                    fragment = new StockRankFilterBlockFragment();
                } else if (id == R.id.filter4) {
                    fragment = new StockRankFilterBlockFragment();
                }
                if (fragment == null) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(StockRankFilterBaseFragment.StockRankFilterGroupModelTag, subGroupModel);
                fragment.setArguments(bundle);
//                findViewById(R.id.stock_detail_filter_fragment).setVisibility(View.VISIBLE);
                FragmentManager supportFragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.stock_detail_filter_fragment, fragment, "filter");
                fragmentTransaction.commitAllowingStateLoss();
            }
        };
    }

    public void onReceiveResult(int requestCode, int index, StockRankFilterGroupModel topgroupModel) {
        if (StockRankFilterBaseFragment.FilterFragmentCode == requestCode) {
            if (index > 4) {
                return;
            }
            StockRankFilterGroupModel groupModel = mRankFilerList.get(index);
//            groupModel.filterGroupList.clear();
//            groupModel.filterGroupList.addAll(topgroupModel.filterGroupList);
            sendRankdDetailService();
        }
    }
}
