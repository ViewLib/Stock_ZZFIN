package com.xt.lxl.stock.widget.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xt.lxl.stock.R;

/**
 * Created by Administrator on 2017/10/10 0010.
 */

public class StockDetailShowText extends LinearLayout {

    TextView tv1;
    TextView tv2;

    public StockDetailShowText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.stock_detail_info_tv_view, this);
        initView();
    }

    private void initView() {
        tv1 = (TextView) findViewById(R.id.stock_detail_tv1);
        tv2 = (TextView) findViewById(R.id.stock_detail_tv2);
    }

    public void setTextValue(String text1, String text2) {
        tv1.setText(text1);
        tv2.setText(text2);
    }


}
