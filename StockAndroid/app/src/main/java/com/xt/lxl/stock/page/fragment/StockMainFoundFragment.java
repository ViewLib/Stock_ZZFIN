package com.xt.lxl.stock.page.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.model.model.StockFoundRankModel;
import com.xt.lxl.stock.page.activity.StockRankActivity;
import com.xt.lxl.stock.util.DataSource;
import com.xt.lxl.stock.util.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/9/2.
 */
public class StockMainFoundFragment extends Fragment {

    TextView mRankTitleText;
    ImageView top10;
    RecyclerView mRankContainer;
    FoundRankAdapter mAdapter;
    List<StockFoundRankModel> mDatas = new ArrayList<>();

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
        bindData();
        initListener();
    }

    private void initData() {
        mDatas.clear();
        mDatas.addAll(DataSource.getRankList(getContext()));
    }

    private void initView(View view) {
//        mRankTitleText = (TextView) view.findViewById(R.id.stock_found_rank_title_text);
        mRankContainer = (RecyclerView) view.findViewById(R.id.stock_found_rank_container);
        top10 = (ImageView) view.findViewById(R.id.stock_main_found_top10);
    }

    private void initListener() {
        mAdapter.setItemOnClickCallBack(new ItemOnClickCallBack() {
            @Override
            public void onItemClick(View view, int position) {
                StockFoundRankModel rankModel = (StockFoundRankModel) view.getTag();
                gotoStockRankPage(rankModel);
            }
        });
    }

    private void bindData() {
        mAdapter = new FoundRankAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRankContainer.setLayoutManager(gridLayoutManager);
        mRankContainer.setHasFixedSize(true);
        mRankContainer.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 跳转股票排行界面
     */
    private void gotoStockRankPage(StockFoundRankModel rankModel) {
        Intent intent = new Intent(getActivity(), StockRankActivity.class);
        startActivity(intent);
    }

    class FoundRankAdapter extends RecyclerView.Adapter<FoundRankAdapter.MyViewHolder> {

        ItemOnClickCallBack mItemOnClickCallBack;

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    getActivity()).inflate(R.layout.stock_found_rank_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            StockFoundRankModel stockFoundRankModel = mDatas.get(position);
            holder.mRankText.setText(stockFoundRankModel.title);
            GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) holder.mView.getLayoutParams();
            int margin = DeviceUtil.getPixelFromDip(getContext(), 16);
            layoutParams.setMargins(0, margin, position % 2 == 0 ? margin : 0, 0);
            if (mItemOnClickCallBack != null) {
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemOnClickCallBack.onItemClick(holder.mView, position);
                    }
                });
                holder.mView.setTag(stockFoundRankModel);
            }
        }


        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        public void setItemOnClickCallBack(ItemOnClickCallBack itemOnClickCallBack) {
            mItemOnClickCallBack = itemOnClickCallBack;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout mView;
            TextView mRankText;
            ImageView mRankType;

            public MyViewHolder(View view) {
                super(view);
                mView = (RelativeLayout) view;
                mRankText = (TextView) view.findViewById(R.id.stock_found_rank_text);
                mRankType = (ImageView) view.findViewById(R.id.stock_found_rank_type_bg);
            }
        }
    }

    public interface ItemOnClickCallBack {
        void onItemClick(View view, int position);
    }
}
