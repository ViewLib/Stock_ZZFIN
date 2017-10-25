package com.xt.lxl.stock.page.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.model.model.StockRankFilterGroupModel;

/**
 * Created by xiangleiliu on 2017/10/23.
 */
public abstract class StockRankFilterBaseFragment extends Fragment implements View.OnClickListener {

    public static final String StockRankFilterGroupModelTag = "StockRankFilterGroupModel";

    StockRankFilterGroupModel filterGroupModel;

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
        initData();
        initView(view);
        bindData();
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.commitAllowingStateLoss();
    }

    protected void initData() {
        filterGroupModel = (StockRankFilterGroupModel) getArguments().get(StockRankFilterGroupModelTag);
    }

    protected abstract void bindData();

    protected abstract void initView(View view);
}
