package com.xt.lxl.stock.page.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.listener.StockListCallBacks;
import com.xt.lxl.stock.model.model.StockViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/11/19.
 */
public class StockEditAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<StockViewModel> mStockList = new ArrayList<>();
    private List<String> selectList = new ArrayList<>();

    public StockEditAdapter(Context context, StockListCallBacks callBacks) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        this.mCallBacks = callBacks;
    }

    public void setStockList(List<StockViewModel> stockList) {
        mStockList = stockList;
//        for (StockViewModel stockViewModel : mStockList) {
//            selectList.add(stockViewModel.stockCode);
//        }
//        mSaveList = saveList;
    }

    @Override
    public int getCount() {
        return mStockList.size();
    }

    @Override
    public StockViewModel getItem(int position) {
        StockViewModel stockViewModel = mStockList.get(position);
        return stockViewModel;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.stock_edit_list_item, parent, false);
        }
        StockViewModel item = getItem(position);
        bindData(convertView, item);
        return convertView;
    }

    private void bindData(View convertView, final StockViewModel stockViewModel) {
        final ImageView itemIcon = (ImageView) convertView.findViewById(R.id.stock_edit_item_icon);
        TextView itemName = (TextView) convertView.findViewById(R.id.stock_edit_item_name);
        TextView itemCode = (TextView) convertView.findViewById(R.id.stock_edit_item_code);
        itemName.setText(stockViewModel.stockName);
        itemCode.setText(stockViewModel.stockCode);
        itemIcon.setSelected(selectList.contains(stockViewModel.stockCode));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectList.contains(stockViewModel.stockCode)) {
                    itemIcon.setSelected(false);
                    selectList.remove(stockViewModel.stockCode);
                } else {
                    itemIcon.setSelected(true);
                    selectList.add(stockViewModel.stockCode);
                }
            }
        });
    }

    public List<String> getSelectList() {
        return selectList;
    }


}
