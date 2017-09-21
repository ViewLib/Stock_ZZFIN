package com.xt.lxl.stock.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xt.lxl.stock.R;
import com.xt.lxl.stock.util.DeviceUtil;
import com.xt.lxl.stock.util.StockShowUtil;

/**
 * Created by xiangleiliu on 2017/8/24.
 */
public class StockBannerView extends RelativeLayout {

    Context mContext;
    ImageView mBannerIcon;
    TextView mBannerText;
    ImageView mBannerArrow;


    public StockBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StockBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StockBannerView);
        String text = a.getString(R.styleable.StockBannerView_stock_banner_text);
        Drawable icon = a.getDrawable(R.styleable.StockBannerView_stock_banner_icon);
        boolean hasArrow = a.getBoolean(R.styleable.StockBannerView_stock_banner_hasArrow, true);
        int textStyle = a.getResourceId(R.styleable.StockBannerView_stock_banner_icon, R.style.text_14_000000);
        a.recycle();
        inflate(context, R.layout.stock_view_banner, this);
        int padding = DeviceUtil.getPixelFromDip(mContext, 15);
        setPadding(padding, 0, padding, 0);
        initView();
        bindText(text, textStyle);
        bindIcon(icon);
        mBannerArrow.setVisibility(hasArrow ? View.VISIBLE : View.GONE);
        initListener();
    }

    private void initListener() {

    }

    private void initView() {
        mBannerIcon = (ImageView) findViewById(R.id.stock_setting_icon);
        mBannerText = (TextView) findViewById(R.id.stock_setting_text);
        mBannerArrow = (ImageView) findViewById(R.id.stock_setting_arrow);
    }

    public void bindText(String title, int style) {
        mBannerText.setText(title);
        mBannerText.setTextAppearance(mContext, style);
    }

    public void bindIcon(Drawable icon) {
        mBannerIcon.setBackground(icon);
    }
}
