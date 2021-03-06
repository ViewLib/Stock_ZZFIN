package com.xt.lxl.stock.page.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.listener.StockListCallBacks;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.util.DataShowUtil;
import com.xt.lxl.stock.util.HotelViewHolder;
import com.xt.lxl.stock.widget.helper.HotelLabelDrawable;
import com.xt.lxl.stock.widget.view.StockChangeText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/8/5.
 */
public class StockListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private StockListCallBacks mCallBacks;
    private ArrayList<StockViewModel> mStockList = new ArrayList<>();

    public StockListAdapter(Context context, StockListCallBacks callBacks) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mCallBacks = callBacks;
    }

    public ArrayList<StockViewModel> getStockList() {
        ArrayList<StockViewModel> stockList = new ArrayList<>();
        for (StockViewModel stockViewModel : mStockList) {
            if (stockViewModel.showType == StockViewModel.STOCK_SHOW_TYPE_NORMAL) {
                stockList.add(stockViewModel);
            }
        }
        return stockList;
    }

    @Override
    public int getCount() {
        return mStockList.size();
    }

    @Override
    public StockViewModel getItem(int position) {
        return mStockList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mStockList.get(position).hashCode();
    }

    @Override
    public int getViewTypeCount() {
        return 3;

    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        }
        if (position == mStockList.size() - 1) {
            return 2;
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == 1) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.stock_list_item_top_empty, parent, false);
            }
        } else if (itemViewType == 2) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.stock_list_item_bottom, parent, false);
            }
            convertView.findViewById(R.id.stock_item_add_stock).setOnClickListener(mCallBacks.mAddCallBack);
        } else {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.stock_list_item, parent, false);
            }
            StockViewModel item = getItem(position);
            bindData(convertView, item);
        }
        return convertView;
    }

    private void bindData(View convertView, StockViewModel stockViewModel) {
        final TextView stockName = HotelViewHolder.requestView(convertView, R.id.stock_list_item_name);
        final TextView stockCode = HotelViewHolder.requestView(convertView, R.id.stock_list_item_code);
        final TextView stockPrice = HotelViewHolder.requestView(convertView, R.id.stock_list_item_price);
//        final TextView stockChange = HotelViewHolder.requestView(convertView, R.id.stock_list_item_change);
        final TextView stockType = HotelViewHolder.requestView(convertView, R.id.stock_list_item_type);
        final StockChangeText stockChange = HotelViewHolder.requestView(convertView, R.id.stock_list_item_change);

        //展示国内海外标识
        if (StockViewModel.STOCK_TYPE_US.equals(stockViewModel.stockType)) {
            stockType.setVisibility(View.VISIBLE);
        } else {
            stockType.setVisibility(View.INVISIBLE);
        }

        String defaultStr = "数据缺失";
        HotelViewHolder.showTextOrDefault(stockName, stockViewModel.stockName, defaultStr);
        HotelViewHolder.showTextOrDefault(stockCode, stockViewModel.stockCode, defaultStr);
        HotelViewHolder.showTextOrDefault(stockPrice, stockViewModel.stockPirce, defaultStr);
        if (stockViewModel.stockState == StockViewModel.STOCK_STATE_SUSPENSION) {
//            stockChange.setText("停牌");
            stockChange.setBackgroundColor(convertView.getResources().getColor(R.color.stock_portfolio_quotation_color_gray_night));
            return;
        }
        HotelViewHolder.showTextOrDefault(stockChange, DataShowUtil.getDisplayChangeStr(stockViewModel.stockChangeD), defaultStr);
        //展示涨跌幅背景色
        HotelLabelDrawable[] drawables = DataShowUtil.transforDrawables(mInflater.getContext(), stockViewModel);
        if (drawables.length == 1) {
            stockChange.refreshCenterDrawables(drawables[0]);
        } else {
            stockChange.refreshLabelDrawables(drawables[0], drawables[1]);
        }
//        if (stockViewModel.mStockChangeD == 0) {
//            stockChange.setBackgroundColor(convertView.getResources().getColor(R.color.stock_portfolio_quotation_color_gray_night));
//        } else if (stockViewModel.mStockChangeD > 0) {
//            stockChange.setBackgroundColor(convertView.getResources().getColor(R.color.stock_portfolio_quotation_color_red_night));
//        } else {
//            stockChange.setBackgroundColor(convertView.getResources().getColor(R.color.stock_portfolio_quotation_color_green_night));
//        }
    }

    public void initOneStockViewModel(List<StockViewModel> modelList) {
        mStockList.clear();
        mStockList.add(0, new StockViewModel(StockViewModel.STOCK_SHOW_TYPE_FIRST));
        mStockList.addAll(modelList);
        mStockList.add(new StockViewModel(StockViewModel.STOCK_SHOW_TYPE_BOTTOM));
    }

    public void addOneStockViewModel(List<StockViewModel> modelList) {
        mStockList.addAll(mStockList.size() - 1, modelList);
    }

    public void addOneStockViewModel(StockViewModel model) {
        mStockList.add(mStockList.size() - 1, model);
    }


}
