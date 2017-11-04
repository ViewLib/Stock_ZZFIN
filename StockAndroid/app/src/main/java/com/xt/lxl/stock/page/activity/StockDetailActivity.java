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
import com.xt.lxl.stock.model.reponse.StockDetailCompanyInfoResponse;
import com.xt.lxl.stock.model.reponse.StockDetailFinanceResponse;
import com.xt.lxl.stock.model.reponse.StockDetailGradeResponse;
import com.xt.lxl.stock.model.reponse.StockEventsDataResponse;
import com.xt.lxl.stock.page.module.StockDetailChartModule;
import com.xt.lxl.stock.page.module.StockDetailCompareModule;
import com.xt.lxl.stock.page.module.StockDetailDescModule;
import com.xt.lxl.stock.page.module.StockDetailFinanceModule;
import com.xt.lxl.stock.page.module.StockDetailGradeModule;
import com.xt.lxl.stock.page.module.StockDetailImportEventModule;
import com.xt.lxl.stock.page.module.StockDetailInfoModule;
import com.xt.lxl.stock.page.module.StockDetailNewsModule;
import com.xt.lxl.stock.sender.StockSender;
import com.xt.lxl.stock.util.DataSource;
import com.xt.lxl.stock.viewmodel.StockDetailCacheBean;
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
    StockDetailCacheBean mCacheBean = new StockDetailCacheBean();

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
        StockViewModel stockViewModel = (StockViewModel) getIntent().getExtras().getSerializable(StockDetailActivity.STOCK_DETAIL);
        mCacheBean.mStockViewModel = stockViewModel;
        List<String> saveStockCodeList = DataSource.getSaveStockCodeList(this);
        mCacheBean.isAdd = saveStockCodeList.contains(mCacheBean.mStockViewModel.stockCode);
    }

    private void initView() {
        titleView = (StockTitleView) findViewById(R.id.stock_title_view);

        infoModule = new StockDetailInfoModule(mCacheBean);
        infoModule.setModuleView(findViewById(R.id.stock_detail_home_info));

        chartModule = new StockDetailChartModule(mCacheBean);
        chartModule.setModuleView(findViewById(R.id.stock_kline));

        importEventModule = new StockDetailImportEventModule(mCacheBean);
        importEventModule.setModuleView(findViewById(R.id.stock_detail_home_event));

        newsModule = new StockDetailNewsModule(mCacheBean);
        newsModule.setModuleView(findViewById(R.id.stock_detail_home_news));


        descModule = new StockDetailDescModule(mCacheBean);
        descModule.setModuleView(findViewById(R.id.stock_detail_home_desc));


        financeModule = new StockDetailFinanceModule(mCacheBean);
        financeModule.setModuleView(findViewById(R.id.stock_detail_home_finance));

        compareModule = new StockDetailCompareModule(mCacheBean);
        compareModule.setModuleView(findViewById(R.id.stock_detail_home_compare));

        gradeModule = new StockDetailGradeModule(mCacheBean);
        gradeModule.setModuleView(findViewById(R.id.stock_detail_home_grade));
    }

    private void bindData() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(mCacheBean.mStockViewModel.stockName + "\n");
        builder.setSpan(new TextAppearanceSpan(this, R.style.text_15_ffffff), 0, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int length = mCacheBean.mStockViewModel.stockName.length();
        builder.append(mCacheBean.mStockViewModel.stockCode);
        builder.setSpan(new TextAppearanceSpan(this, R.style.text_12_ffffff), length, builder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        titleView.setTitle(builder);
        sendStockDataService();
        sendStockCompanyService();
        sendStockGradeService();
        sendFinanceService();
        sendImportantEvent();
        sendNewsEvent();
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
                List<StockViewModel> stockViewModelList = StockSender.getInstance().requestStockModelByCode(mCacheBean.mStockViewModel.stockCode);
                if (stockViewModelList.size() == 0) {
                    return;
                }
                final StockViewModel stockViewModel = stockViewModelList.get(0);//刷新股票
                mCacheBean.mStockViewModel.stockName = stockViewModel.stockName;
                mCacheBean.mStockViewModel.stockPirce = stockViewModel.stockPirce;
                mCacheBean.mStockViewModel.stockChangeValue = stockViewModel.stockChangeValue;
                mCacheBean.mStockViewModel.stockChange = stockViewModel.stockChange;
                mCacheBean.mStockViewModel.ratio = stockViewModel.ratio;
                mCacheBean.mStockViewModel.turnover = stockViewModel.turnover;
                mCacheBean.mStockViewModel.valueAll = stockViewModel.valueAll;
                mCacheBean.mStockViewModel.maxPrice = stockViewModel.maxPrice;
                mCacheBean.mStockViewModel.minPrice = stockViewModel.minPrice;
                mCacheBean.mStockViewModel.amplitude = stockViewModel.amplitude;
                mCacheBean.mStockViewModel.volume = stockViewModel.volume;
                mCacheBean.mStockViewModel.isSuspension = stockViewModel.isSuspension;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        infoModule.bindData();
                        chartModule.bindData();
                        mHandler.postDelayed(runnable, StockConfig.INTERVAL_TIME);
                    }
                });
            }
        }).start();
    }

    private void sendStockCompanyService() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StockDetailCompanyInfoResponse stockDetailCompanyInfoResponse = StockSender.getInstance().requestStockCompanyService(mCacheBean.mStockViewModel.getRequestStockCode());
                if (stockDetailCompanyInfoResponse.stockHolderList.size() != 0) {
                    mCacheBean.stockHolderList = stockDetailCompanyInfoResponse.stockHolderList;
                }
                mCacheBean.stockDetailCompanyModel = stockDetailCompanyInfoResponse.companyModel;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        descModule.bindData();
                    }
                });
            }
        }).start();
    }

    private void sendFinanceService() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StockDetailFinanceResponse financeResponse = StockSender.getInstance().requestFinanceService("300170.SZ");
//                StockDetailFinanceResponse financeResponse = StockSender.getInstance().requestFinanceService(mCacheBean.mStockViewModel.getRequestStockCode());
                if (financeResponse.resultCode == 200) {
                    mCacheBean.financeResponse = financeResponse;
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        financeModule.bindData();
                    }
                });
            }
        }).start();
    }

    private void sendStockGradeService() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StockDetailGradeResponse stockDetailGradeResponse = StockSender.getInstance().requestStockGradeService(mCacheBean.mStockViewModel.getRequestStockCode());
                if (stockDetailGradeResponse.gradleModelList.size() > 0) {
                    mCacheBean.gradleModelList = stockDetailGradeResponse.gradleModelList;
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        gradeModule.bindData();
                    }
                });
            }
        }).start();
    }

    private void sendImportantEvent() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StockEventsDataResponse eventsDataResponse = StockSender.getInstance().requestStockEventImportant(mCacheBean.mStockViewModel.getRequestStockCode(), 2);
                if (eventsDataResponse.resultCode != 200) {
                    return;
                }
                mCacheBean.eventsDataResponse = eventsDataResponse;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        importEventModule.bindData();
                    }
                });
            }
        }).start();
    }

    private void sendNewsEvent() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCacheBean.newsResponse = DataSource.getNewsResponse();

                        newsModule.bindData();
                    }
                });
            }
        }).start();
    }

    public void refreshAllData(StockDetailCacheBean cacheBean) {
        infoModule.bindData();
        chartModule.bindData();
        importEventModule.bindData();
        newsModule.bindData();
        descModule.bindData();
        financeModule.bindData();
        compareModule.bindData();
        gradeModule.bindData();
    }

    private void initListener() {
        listener.addClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //添加操作
                mCacheBean.isAdd = false;
                DataSource.addStockCode(StockDetailActivity.this, mCacheBean.mStockViewModel.stockCode);
                infoModule.bindData();
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(runnable);
    }
}
