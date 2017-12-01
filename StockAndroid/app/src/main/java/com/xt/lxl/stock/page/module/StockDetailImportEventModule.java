package com.xt.lxl.stock.page.module;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.listener.StockDetailListener;
import com.xt.lxl.stock.model.model.StockEventsDataList;
import com.xt.lxl.stock.model.model.StockEventsDataModel;
import com.xt.lxl.stock.viewmodel.StockDetailCacheBean;
import com.xt.lxl.stock.widget.view.StockTextView;

import java.util.List;

/**
 * Created by Administrator on 2017/10/19 0019.
 */

public class StockDetailImportEventModule extends StockDetailBaseModule implements View.OnClickListener {

    private StockTextView mEventTitle;
    private TextView mLookMore;//查看更多
    private LinearLayout mEventContainer;//问题列表


    public StockDetailImportEventModule(StockDetailCacheBean cacheBean, StockDetailListener listener) {
        super(cacheBean, listener);
    }

    @Override
    public void initModuleView(View view) {
        mEventTitle = (StockTextView) view.findViewById(R.id.stock_important_event_title);
        mEventContainer = (LinearLayout) view.findViewById(R.id.stock_important_event_list);
        mLookMore = (TextView) view.findViewById(R.id.stock_detail_event_lookmore);

    }

    @Override
    public void bindData() {
        List<StockEventsDataList> stockEventsDataLists = mCacheBean.eventsDataResponse.stockEventsDataLists;//每种类型取第一个展示
        bindEventDataList(stockEventsDataLists, true);
        mLookMore.setOnClickListener(this);
    }


    private void bindEventDataList(List<StockEventsDataList> stockEventsDataLists, boolean isShowMore) {
        List<StockEventsDataList> showList;
        if (stockEventsDataLists.size() <= 3) {
            isShowMore = false;
        }
        if (isShowMore) {
            showList = stockEventsDataLists.subList(0, 3);
        } else {
            showList = stockEventsDataLists;
        }
        mLookMore.setVisibility(isShowMore ? View.VISIBLE : View.GONE);
        mEventContainer.removeAllViews();
        for (StockEventsDataList dataList : showList) {
            bindItemEventModel(dataList);
        }
        if (isShowMore && mEventContainer.getChildCount() > 3) {
            mLookMore.setVisibility(View.GONE);
        }
    }

    public void bindItemEventModel(StockEventsDataList eventsDataList) {
        if (eventsDataList.stockEventsDataModels.size() == 0) {
            return;
        }
        StockEventsDataModel eventsDataModel = eventsDataList.stockEventsDataModels.get(0);
        View inflate = View.inflate(mContainer.getContext(), R.layout.stock_detail_important_event_item, null);
        TextView titleTv = (TextView) inflate.findViewById(R.id.stock_detail_important_title);
        TextView descTv = (TextView) inflate.findViewById(R.id.stock_detail_important_desc);
        titleTv.setText(eventsDataModel.eventTitle);
        descTv.setText(eventsDataModel.eventDesc);
        mEventContainer.addView(inflate);
    }


    @Override
    public void onClick(View v) {
        bindEventDataList(mCacheBean.eventsDataResponse.stockEventsDataLists, false);
    }
}
