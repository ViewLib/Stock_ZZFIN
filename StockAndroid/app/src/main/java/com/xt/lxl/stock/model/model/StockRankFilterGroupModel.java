package com.xt.lxl.stock.model.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19 0019.
 * 每一列的筛选model，
 * 例如：
 * 企业性质、地区、行业、其他
 */

public class StockRankFilterGroupModel extends StockBaseModel{
    public static final int SHOW_TYPE_TILE = 1;//平铺类型
    public static final int SHOW_TYPE_GROUP = 2;//节点选择

    public int showType;
    public List<StockRankFilterItemModel> filteList = new ArrayList<>();//筛选列表，比如央企/私企/外企/等等
    public List<StockRankFilterGroupModel> filterGroupList = new ArrayList<>();//界面筛选项列表
}
