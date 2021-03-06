package com.xt.lxl.stock.page.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.model.model.StockRankFilterGroupModel;
import com.xt.lxl.stock.model.model.StockRankFilterItemModel;
import com.xt.lxl.stock.page.activity.StockRankActivity;

import java.util.ArrayList;

/**
 * Created by xiangleiliu on 2017/10/23.
 */
public abstract class StockRankFilterBaseFragment extends Fragment implements View.OnClickListener {

    public static final String StockRankFilterGroupModelTag = "StockRankFilterGroupModelTag";
    public static final String SelectItemIndexTag = "SelectItemIndexTag";
    public static final int FilterFragmentCode = 123456;

    StockRankFilterGroupModel filterGroupModel;
    int index;
    View mFilterClear;
    View mFilterOK;

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
        mFilterClear = view.findViewById(R.id.filter_clear);
        mFilterOK = view.findViewById(R.id.filter_ok);
        if (mFilterClear != null && mFilterOK != null) {
            mFilterClear.setOnClickListener(this);
            mFilterOK.setOnClickListener(this);
        }
        initData();
        initView(view);
        bindData();
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.filter_clear) {
            //清空筛选
            clearSelect();
        } else if (v.getId() == R.id.filter_ok) {
            submitFilter();
            closeFragment();
        } else {
            closeFragment();
        }
    }

    private void closeFragment() {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void submitFilter() {
        ((StockRankActivity) getActivity()).onReceiveResult(FilterFragmentCode, index, filterGroupModel);
    }


    protected void initData() {
        filterGroupModel = (StockRankFilterGroupModel) getArguments().getSerializable(StockRankFilterGroupModelTag);
        index = getArguments().getInt(SelectItemIndexTag);
    }

    protected abstract void bindData();

    protected abstract void initView(View view);

    protected abstract void clearSelect();

}
