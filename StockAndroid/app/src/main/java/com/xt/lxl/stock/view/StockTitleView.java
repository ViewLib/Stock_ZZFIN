package com.xt.lxl.stock.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xt.lxl.stock.R;

/**
 * Created by xiangleiliu on 2017/8/24.
 */
public class StockTitleView extends RelativeLayout {

    Context mContext;
    TextView mTitle;
    ImageView mBackBtn;
    ImageView mActionBtn;


    public StockTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StockTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StockTitleView);
        String title = a.getString(R.styleable.StockTitleView_stock_title_text_msg);
        int titleStyle = a.getResourceId(R.styleable.StockTitleView_stock_title_text_style, R.style.text_18_62696f);
        a.recycle();
        inflate(context, R.layout.stock_base_title, this);
        initView();
        setTitle(title, titleStyle);
        initListener();
    }

    private void initListener() {
        mBackBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!(mContext instanceof Activity)) {
                    return;
                }
                ((Activity) mContext).finish();
            }
        });

    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title_view);
        mBackBtn = (ImageView) findViewById(R.id.back_btn);
        mActionBtn = (ImageView) findViewById(R.id.action_btn);
    }

    public void setTitle(String title, int style) {
        mTitle.setText(title);
        mTitle.setTextAppearance(mContext, style);
    }


}
