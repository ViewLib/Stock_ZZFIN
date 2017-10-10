package com.xt.lxl.stock.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/10/10 0010.
 * K线图
 */

public class StockKLineView extends ViewGroup {
    TextView text;

    public StockKLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        text = new TextView(context);
        text.setText("k线图模块");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
