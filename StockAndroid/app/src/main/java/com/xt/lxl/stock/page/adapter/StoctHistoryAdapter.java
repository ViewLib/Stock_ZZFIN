package com.xt.lxl.stock.page.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.listener.StockItemEditCallBacks;
import com.xt.lxl.stock.model.model.StockFoundRankModel;
import com.xt.lxl.stock.model.model.StockSearchModel;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.util.HotelViewHolder;
import com.xt.lxl.stock.util.DeviceUtil;
import com.xt.lxl.stock.widget.view.StockTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/8/5.
 */
public class StoctHistoryAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<StockSearchModel> mStockList = new ArrayList<>();
    private List<String> mSaveList = new ArrayList<>();
    private StockItemEditCallBacks mCallBacks;

    public StoctHistoryAdapter(Context context, StockItemEditCallBacks callBacks) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mCallBacks = callBacks;
    }

    public void setData(List<StockSearchModel> stockList) {
        mStockList = stockList;
    }

    public void setSaveList(List<String> saveList) {
        mSaveList = saveList;
    }

    @Override
    public int getCount() {
        return mStockList.size();
    }

    @Override
    public StockSearchModel getItem(int position) {
        return mStockList.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        StockSearchModel stockSearchModel = mStockList.get(position);
        if (stockSearchModel.searchType == StockSearchModel.STOCK_FOUND_TYPE_RNAK) {
            return 2;
        } else if (stockSearchModel.searchType == StockSearchModel.STOCK_FOUND_TYPE_STOCK) {
            return 1;
        }
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return mStockList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            int itemViewType = getItemViewType(position);
            if (itemViewType == StockSearchModel.STOCK_FOUND_TYPE_STOCK) {
                convertView = mInflater.inflate(R.layout.stock_item_history_stock_item, parent, false);
            } else if (itemViewType == StockSearchModel.STOCK_FOUND_TYPE_RNAK) {
                convertView = mInflater.inflate(R.layout.stock_item_history_event_item, parent, false);
            }
        }
        StockSearchModel item = getItem(position);
        if (item.searchType == StockSearchModel.STOCK_FOUND_TYPE_STOCK) {
            bindDataByStock(convertView, item.stockViewModel);
        } else if (item.searchType == StockSearchModel.STOCK_FOUND_TYPE_RNAK) {
            bindDataByEvent(convertView, item.rankModel);
        }
        return convertView;
    }


    private void bindDataByStock(View convertView, StockViewModel stockViewModel) {
        final TextView stockName = HotelViewHolder.requestView(convertView, R.id.stock_item_list_history_item_name);
        final TextView stockCode = HotelViewHolder.requestView(convertView, R.id.stock_item_list_history_item_code);
        final StockTextView stockAction = HotelViewHolder.requestView(convertView, R.id.stock_item_list_history_item_action);
        String defaultStr = "数据缺失";

        if (mSaveList.contains(stockViewModel.stockCode)) {
            stockAction.setText("已添加");
            stockAction.setCompoundDrawable(null, 0, 0, 0);
        } else {
            int pixelFromDip = DeviceUtil.getPixelFromDip(convertView.getContext(), 15);
            stockAction.setText("");
            stockAction.setCompoundDrawable(convertView.getResources().getDrawable(R.drawable.stock_history_item_add), 0, pixelFromDip, pixelFromDip);
            stockAction.setOnClickListener(mCallBacks.mActionCallBack);
        }
        stockAction.setTag(stockViewModel);
        HotelViewHolder.showTextOrDefault(stockName, stockViewModel.stockName, defaultStr);
        HotelViewHolder.showTextOrDefault(stockCode, stockViewModel.stockCode, defaultStr);
    }

    private void bindDataByEvent(View convertView, StockFoundRankModel rankModel) {
        TextView eventText = (TextView) convertView.findViewById(R.id.item_history_event_title);
        eventText.setText(rankModel.title);
    }
}
