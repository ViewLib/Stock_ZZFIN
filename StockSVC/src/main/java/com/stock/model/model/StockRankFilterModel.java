package com.stock.model.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19 0019.
 * 每一列的筛选model，
 */

public class StockRankFilterModel {
    public int defaultPosition;//默认筛选位置
    public String filterTitle = "";
    public int filter_type = 0;
    public int parent_group_id = 0;
    public List<StockFilterViewModel> stockFilterViewModel = new ArrayList<>();//筛选列表，比如央企/私企/外企/等等
}
