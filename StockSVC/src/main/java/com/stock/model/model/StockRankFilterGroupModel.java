package com.stock.model.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19 0019.
 * 每一列的筛选model，
 * 例如：
 * 企业性质、地区、行业、其他
 */

public class StockRankFilterGroupModel extends StockBaseModel {
    public static final int SHOW_TYPE_GROUP = 1;//父级节点
    public static final int SHOW_TYPE_TILE = 2;//选择类，例如城市、行业选择
    public static final int SHOW_TYPE_NODE = 3;//节点选择

    public int groupId;//筛选ID
    public String groupName = "";//筛选名称
    public int level;//层级
    public int parentGroupId;//筛选ID
    public int showType = SHOW_TYPE_GROUP;
    public List<StockRankFilterItemModel> filteList = new ArrayList<>();//筛选列表，比如央企/私企/外企/等等
    public List<StockRankFilterGroupModel> filterGroupList = new ArrayList<>();//界面筛选项列表

    public StockRankFilterGroupModel() {

    }

    public StockRankFilterGroupModel(String groupName) {
        this.groupName = groupName;
    }
}
