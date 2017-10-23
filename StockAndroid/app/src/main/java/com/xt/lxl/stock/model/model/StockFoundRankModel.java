package com.xt.lxl.stock.model.model;

/**
 * Created by xiangleiliu on 2017/9/10.
 * 排行model，top10的排行
 */
public class StockFoundRankModel extends StockBaseModel {
    public static final int SHOW_TYPE_DEFAULT = 0;
    public static final int SHOW_TYPE_BULE = 1;
    public static final int SHOW_TYPE_YELLOW = 2;


    public String title;//title
    public int showType;//展示的类型，左上角的选择类型
    public int searchRelation;//跳转类型，是那种类型的排行

    public StockFoundRankModel() {

    }

    public StockFoundRankModel(String mTitle) {
        this(mTitle, SHOW_TYPE_BULE);
    }


    public StockFoundRankModel(String title, int showType) {
        this.title = title;
        this.showType = showType;
    }


}
