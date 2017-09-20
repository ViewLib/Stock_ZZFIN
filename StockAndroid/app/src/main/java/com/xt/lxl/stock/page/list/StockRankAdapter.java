package com.xt.lxl.stock.page.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.listener.StockListCallBacks;
import com.xt.lxl.stock.model.model.StockRankResultModel;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.util.DataShowUtil;
import com.xt.lxl.stock.util.HotelViewHolder;
import com.xt.lxl.stock.util.StockShowUtil;
import com.xt.lxl.stock.widget.helper.HotelLabelDrawable;
import com.xt.lxl.stock.widget.view.StockChangeText;
import com.xt.lxl.stock.widget.view.StockTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/8/5.
 */
public class StockRankAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private StockListCallBacks mCallBacks;
    private int mColum = 5;//行数
    private List<StockRankResultModel> mStockList = new ArrayList<>();
    private List<String> mSaveList = new ArrayList<>();

    public StockRankAdapter(Context context, StockListCallBacks callBacks) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mCallBacks = callBacks;
    }

    @Override
    public int getCount() {
        return mStockList.size();
    }

    @Override
    public StockRankResultModel getItem(int position) {
        return mStockList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mStockList.get(position).hashCode();
    }

    @Override
    public int getViewTypeCount() {
        return 2;

    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == 1) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.stock_list_item_top_empty, parent, false);
                convertView.getLayoutParams().height = StockShowUtil.getPixelFromDip(convertView.getContext(), 30);
            }
        } else {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.stock_rank_history_item, parent, false);
            }
            bindData(convertView, getItem(position), position);
        }
        return convertView;
    }

    private void bindData(View convertView, StockRankResultModel stockViewModel, int position) {

        final TextView stockPosition = HotelViewHolder.requestView(convertView, R.id.stock_rank_position);
        final TextView stockName = HotelViewHolder.requestView(convertView, R.id.stock_rank_stockname);
        final TextView stockCode = HotelViewHolder.requestView(convertView, R.id.stock_rank_stockcode);
        final StockTextView stockAction = HotelViewHolder.requestView(convertView, R.id.stock_rank_action);
        final TextView attr1 = HotelViewHolder.requestView(convertView, R.id.stock_rank_attr1);
        final TextView attr2 = HotelViewHolder.requestView(convertView, R.id.stock_rank_attr2);
        final TextView attr3 = HotelViewHolder.requestView(convertView, R.id.stock_rank_attr3);

        String positionStr = String.valueOf(position);
        if (positionStr.length() == 1) {
            positionStr = "0" + positionStr;
        }
        stockPosition.setText(positionStr);
        stockName.setText(stockViewModel.mStockName);
        stockCode.setText(stockViewModel.mStockCode);

        if (mSaveList.contains(stockViewModel.mStockCode)) {
            stockAction.setText("已添加");
            stockAction.setCompoundDrawable(null);
        } else {
            stockAction.setText("");
            int pixelFromDip = StockShowUtil.getPixelFromDip(convertView.getContext(), 15);
            stockAction.setCompoundDrawable(convertView.getResources().getDrawable(R.drawable.stock_history_item_add), 0, pixelFromDip, pixelFromDip);
        }

        attr1.setVisibility(View.GONE);
        attr2.setVisibility(View.GONE);
        attr3.setVisibility(View.GONE);

        if (mColum == 2) {
            return;
        }

        if (mColum >= 3) {
            attr1.setVisibility(View.VISIBLE);
            attr1.setText(stockViewModel.mAttr1);
        }

        if (mColum >= 4) {
            attr2.setVisibility(View.VISIBLE);
            attr2.setText(stockViewModel.mAttr2);
        }

        if (mColum >= 5) {
            attr3.setVisibility(View.VISIBLE);
            attr3.setText(stockViewModel.mAttr3);
        }

    }

    public void setRankResultList(List<StockRankResultModel> modelList) {
        mStockList = modelList;
    }

    public void setSaveList(List<String> saveList) {
        mSaveList = saveList;
    }

    public void setColum(int colum) {
        this.mColum = colum;
    }


}
