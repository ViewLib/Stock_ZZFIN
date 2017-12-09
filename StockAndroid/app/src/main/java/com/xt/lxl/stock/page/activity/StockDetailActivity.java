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
import com.xt.lxl.stock.model.ServiceResponse;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.model.reponse.StockDetailCompanyInfoResponse;
import com.xt.lxl.stock.model.reponse.StockDetailCompareResponse;
import com.xt.lxl.stock.model.reponse.StockDetailFinanceResponse;
import com.xt.lxl.stock.model.reponse.StockDetailGradeResponse;
import com.xt.lxl.stock.model.reponse.StockEventsDataResponse;
import com.xt.lxl.stock.model.reponse.StockGetDateDataResponse;
import com.xt.lxl.stock.model.reponse.StockSyncResponse;
import com.xt.lxl.stock.page.chartfragment.StockDayChartFragment;
import com.xt.lxl.stock.page.module.StockDetailChartModule;
import com.xt.lxl.stock.page.module.StockDetailCompareModule;
import com.xt.lxl.stock.page.module.StockDetailDescModule;
import com.xt.lxl.stock.page.module.StockDetailFinanceModule;
import com.xt.lxl.stock.page.module.StockDetailGradeModule;
import com.xt.lxl.stock.page.module.StockDetailImportEventModule;
import com.xt.lxl.stock.page.module.StockDetailInfoModule;
import com.xt.lxl.stock.page.module.StockDetailNewsModule;
import com.xt.lxl.stock.sender.StockSender;
import com.xt.lxl.stock.sender.StockSenderCache;
import com.xt.lxl.stock.util.DataSource;
import com.xt.lxl.stock.viewmodel.StockDetailCacheBean;
import com.xt.lxl.stock.widget.stockchart.bean.DayViewModel;
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

        infoModule = new StockDetailInfoModule(mCacheBean, listener);
        infoModule.setModuleView(findViewById(R.id.stock_detail_home_info));

        chartModule = new StockDetailChartModule(mCacheBean, listener);
        chartModule.setModuleView(findViewById(R.id.stock_kline));

        importEventModule = new StockDetailImportEventModule(mCacheBean, listener);
        importEventModule.setModuleView(findViewById(R.id.stock_detail_home_event));

        newsModule = new StockDetailNewsModule(mCacheBean, listener);
        newsModule.setModuleView(findViewById(R.id.stock_detail_home_news));


        descModule = new StockDetailDescModule(mCacheBean, listener);
        descModule.setModuleView(findViewById(R.id.stock_detail_home_desc));


        financeModule = new StockDetailFinanceModule(mCacheBean, listener);
        financeModule.setModuleView(findViewById(R.id.stock_detail_home_finance));

        compareModule = new StockDetailCompareModule(mCacheBean, listener);
        compareModule.setModuleView(findViewById(R.id.stock_detail_home_compare));

        gradeModule = new StockDetailGradeModule(mCacheBean, listener);
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
        if (mCacheBean.mStockViewModel.isDelisting) {
            infoModule.bindData();
            return;
        }
        hanldeSendService();
    }

    private void hanldeSendService() {
        sendStockDataService();
        sendImportantEvent();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendStockCompanyService();
                sendStockGradeService();
                sendFinanceService();
                sendCompareService();
                handleNewModule();
            }
        }, 3000);
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
                if (mCacheBean.forwardPirce == 0) {
                    float v = StockSender.getInstance().requestForwadPrice(mCacheBean.mStockViewModel.stockCode);
                    mCacheBean.forwardPirce = v;
                }

                final StockViewModel stockViewModel = stockViewModelList.get(0);//刷新股票
                mCacheBean.mStockViewModel = stockViewModel;
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
                StockDetailCompanyInfoResponse stockDetailCompanyInfoResponse = StockSenderCache.getCache().getStockResponseModel(mCacheBean.mStockViewModel.stockCode, StockDetailCompanyInfoResponse.class);
                if (stockDetailCompanyInfoResponse == null) {
                    stockDetailCompanyInfoResponse = StockSender.getInstance().requestStockCompanyService(mCacheBean.mStockViewModel.getRequestStockCode());
                    if (stockDetailCompanyInfoResponse == null || stockDetailCompanyInfoResponse.stockHolderList.size() == 0) {
                        return;
                    }
                    StockSenderCache.getCache().putStockResponseModel(mCacheBean.mStockViewModel.stockCode, stockDetailCompanyInfoResponse);
                }
                mCacheBean.stockHolderList = stockDetailCompanyInfoResponse.stockHolderList;
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

                StockDetailFinanceResponse financeResponse = StockSenderCache.getCache().getStockResponseModel(mCacheBean.mStockViewModel.stockCode, StockDetailFinanceResponse.class);
                if (financeResponse == null) {
                    financeResponse = StockSender.getInstance().requestFinanceService(mCacheBean.mStockViewModel.getRequestStockCode());
                    if (financeResponse == null || financeResponse.resultCode != 200) {
                        return;
                    }
                    StockSenderCache.getCache().putStockResponseModel(mCacheBean.mStockViewModel.stockCode, financeResponse);
                }
                mCacheBean.financeResponse = financeResponse;
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
                StockDetailGradeResponse stockDetailGradeResponse = StockSenderCache.getCache().getStockResponseModel(mCacheBean.mStockViewModel.stockCode, StockDetailGradeResponse.class);
                if (stockDetailGradeResponse == null) {
                    stockDetailGradeResponse = StockSender.getInstance().requestStockGradeService(mCacheBean.mStockViewModel.getRequestStockCode());
                    if (stockDetailGradeResponse.gradleModelList.size() == 0) {
                        return;
                    }
                    StockSenderCache.getCache().putStockResponseModel(mCacheBean.mStockViewModel.stockCode, stockDetailGradeResponse);
                }
                mCacheBean.gradleModelList = stockDetailGradeResponse.gradleModelList;
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
                int type = 2;
                StockEventsDataResponse eventsDataResponse = StockSenderCache.getCache().getStockResponseModel(mCacheBean.mStockViewModel.stockCode, type, StockEventsDataResponse.class);
                if (eventsDataResponse == null) {
                    eventsDataResponse = StockSender.getInstance().requestStockEventImportant(mCacheBean.mStockViewModel.getRequestStockCode(), type);
                    if (eventsDataResponse.resultCode != 200) {
                        return;
                    }
                    StockSenderCache.getCache().putStockResponseModel(mCacheBean.mStockViewModel.stockCode, type, eventsDataResponse);
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


    private void handleNewModule() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StockGetDateDataResponse dataResponses = StockSender.getInstance().requestDateData(mCacheBean.mStockViewModel.getRequestStockCode(), StockGetDateDataResponse.TYPE_DAY);
                final DayViewModel dayViewModel = StockDayChartFragment.calculationData(dataResponses);
                int type = 3;
                StockEventsDataResponse eventsDataResponse = StockSenderCache.getCache().getStockResponseModel(mCacheBean.mStockViewModel.stockCode, type, StockEventsDataResponse.class);
                if (eventsDataResponse == null) {
                    eventsDataResponse = StockSender.getInstance().requestStockEventImportant(mCacheBean.mStockViewModel.getRequestStockCode(), type);
                    if (eventsDataResponse.resultCode != 200) {
                        return;
                    }
                    StockSenderCache.getCache().putStockResponseModel(mCacheBean.mStockViewModel.stockCode, type, eventsDataResponse);
                }
                mCacheBean.dayViewModel = dayViewModel;
                mCacheBean.newsResponse = eventsDataResponse;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        newsModule.bindData();
                    }
                });
            }
        }).start();
    }

    private void sendCompareService() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                StockDetailCompareResponse compareResponse = StockSenderCache.getCache().getStockResponseModel(mCacheBean.mStockViewModel.stockCode, StockDetailCompareResponse.class);
                if (compareResponse == null) {
                    compareResponse = StockSender.getInstance().requestStockCompare(mCacheBean.mStockViewModel.getRequestStockCode());
                    if (compareResponse.resultCode != 200) {
                        return;
                    }
                    if (compareResponse.compareList.size() == 0) {
                        return;
                    }
                    StockSenderCache.getCache().putStockResponseModel(mCacheBean.mStockViewModel.stockCode, compareResponse);
                }
                mCacheBean.compareResponse = compareResponse;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        compareModule.bindData();
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
                if (mCacheBean.isAdd) {
                    mCacheBean.isAdd = false;
                    DataSource.deleteStockCode(StockDetailActivity.this, mCacheBean.mStockViewModel.stockCode);
                } else {
                    mCacheBean.isAdd = true;
                    DataSource.addStockCode(StockDetailActivity.this, mCacheBean.mStockViewModel.stockCode);
                }
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
