package com.xt.lxl.stock.page.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.widget.view.StockDetailShowText;
import com.xt.lxl.stock.widget.view.StockTitleView;

/**
 * 股票详情页
 * Created by xiangleiliu on 2017/8/24.
 */
public class StockDetailActivity extends FragmentActivity {

    public static final String STOCK_DETAIL = "STOCK_DETAIL";

    StockTitleView titleView;
    StockDetailShowText priceTop;
    StockDetailShowText priceBottom;
    StockDetailShowText upAndDown;
    StockDetailShowText rate;
    StockDetailShowText turnover;
    StockDetailShowText marketvalue;

    StockViewModel mStockViewModel = new StockViewModel();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_detail_layout);
        initData();
        initView();
        bindData();
        initListener();
    }


    private void initData() {
        mStockViewModel = (StockViewModel) getIntent().getExtras().getSerializable(StockDetailActivity.STOCK_DETAIL);
    }

    private void initView() {
        titleView = (StockTitleView) findViewById(R.id.stock_title_view);
        priceTop = (StockDetailShowText) findViewById(R.id.stock_detail_price_top);
        priceBottom = (StockDetailShowText) findViewById(R.id.stock_detail_price_bottom);
        upAndDown = (StockDetailShowText) findViewById(R.id.stock_detail_upanddown);
        rate = (StockDetailShowText) findViewById(R.id.stock_detail_rate);
        turnover = (StockDetailShowText) findViewById(R.id.stock_detail_turnover);
        marketvalue = (StockDetailShowText) findViewById(R.id.stock_detail_marketvalue);

    }

    private void bindData() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(mStockViewModel.stockName);
        builder.setSpan(new RelativeSizeSpan(16), 0, builder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        int length = mStockViewModel.stockName.length();
        builder.append(mStockViewModel.stockCode);
        builder.setSpan(new RelativeSizeSpan(14), length, builder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        titleView.setTitle(builder);

        priceTop.setTextValue("今日最高", "52");
        priceBottom.setTextValue("今日最低", "50");
        upAndDown.setTextValue("今日振幅", "6%");
        rate.setTextValue("换手率", "10%");
        turnover.setTextValue("成交量", "5.2亿");
        marketvalue.setTextValue("市值", "52亿");
    }

    private void initListener() {

    }

}
