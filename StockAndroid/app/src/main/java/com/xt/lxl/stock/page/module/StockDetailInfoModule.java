package com.xt.lxl.stock.page.module;

import android.view.View;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.listener.StockDetailListener;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.util.DeviceUtil;
import com.xt.lxl.stock.util.StockUtil;
import com.xt.lxl.stock.util.StringUtil;
import com.xt.lxl.stock.viewmodel.StockDetailCacheBean;
import com.xt.lxl.stock.widget.view.StockDetailShowText;
import com.xt.lxl.stock.widget.view.StockTextView;

/**
 * Created by xiangleiliu on 2017/10/20.
 */
public class StockDetailInfoModule extends StockDetailBaseModule {

    TextView privceTv;
    TextView changePriceValueTv;
    TextView changePriceRatioTv;
    StockTextView stockAdd;

    StockDetailShowText priceTop;
    StockDetailShowText priceBottom;
    StockDetailShowText upAndDown;
    StockDetailShowText rate;
    StockDetailShowText turnover;
    StockDetailShowText marketvalue;

    public StockDetailInfoModule(StockDetailCacheBean cacheBean, StockDetailListener listener) {
        super(cacheBean, listener);
    }

    public void initModuleView(View view) {
        privceTv = (TextView) view.findViewById(R.id.stock_detail_price_tv);
        changePriceValueTv = (TextView) view.findViewById(R.id.stock_detail_change_pricevalue_tv);
        changePriceRatioTv = (TextView) view.findViewById(R.id.stock_detail_change_priceratio_tv);
        stockAdd = (StockTextView) view.findViewById(R.id.stock_detail_add);

        priceTop = (StockDetailShowText) view.findViewById(R.id.stock_detail_price_top);
        priceBottom = (StockDetailShowText) view.findViewById(R.id.stock_detail_price_bottom);
        upAndDown = (StockDetailShowText) view.findViewById(R.id.stock_detail_upanddown);
        rate = (StockDetailShowText) view.findViewById(R.id.stock_detail_rate);
        turnover = (StockDetailShowText) view.findViewById(R.id.stock_detail_turnover);
        marketvalue = (StockDetailShowText) view.findViewById(R.id.stock_detail_marketvalue);
    }

    public void bindData() {
        StockViewModel stockViewModel = mCacheBean.mStockViewModel;
        if (StringUtil.emptyOrNull(stockViewModel.stockCode) || stockViewModel.isDelisting) {
            privceTv.setText("暂无价格");
            changePriceValueTv.setText("暂无数据");
            changePriceRatioTv.setText("暂无数据");
            priceTop.setTextValue("今日最高", "暂无");
            priceBottom.setTextValue("今日最低", "暂无");
            upAndDown.setTextValue("今年涨幅", "暂无");
            rate.setTextValue("换手率", "暂无");
            turnover.setTextValue("成交量", "暂无");
            marketvalue.setTextValue("市值", "暂无");
        } else {
            privceTv.setText(stockViewModel.stockPirce);
            changePriceRatioTv.setText(stockViewModel.stockChange + "%");//需要颜色的
            if (stockViewModel.stockChangeValue.startsWith("-")) {
                changePriceValueTv.setText(stockViewModel.stockChangeValue);
                privceTv.setTextAppearance(mContainer.getContext(), R.style.text_22_006400);
                changePriceValueTv.setTextAppearance(mContainer.getContext(), R.style.text_14_006400);
                changePriceRatioTv.setTextAppearance(mContainer.getContext(), R.style.text_14_006400);
            } else {
                privceTv.setTextAppearance(mContainer.getContext(), R.style.text_22_fe2a32);
                changePriceValueTv.setTextAppearance(mContainer.getContext(), R.style.text_14_fe2a32);
                changePriceRatioTv.setTextAppearance(mContainer.getContext(), R.style.text_14_fe2a32);
                changePriceValueTv.setText("+" + stockViewModel.stockChangeValue);
            }

            priceTop.setTextValue("今日最高", stockViewModel.maxPrice);
            priceBottom.setTextValue("今日最低", stockViewModel.minPrice);
            float currenyPrice = StringUtil.toFloat(stockViewModel.stockPirce);
            if (mCacheBean.forwardPirce == 0) {
                upAndDown.setTextValue("今年涨幅", "暂无");
            } else {
                float v = (currenyPrice - mCacheBean.forwardPirce) / mCacheBean.forwardPirce  * 100;
                upAndDown.setTextValue("今年涨幅", StockUtil.roundedFor(v, 2) + "%");
            }

            rate.setTextValue("换手率", stockViewModel.turnover + "%");
            turnover.setTextValue("成交值", StockUtil.getDealValue(stockViewModel.dealValue));
            marketvalue.setTextValue("市值", StockUtil.getIntegerValue(stockViewModel.valueAll) + "亿");
        }
        stockAdd.setOnClickListener(mListener.addClickListener);
        int pixelFromDip = DeviceUtil.getPixelFromDip(mContext, 15);
        if (mCacheBean.isAdd) {
            stockAdd.setText("删除");
            stockAdd.setCompoundDrawable(mContext.getResources().getDrawable(R.drawable.stock_history_item_delete), 1, pixelFromDip, pixelFromDip);
        } else {
            stockAdd.setText("添加");
            stockAdd.setCompoundDrawable(mContext.getResources().getDrawable(R.drawable.stock_history_item_add), 1, pixelFromDip, pixelFromDip);
        }
        stockAdd.setOnClickListener(mListener.addClickListener);
    }
}
