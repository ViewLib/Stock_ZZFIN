package com.xt.lxl.stock.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xt.lxl.stock.R;

/**
 * Created by Administrator on 2017/10/10 0010.
 */

public class StockDetailShowText extends LinearLayout {

    TextView tv1;
    TextView tv2;

    int mTitleWidth = 0;
    boolean canMulti = false;

    public StockDetailShowText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StockDetailShowText);
        mTitleWidth = a.getDimensionPixelSize(R.styleable.StockDetailShowText_stock_title_width, 60);
        canMulti = a.getBoolean(R.styleable.StockDetailShowText_stock_detail_show_canMulti, false);
        a.recycle();

        LayoutInflater.from(context).inflate(R.layout.stock_detail_info_tv_view, this);
        initView();
    }

    private void initView() {
        tv1 = (TextView) findViewById(R.id.stock_detail_tv1);
        tv2 = (TextView) findViewById(R.id.stock_detail_tv2);
        tv2.setSingleLine(!canMulti);
        tv1.getLayoutParams().width = mTitleWidth;
    }

    public void setTextValue(String text1, String text2) {
        tv1.setText(text1);
        tv2.setText(text2);
    }


}
