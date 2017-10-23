package com.xt.lxl.stock.page.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.model.model.StockRankFilterGroupModel;
import com.xt.lxl.stock.model.model.StockRankFilterItemModel;

import java.util.List;

/**
 * Created by xiangleiliu on 2017/10/23.
 */
public class StockRankFilterFragment extends Fragment {

    public static final String StockRankFilterGroupModel = "StockRankFilterGroupModel";

    LinearLayout mStockRankFilterLayout;

    StockRankFilterGroupModel filterGroupModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stock_rank_filter_layout, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView(view);
        bindData();
    }

    private void initData() {
        filterGroupModel = (StockRankFilterGroupModel) getArguments().get(StockRankFilterGroupModel);
    }

    private void bindData() {
        mStockRankFilterLayout.removeAllViews();
        List<StockRankFilterItemModel> filteList = filterGroupModel.filteList;
        for (StockRankFilterItemModel stockRankFilterItemModel : filteList) {
            TextView textView = new TextView(getContext());
            textView.setText(stockRankFilterItemModel.filterName);
            textView.setTag(stockRankFilterItemModel);
            mStockRankFilterLayout.addView(textView);
        }
    }

    private void initView(View view) {
        mStockRankFilterLayout = (LinearLayout) view.findViewById(R.id.stock_rank_filter_layout);
    }
}
