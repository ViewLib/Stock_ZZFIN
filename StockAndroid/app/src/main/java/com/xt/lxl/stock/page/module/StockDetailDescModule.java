package com.xt.lxl.stock.page.module;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.model.model.StockDetailCompanyModel;
import com.xt.lxl.stock.model.model.StockDetailStockHolder;
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
        StockDetailCompanyModel stockDetailCompanyModel = mCacheBean.stockDetailCompanyModel;
        List<StockDetailStockHolder> stockHolderList = mCacheBean.stockHolderList;

        View infoView = View.inflate(mContainer.getContext(), R.layout.stock_detail_desc_company_info, null);
        View rankView = View.inflate(mContainer.getContext(), R.layout.stock_detail_desc_company_rank, null);

        handleCompanyInfo(infoView, stockDetailCompanyModel);
        handleStockHolder(rankView, stockHolderList);

        mViewList.add(infoView);
        mViewList.add(rankView);
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
        TableRow row = new TableRow(view.getContext());
        setRowData(row, true, "排序", "股东名称", "持股数量", "持股比例");
        stockHolderTable.addView(row);
        for (int i = 0; i < stockHolderList.size(); i++) {
            StockDetailStockHolder holder = stockHolderList.get(i);
            row = new TableRow(view.getContext());
            setRowData(row, true, String.valueOf(i), holder.stockHolderName, holder.stockHolderAmount, holder.stockHolderRatio);
            stockHolderTable.addView(row);
        }
    }

    private void setRowData(TableRow row, boolean isTitle, String position, String stockHolderName, String stockHolderAmount, String stockHolderRatio) {
        TextView posionTv = new TextView(row.getContext());
        posionTv.setText(position);

        TextView stockHolderNameTv = new TextView(row.getContext());
        stockHolderNameTv.setText(stockHolderName);

        TextView stockHolderAmountTv = new TextView(row.getContext());
        stockHolderAmountTv.setText(stockHolderAmount);

        TextView stockHolderRatioTv = new TextView(row.getContext());
        stockHolderRatioTv.setText(stockHolderRatio);
    }


    PagerAdapter pagerAdapter = new PagerAdapter() {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mViewList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            // TODO Auto-generated method stub
            container.removeView(mViewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            container.addView(mViewList.get(position));
            return mViewList.get(position);
        }
    };


}
