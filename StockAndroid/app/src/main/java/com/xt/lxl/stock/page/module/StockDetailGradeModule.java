package com.xt.lxl.stock.page.module;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.listener.StockDetailListener;
import com.xt.lxl.stock.model.model.StockDetailGradleModel;
import com.xt.lxl.stock.viewmodel.StockDetailCacheBean;
import com.xt.lxl.stock.widget.view.StockTextView;

import java.util.List;

/**
 * Created by Administrator on 2017/10/19 0019.
 * 券商评级
 */

public class StockDetailGradeModule extends StockDetailBaseModule {

    private StockTextView mTitle;
    private LinearLayout mContainer;


    public StockDetailGradeModule(StockDetailCacheBean cacheBean, StockDetailListener listener) {
        super(cacheBean, listener);
    }

    @Override
    public void initModuleView(View view) {
        mTitle = (StockTextView) view.findViewById(R.id.stock_detail_grade_title);
        mContainer = (LinearLayout) view.findViewById(R.id.stock_detail_grade_container);
    }

    @Override
    public void bindData() {
        List<StockDetailGradleModel> gradleModelList = mCacheBean.gradleModelList;
        mContainer.removeAllViews();
        for (int i = -1; i < gradleModelList.size(); i++) {
            View inflate = View.inflate(mContainer.getContext(), R.layout.stock_detail_home_grade_item, null);
            TextView nameTv = (TextView) inflate.findViewById(R.id.stock_detail_grade_name);
            TextView levelTv = (TextView) inflate.findViewById(R.id.stock_detail_grade_level);
            TextView priceTv = (TextView) inflate.findViewById(R.id.stock_detail_grade_price);
            TextView dateTv = (TextView) inflate.findViewById(R.id.stock_detail_grade_date);
            if (i == -1) {
                inflate.setBackgroundColor(Color.parseColor("#DEEBF6"));
                nameTv.setText("券商名称");
                levelTv.setText("评级");
                priceTv.setText("目标价格");
                dateTv.setText("评级日期");

                nameTv.setTextAppearance(mContainer.getContext(), R.style.text_13_186db7);
                levelTv.setTextAppearance(mContainer.getContext(), R.style.text_13_186db7);
                priceTv.setTextAppearance(mContainer.getContext(), R.style.text_13_186db7);
                dateTv.setTextAppearance(mContainer.getContext(), R.style.text_13_186db7);
            } else {
                inflate.setBackgroundColor(Color.parseColor(i % 2 == 0 ? "#ffffff" : "#EDF3F8"));
                StockDetailGradleModel stockDetailGradleModel = gradleModelList.get(i);
                nameTv.setText(stockDetailGradleModel.stockBrokerName);
                levelTv.setText("买入");
                priceTv.setText(stockDetailGradleModel.showPrice);
                dateTv.setText(stockDetailGradleModel.dateStr);

                nameTv.setTextAppearance(mContainer.getContext(), R.style.text_12_484848);
                levelTv.setTextAppearance(mContainer.getContext(), R.style.text_12_484848);
                priceTv.setTextAppearance(mContainer.getContext(), R.style.text_12_484848);
                dateTv.setTextAppearance(mContainer.getContext(), R.style.text_12_484848);
            }
            mContainer.addView(inflate);
        }
    }
}
