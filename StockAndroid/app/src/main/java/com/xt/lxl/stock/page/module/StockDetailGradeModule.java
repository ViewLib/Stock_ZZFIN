package com.xt.lxl.stock.page.module;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.widget.view.StockTextView;

/**
 * Created by Administrator on 2017/10/19 0019.
 * 券商评级
 */

public class StockDetailGradeModule extends StockDetailBaseModule {

    private StockTextView mEventTitle;
    private TextView mLookMore;//查看更多
    private LinearLayout mEventContainer;//问题列表


    public StockDetailGradeModule(StockViewModel stockViewModel) {
        super(stockViewModel);
    }

    @Override
    public void setModuleView(View view) {
        mEventTitle = (StockTextView) view.findViewById(R.id.stock_important_event_title);
        mEventContainer = (LinearLayout) view.findViewById(R.id.stock_important_event_list);
        mLookMore = (TextView) view.findViewById(R.id.stock_detail_event_lookmore);
    }

    @Override
    public void bindData(StockViewModel stockViewModel) {

    }
}
