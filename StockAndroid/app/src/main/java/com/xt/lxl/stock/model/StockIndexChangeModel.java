package com.xt.lxl.stock.model;

/**
 * Created by xiangleiliu on 2017/8/27.
 * 指数变化model
 */
public class StockIndexChangeModel {

    public static final int SHOW_LOCATION_LEFT = -1;
    public static final int SHOW_LOCATION_CENTER = 0;
    public static final int SHOW_LOCATION_RIGHT = 1;


    public String mShowText;//当前展示的指数
    public double mShowIndex;//指数展示的百分比，范围0到1,-1到0
    public String mBgColor;//背景颜色
    public String mTextColor;//字体颜色
    public int mTextSize;//字体大小
    public int mShowLocation;//展示的位置，是在框体的左边，中间，还是右边
}
