package com.xt.lxl.stock.model;

/**
 * Created by xiangleiliu on 2017/9/10.
 * 排行model
 */
public class StockFoundRankModel {
    public static final int SHOW_TYPE_DEFAULT = 0;
    public static final int SHOW_TYPE_BULE = 1;
    public static final int SHOW_TYPE_YELLOW = 2;


    public String mTitle;//title
    public int mShowType;//展示的类型，左上角的选择类型
    public int mType;//跳转类型，是那种类型的排行

    public StockFoundRankModel(String mTitle) {
        this(mTitle, SHOW_TYPE_BULE);
    }


    public StockFoundRankModel(String title, int showType) {
        mTitle = title;
        mShowType = showType;
    }


}
