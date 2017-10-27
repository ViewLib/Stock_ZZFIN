package com.xt.lxl.stock.page.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.model.model.StockRankFilterGroupModel;
import com.xt.lxl.stock.model.model.StockRankFilterItemModel;
import com.xt.lxl.stock.util.DeviceUtil;

import java.util.List;

/**
 * Created by xiangleiliu on 2017/10/23.
 * 分块 价值指标、股本机构、技术资金
 */
public class StockRankFilterBlockFragment extends StockRankFilterBaseFragment {
    ListView mStockRankFilterList;
    StockRankFilterBlockAdapter adapter;


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

    @Override
    protected void bindData() {
        adapter = new StockRankFilterBlockAdapter();
        adapter.setFilteList(filterGroupModel);
        mStockRankFilterList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    protected void initView(View view) {
        mStockRankFilterList = (ListView) view.findViewById(R.id.stock_rank_filter_block_list);
    }

    @Override
    protected void clearSelect() {
        filterGroupModel.clearAllSelect();
        adapter.notifyDataSetChanged();
    }

    class StockRankFilterBlockAdapter extends BaseAdapter {
        StockRankFilterGroupModel mFilterGroupModel;

        public void setFilteList(StockRankFilterGroupModel filterGroupModel) {
            mFilterGroupModel = filterGroupModel;
        }

        @Override
        public int getCount() {
            return mFilterGroupModel.filterGroupList.size();
        }

        @Override
        public StockRankFilterGroupModel getItem(int position) {
            return mFilterGroupModel.filterGroupList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.stock_rank_filter_block_item, null);
            }

            TextView stockFilterTitle = (TextView) convertView.findViewById(R.id.stock_filter_title);
            RelativeLayout filterTags = (RelativeLayout) convertView.findViewById(R.id.stock_filter_tags);

            StockRankFilterGroupModel groupModel = getItem(position);
            stockFilterTitle.setText(groupModel.groupName);

            List<StockRankFilterItemModel> filteList = groupModel.filteList;
            RelativeLayout.LayoutParams lp;

            filterTags.removeAllViews();
            if (filteList.size() >= 1) {
                StockRankFilterItemModel stockRankFilterItemModel = filteList.get(0);
                TextView textView = createTextView(stockRankFilterItemModel);
                lp = new RelativeLayout.LayoutParams(-2, -2);
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lp.addRule(RelativeLayout.CENTER_VERTICAL);
                filterTags.addView(textView, lp);
            }
            if (filteList.size() >= 2) {
                StockRankFilterItemModel stockRankFilterItemModel = filteList.get(1);
                TextView textView = createTextView(stockRankFilterItemModel);
                lp = new RelativeLayout.LayoutParams(-2, -2);
                lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                filterTags.addView(textView, lp);
            }
            if (filteList.size() >= 3) {
                StockRankFilterItemModel stockRankFilterItemModel = filteList.get(2);
                TextView textView = createTextView(stockRankFilterItemModel);
                lp = new RelativeLayout.LayoutParams(-2, -2);
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lp.addRule(RelativeLayout.CENTER_VERTICAL);
                filterTags.addView(textView, lp);
            }
            return convertView;
        }

        private TextView createTextView(StockRankFilterItemModel stockRankFilterItemModel) {
            int padding = DeviceUtil.getPixelFromDip(getContext(), 10);
            TextView textView = new TextView(getContext());
            textView.setText(stockRankFilterItemModel.filterName);
            textView.setTag(stockRankFilterItemModel);
//            textView.setBackgroundColor(Color.parseColor("#f6f6f6"));
            textView.setPadding(padding, padding, padding, padding);
            textView.setSelected(stockRankFilterItemModel.isCheck);
            textView.setBackgroundResource(R.drawable.stock_rank_filter_block_item_selector);
            textView.setTextColor(getResources().getColor(R.color.stock_rank_filter_block_text_selector));
            textView.setOnClickListener(itemListener);
            return textView;
        }
    }


    View.OnClickListener itemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            StockRankFilterItemModel stockRankFilterItemModel = (StockRankFilterItemModel) v.getTag();
            stockRankFilterItemModel.isCheck = !stockRankFilterItemModel.isCheck;
            adapter.notifyDataSetChanged();
        }
    };
}
