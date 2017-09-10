package com.xt.lxl.stock.page.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.util.DataSource;
import com.xt.lxl.stock.widget.helper.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/9/2.
 */
public class StockMainFoundFragment extends Fragment {

    TextView mRankTitleText;
    RecyclerView mRankContainer;
    FoundRankAdapter mAdapter;
    List<String> mDatas = new ArrayList();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stock_main_found, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView(view);
        initListener();
        bindData();
    }

    private void initData() {
        mDatas.clear();
        mDatas.addAll(DataSource.getRankList(getContext()));
    }

    private void initView(View view) {
//        mRankTitleText = (TextView) view.findViewById(R.id.stock_found_rank_title_text);
        mRankContainer = (RecyclerView) view.findViewById(R.id.stock_found_rank_container);
    }

    private void initListener() {

    }

    private void bindData() {
        mAdapter = new FoundRankAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRankContainer.setLayoutManager(gridLayoutManager);
        mRankContainer.setHasFixedSize(true);
        mRankContainer.setAdapter(mAdapter);
        mRankContainer.addItemDecoration(new DividerGridItemDecoration(getContext(), getResources().getDrawable(R.drawable.stock_divider)));
        mAdapter.notifyDataSetChanged();
    }

    class FoundRankAdapter extends RecyclerView.Adapter<FoundRankAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    getActivity()).inflate(R.layout.stock_found_rank_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            String s = mDatas.get(position);
            holder.mRankText.setText(s);
        }


        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView mRankText;
            ImageView mRankType;

            public MyViewHolder(View view) {
                super(view);
                mRankText = (TextView) view.findViewById(R.id.stock_found_rank_text);
                mRankType = (ImageView) view.findViewById(R.id.stock_found_rank_type_bg);
            }
        }
    }


}
