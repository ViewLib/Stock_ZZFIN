package com.xt.lxl.stock.page.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/10/28.
 */
public class StockViewPagerAdapter extends PagerAdapter {
    private List<View> mViewList = new ArrayList<>();

    public StockViewPagerAdapter(List<View> list) {
        super();
        this.mViewList = list;
    }

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

}
