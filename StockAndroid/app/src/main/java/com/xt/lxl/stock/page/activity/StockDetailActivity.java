package com.xt.lxl.stock.page.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.config.StockConfig;
import com.xt.lxl.stock.listener.StockDetailListener;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.page.module.StockDetailChartModule;
import com.xt.lxl.stock.page.module.StockDetailCompareModule;
import com.xt.lxl.stock.page.module.StockDetailDescModule;
import com.xt.lxl.stock.page.module.StockDetailFinanceModule;
import com.xt.lxl.stock.page.module.StockDetailGradeModule;
import com.xt.lxl.stock.page.module.StockDetailImportEventModule;
import com.xt.lxl.stock.page.module.StockDetailInfoModule;
import com.xt.lxl.stock.page.module.StockDetailNewsModule;
import com.xt.lxl.stock.sender.StockSender;
import com.xt.lxl.stock.widget.view.StockTitleView;

import java.util.List;

/**
 * 股票详情页
 * Created by xiangleiliu on 2017/8/24.
 */
public class StockDetailActivity extends FragmentActivity {

    public static final String STOCK_DETAIL = "STOCK_DETAIL";
    Handler mHandler = new Handler();

    StockTitleView titleView;
    StockDetailInfoModule infoModule;
    StockDetailChartModule chartModule;
    StockDetailImportEventModule importEventModule;
    StockDetailNewsModule newsModule;
    StockDetailDescModule descModule;
    StockDetailFinanceModule financeModule;
    StockDetailCompareModule compareModule;
    StockDetailGradeModule gradeModule;

    StockDetailListener listener = new StockDetailListener();
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

        infoModule = new StockDetailInfoModule(mStockViewModel);
        infoModule.setModuleView(findViewById(R.id.stock_detail_info));

        chartModule = new StockDetailChartModule(mStockViewModel);
        chartModule.setModuleView(findViewById(R.id.stock_kline));

        importEventModule = new StockDetailImportEventModule(mStockViewModel);
        importEventModule.setModuleView(findViewById(R.id.stock_detail_event));

        newsModule = new StockDetailNewsModule(mStockViewModel);
        newsModule.setModuleView(findViewById(R.id.stock_detail_news));


        descModule = new StockDetailDescModule(mStockViewModel);
        descModule.setModuleView(findViewById(R.id.stock_detail_desc));


        financeModule = new StockDetailFinanceModule(mStockViewModel);
        financeModule.setModuleView(findViewById(R.id.stock_detail_finance));

        compareModule = new StockDetailCompareModule(mStockViewModel);
        compareModule.setModuleView(findViewById(R.id.stock_detail_compare));

        gradeModule = new StockDetailGradeModule(mStockViewModel);
        gradeModule.setModuleView(findViewById(R.id.stock_detail_grade));
    }

    private void bindData() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(mStockViewModel.stockName + "\n");
        builder.setSpan(new TextAppearanceSpan(this, R.style.text_15_ffffff), 0, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int length = mStockViewModel.stockName.length();
        builder.append(mStockViewModel.stockCode);
        builder.setSpan(new TextAppearanceSpan(this, R.style.text_12_ffffff), length, builder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        titleView.setTitle(builder);
        sendStockDataService();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sendStockDataService();
        }
    };

    public void sendStockDataService() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<StockViewModel> stockViewModelList = StockSender.getInstance().requestStockModelByCode(mStockViewModel.stockCode);
                if (stockViewModelList.size() == 0) {
                    return;
                }
                final StockViewModel stockViewModel = stockViewModelList.get(0);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshData(stockViewModel);
                        mHandler.postDelayed(runnable, StockConfig.INTERVAL_TIME);
                    }
                });
            }
        }).start();
    }

    public void refreshData(StockViewModel stockViewModel) {
        infoModule.bindData(stockViewModel);
        chartModule.bindData(stockViewModel);
        importEventModule.bindData(stockViewModel);
        importEventModule.bindData(stockViewModel);
        importEventModule.bindData(stockViewModel);
        newsModule.bindData(stockViewModel);
        descModule.bindData(stockViewModel);
        financeModule.bindData(stockViewModel);
        compareModule.bindData(stockViewModel);
        gradeModule.bindData(stockViewModel);

    }

    private void initListener() {
        listener.addClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        };

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(runnable);
    }
}
