package com.xt.lxl.stock.page.module;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.model.model.StockDetailCompanyModel;
import com.xt.lxl.stock.model.model.StockDetailStockHolder;
import com.xt.lxl.stock.page.adapter.StockViewPagerAdapter;
import com.xt.lxl.stock.viewmodel.StockDetailCacheBean;
import com.xt.lxl.stock.widget.view.StockDetailShowText;
import com.xt.lxl.stock.widget.view.StockTabGroupButton;
import com.xt.lxl.stock.widget.view.StockTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/19 0019.
 * 公司介绍
 */

public class StockDetailDescModule extends StockDetailBaseModule {

    private StockTextView mTitle;
    private StockTabGroupButton mTab;//查看更多
    private ViewPager mPager;//
    private List<View> mViewList = new ArrayList<>();
    private StockViewPagerAdapter pagerAdapter;

    public StockDetailDescModule(StockDetailCacheBean cacheBean) {
        super(cacheBean);
    }

    @Override
    public void initModuleView(View view) {
        mTitle = (StockTextView) view.findViewById(R.id.stock_detail_desc_title);
        mTab = (StockTabGroupButton) view.findViewById(R.id.stock_detail_desc_tab);
        mPager = (ViewPager) view.findViewById(R.id.stock_detail_desc_container);
        List<String> list = new ArrayList<>();
        list.add("公司简介");
        list.add("十大股东");
        mTab.setTabItemArrayText(list);
        mTab.initView();
    }

    @Override
    public void bindData() {
        createViewList();
        //拼接List<View>
        pagerAdapter = new StockViewPagerAdapter(mViewList);
        mPager.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();
        mTab.setOnTabItemSelectedListener(new StockTabGroupButton.OnTabItemSelectedListener() {
            @Override
            public void onTabItemClicked(int whichButton) {
                mPager.setCurrentItem(whichButton);
            }
        });
    }

    private void createViewList() {
        View infoView;
        View rankView;
        if (mViewList.size() != 2) {
            mViewList.clear();
            infoView = View.inflate(mContainer.getContext(), R.layout.stock_detail_desc_company_info, null);
            mViewList.add(0, infoView);

            rankView = View.inflate(mContainer.getContext(), R.layout.stock_detail_desc_company_rank, null);
            mViewList.add(1, rankView);
        } else {
            infoView = mViewList.get(0);
            rankView = mViewList.get(1);
        }

        StockDetailCompanyModel stockDetailCompanyModel = mCacheBean.stockDetailCompanyModel;
        List<StockDetailStockHolder> stockHolderList = mCacheBean.stockHolderList;
        handleCompanyInfo(infoView, stockDetailCompanyModel);
        handleStockHolder(rankView, stockHolderList);

    }

    //公司信息
    private void handleCompanyInfo(View view, StockDetailCompanyModel stockDetailCompanyModel) {
        StockDetailShowText companyName = (StockDetailShowText) view.findViewById(R.id.stock_detail_company_name);
        StockDetailShowText companyCreateDate = (StockDetailShowText) view.findViewById(R.id.stock_detail_company_createdate);
        StockDetailShowText companyArea = (StockDetailShowText) view.findViewById(R.id.stock_detail_company_area);
        StockDetailShowText companyBusiness = (StockDetailShowText) view.findViewById(R.id.stock_detail_company_business);
        companyName.setTextValue("公司名称", stockDetailCompanyModel.companyName);
        companyCreateDate.setTextValue("成立时间", stockDetailCompanyModel.establishDate);
        companyArea.setTextValue("总部位置", stockDetailCompanyModel.baseArea);
        companyBusiness.setTextValue("主营业务", stockDetailCompanyModel.companyBusiness);
    }

    //十大股东
    private void handleStockHolder(View view, List<StockDetailStockHolder> stockHolderList) {
        TableLayout stockHolderTable = (TableLayout) view.findViewById(R.id.stock_detail_desc_company_table);
        TableRow row = setRowData(true, "股东名称", "持股数量", "持股比例");
        row.setBackgroundColor(Color.parseColor("#DEEBF6"));

        stockHolderTable.addView(row);
        for (int i = 0; i < stockHolderList.size(); i++) {
            StockDetailStockHolder holder = stockHolderList.get(i);
            row = setRowData(false, holder.stockHolderNmae, holder.stockHolderAmount, holder.stockHolderRatio);
            stockHolderTable.addView(row);
            if (i % 2 == 0) {
                row.setBackgroundColor(Color.parseColor("#FFFFFF"));
            } else {
                row.setBackgroundColor(Color.parseColor("#EDF3F8"));
            }
        }
    }

    private TableRow setRowData(boolean isTitle, String stockHolderName, String stockHolderAmount, String stockHolderRatio) {
        TableRow row = (TableRow) View.inflate(mContainer.getContext(), R.layout.stock_detail_desc_company_table_item, null);

        TextView stockHolderNameTv = (TextView) row.findViewById(R.id.stock_holder_name);
        stockHolderNameTv.setText(stockHolderName);

        TextView stockHolderAmountTv = (TextView) row.findViewById(R.id.stock_holder_have);
        stockHolderAmountTv.setText(stockHolderAmount);

        TextView stockHolderRatioTv = (TextView) row.findViewById(R.id.stock_holder_ratio);
        stockHolderRatioTv.setText(stockHolderRatio);

        if (isTitle) {
            stockHolderNameTv.setTextAppearance(row.getContext(), R.style.text_13_186db7);
            stockHolderAmountTv.setTextAppearance(row.getContext(), R.style.text_13_186db7);
            stockHolderRatioTv.setTextAppearance(row.getContext(), R.style.text_13_186db7);
        } else {
            stockHolderNameTv.setTextAppearance(row.getContext(), R.style.text_12_484848);
            stockHolderAmountTv.setTextAppearance(row.getContext(), R.style.text_12_484848);
            stockHolderRatioTv.setTextAppearance(row.getContext(), R.style.text_12_484848);
        }
        return row;
    }

}
