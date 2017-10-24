package com.xt.lxl.stock.page.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.xt.lxl.stock.R;

/**
 * Created by xiangleiliu on 2017/10/23.
 * 分块 价值指标、股本机构、技术资金
 */
public class StockRankFilterBlockFragment extends StockRankFilterBaseFragment {
    ListView mStockRankFilterList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stock_rank_filter_block_layout, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        bindData();
    }

    protected void bindData() {
//        mStockRankFilterLayout.removeAllViews();
//        List<StockRankFilterItemModel> filteList = filterGroupModel.filteList;
//        for (StockRankFilterItemModel stockRankFilterItemModel : filteList) {
//            TextView textView = new TextView(getContext());
//            textView.setText(stockRankFilterItemModel.filterName);
//            textView.setTag(stockRankFilterItemModel);
//            mStockRankFilterLayout.addView(textView);
//        }
    }

    protected void initView(View view) {
        mStockRankFilterList = (ListView) view.findViewById(R.id.stock_rank_filter_block_layout);
    }
}
