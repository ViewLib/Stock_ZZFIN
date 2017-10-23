package com.xt.lxl.stock.page.module;

import android.view.View;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.util.StringUtil;
import com.xt.lxl.stock.viewmodel.StockDetailCacheBean;
import com.xt.lxl.stock.widget.view.StockDetailShowText;

/**
 * Created by xiangleiliu on 2017/10/20.
 */
public class StockDetailInfoModule extends StockDetailBaseModule {

    TextView privceTv;
    TextView changePriceValueTv;
    TextView changePriceRatioTv;
    TextView stockAdd;

    StockDetailShowText priceTop;
    StockDetailShowText priceBottom;
    StockDetailShowText upAndDown;
    StockDetailShowText rate;
    StockDetailShowText turnover;
    StockDetailShowText marketvalue;

    public StockDetailInfoModule(StockDetailCacheBean cacheBean) {
        super(cacheBean);
    }

    public void initModuleView(View view) {
        privceTv = (TextView) view.findViewById(R.id.stock_detail_price_tv);
        changePriceValueTv = (TextView) view.findViewById(R.id.stock_detail_change_pricevalue_tv);
        changePriceRatioTv = (TextView) view.findViewById(R.id.stock_detail_change_priceratio_tv);
        stockAdd = (TextView) view.findViewById(R.id.stock_detail_add);

        priceTop = (StockDetailShowText) view.findViewById(R.id.stock_detail_price_top);
        priceBottom = (StockDetailShowText) view.findViewById(R.id.stock_detail_price_bottom);
        upAndDown = (StockDetailShowText) view.findViewById(R.id.stock_detail_upanddown);
        rate = (StockDetailShowText) view.findViewById(R.id.stock_detail_rate);
        turnover = (StockDetailShowText) view.findViewById(R.id.stock_detail_turnover);
        marketvalue = (StockDetailShowText) view.findViewById(R.id.stock_detail_marketvalue);
    }

    public void bindData() {
        StockViewModel stockViewModel = mCacheBean.mStockViewModel;
        if (StringUtil.emptyOrNull(stockViewModel.stockCode)) {
            privceTv.setText("暂无价格");
            changePriceValueTv.setText("暂无数据");
            changePriceRatioTv.setText("暂无数据");
            priceTop.setTextValue("今日最高", "暂无");
            priceBottom.setTextValue("今日最低", "暂无");
            upAndDown.setTextValue("今日振幅", "暂无");
            rate.setTextValue("换手率", "暂无");
            turnover.setTextValue("成交量", "暂无");
            marketvalue.setTextValue("市值", "暂无");
        } else {
            privceTv.setText(stockViewModel.stockPirce);
            changePriceValueTv.setText(stockViewModel.stockChangeValue);
            changePriceRatioTv.setText(stockViewModel.stockChange + "%");//需要颜色的
            if (stockViewModel.stockChangeValue.startsWith("-")) {
                privceTv.setTextAppearance(mContainer.getContext(), R.style.text_22_006400);
                changePriceValueTv.setTextAppearance(mContainer.getContext(), R.style.text_14_006400);
                changePriceRatioTv.setTextAppearance(mContainer.getContext(), R.style.text_14_006400);
            } else {
                privceTv.setTextAppearance(mContainer.getContext(), R.style.text_22_fe2a32);
                changePriceValueTv.setTextAppearance(mContainer.getContext(), R.style.text_14_fe2a32);
                changePriceRatioTv.setTextAppearance(mContainer.getContext(), R.style.text_14_fe2a32);
            }

            priceTop.setTextValue("今日最高", stockViewModel.maxPrice);
            priceBottom.setTextValue("今日最低", stockViewModel.minPrice);
            upAndDown.setTextValue("今日振幅", stockViewModel.amplitude + "%");
            rate.setTextValue("换手率", stockViewModel.turnover + "%");
            turnover.setTextValue("成交量", stockViewModel.volume + "手");
            marketvalue.setTextValue("市值", stockViewModel.valueAll + "亿");
        }
    }

}
