package com.xt.lxl.stock.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.xt.lxl.stock.config.StockConfig;
import com.xt.lxl.stock.model.model.StockFoundRankModel;
import com.xt.lxl.stock.model.model.StockRankFilterModel;
import com.xt.lxl.stock.model.model.StockRankResultModel;
import com.xt.lxl.stock.model.model.StockSearchModel;
import com.xt.lxl.stock.model.model.StockViewModel;
import com.xt.lxl.stock.model.reponse.StockHotSearchResponse;
import com.xt.lxl.stock.model.reponse.StockRankDetailFilterlResponse;
import com.xt.lxl.stock.model.reponse.StockRankDetailResponse;
import com.xt.lxl.stock.page.activity.StockSearchActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/8/6.
 */
public class DataSource {

    public static void initStockList(List<StockViewModel> mStockList) {
        StockViewModel viewModel1 = new StockViewModel();
        StockViewModel viewModel2 = new StockViewModel();
        mStockList.add(viewModel1);
        mStockList.add(viewModel2);

        viewModel1.mStockName = "汉得信息";
        viewModel1.mStockPirce = "10.26";
        viewModel1.mStockCode = "300170";
        viewModel1.mStockChangeD = -0.0182;

        viewModel2.mStockName = "朗新科技";
        viewModel2.mStockPirce = "10.55";
        viewModel2.mStockCode = "300682";
        viewModel2.mStockChangeD = 0.1001;

        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
        mStockList.add((StockViewModel) viewModel1.clone());
    }

    public static List<String> getSaveStockCodeList(Context context) {
        SharedPreferences codeList = context.getSharedPreferences(StockConfig.STOCK_SAVE_DB_NAME, 0);
        String codeListStr = codeList.getString(StockConfig.STOCK_SAVE_DATA_NAME, "");
        if (StringUtil.emptyOrNull(codeListStr)) {
            return new ArrayList<>();
        }
        if (codeListStr.endsWith(",")) {
            String substring = codeListStr.substring(0, codeListStr.length() - 1);
            codeListStr = substring;
        }
        String[] split = codeListStr.split(",");
        return Arrays.asList(split);
    }

    public static boolean addStockCode(Context context, String code) {
        List<String> list = new ArrayList<>();
        list.addAll(getSaveStockCodeList(context));
        if (list.contains(code)) {
            return false;
        }
        list.add(code);
        StringBuilder builder = new StringBuilder();
        for (String str : list) {
            builder.append(str);
            builder.append(",");
        }
        SharedPreferences codeList = context.getSharedPreferences(StockConfig.STOCK_SAVE_DB_NAME, 0);
        return codeList.edit().putString(StockConfig.STOCK_SAVE_DATA_NAME, builder.toString()).commit();
    }

    public static List<String> getHistoryStockCodeList(Context context) {
        SharedPreferences codeList = context.getSharedPreferences(StockConfig.STOCK_SAVE_DB_NAME, 0);
        String codeListStr = codeList.getString(StockConfig.STOCK_SAVE_DATA_HISTORY, "");
        if (StringUtil.emptyOrNull(codeListStr)) {
            return new ArrayList<>();
        }
        if (codeListStr.endsWith(",")) {
            String substring = codeListStr.substring(0, codeListStr.length() - 1);
            codeListStr = substring;
        }
        String[] split = codeListStr.split(",");
        return Arrays.asList(split);
    }

    public static boolean addHistrySearch(Context context, String searchKey) {
        List<String> list = new ArrayList<>();
        list.addAll(getHistoryStockCodeList(context));
        SharedPreferences codeList = context.getSharedPreferences(StockConfig.STOCK_SAVE_DB_NAME, 0);
        StringBuilder builder = new StringBuilder();
        if (list.size() > 10) {
            list.remove(0);
        }
        list.add(searchKey);
        for (String str : list) {
            builder.append(str);
            builder.append(",");
        }
        return codeList.edit().putString(StockConfig.STOCK_SAVE_DATA_HISTORY, builder.toString()).commit();
    }

    /**
     * 获取所有本地存储的股票信息
     *
     * @return
     */
    public static List<StockViewModel> getSearchAllData() {
        List<StockViewModel> list = new ArrayList<>();
        list.add(new StockViewModel("300170", "汉得信息"));
        list.add(new StockViewModel("300171", "东富龙"));
        list.add(new StockViewModel("300172", "中电环保"));
        list.add(new StockViewModel("300173", "智慧松德"));
        list.add(new StockViewModel("300174", "原理股份"));
        list.add(new StockViewModel("600174", "桂东电力"));
        return list;
    }


    public static List<StockFoundRankModel> getRankList(Context context) {
        List<StockFoundRankModel> list = new ArrayList<>();
        list.add(new StockFoundRankModel("本日融资融券的前十家公司"));
        list.add(new StockFoundRankModel("社保公司重仓流通股排行（持股数量变化）"));
        list.add(new StockFoundRankModel("本月限售解禁前十名"));
        list.add(new StockFoundRankModel("央企净资产从小到大排行"));
        list.add(new StockFoundRankModel("放量大涨前十名"));
        list.add(new StockFoundRankModel("沪港通活跃程序-买入"));
        list.add(new StockFoundRankModel("本日融资融券的前十家公司"));
        list.add(new StockFoundRankModel("放量大涨前十名"));
        list.add(new StockFoundRankModel("年度最佳"));
        list.add(new StockFoundRankModel("本日融资融券的前十家公司"));
        return list;
    }

    public static StockHotSearchResponse getStockHotSearchResponse(StockSearchActivity stockSearchActivity) {
        List<StockSearchModel> list = new ArrayList<>();
        StockSearchModel model1 = new StockSearchModel();
        model1.mSearchType = StockSearchModel.STOCK_FOUND_TYPE_RNAK;
        model1.rankModel = new StockFoundRankModel("事件一一一一一一一啊");

        StockSearchModel model2 = new StockSearchModel();
        model2.mSearchType = StockSearchModel.STOCK_FOUND_TYPE_RNAK;
        model2.rankModel = new StockFoundRankModel("事件二喽喽");

        StockSearchModel model3 = new StockSearchModel();
        model3.stockViewModel = new StockViewModel();
        model3.mSearchType = StockSearchModel.STOCK_FOUND_TYPE_STOCK;
        model3.stockViewModel.mStockName = "汉得信息";
        model3.stockViewModel.mStockCode = "300170";

        StockSearchModel model4 = new StockSearchModel();
        model4.mSearchType = StockSearchModel.STOCK_FOUND_TYPE_RNAK;
        model4.rankModel = new StockFoundRankModel("事件四");

        list.add(model1);
        list.add(model2);
        list.add(model3);
        list.add(model4);

        StockHotSearchResponse response = new StockHotSearchResponse();
        response.resultCode = 200;
        response.resultMessage = "success";
        response.mHotSearchList = list;
        return response;
    }

    public static StockRankDetailFilterlResponse getRankDetailFilterResponse() {
        StockRankDetailFilterlResponse response = new StockRankDetailFilterlResponse();
        StockRankFilterModel model1 = new StockRankFilterModel();
        model1.mDefaultPosition = 0;
        model1.mFilteList.add("央企");
        model1.mFilteList.add("私企");
        model1.mFilteList.add("欧美");

        StockRankFilterModel model2 = new StockRankFilterModel();
        model2.mDefaultPosition = 0;
        model2.mFilteList.add("银行业");
        model2.mFilteList.add("建筑业");
        model2.mFilteList.add("计算机");
        model2.mFilteList.add("金融");

        StockRankFilterModel model3 = new StockRankFilterModel();
        model3.mDefaultPosition = 0;
        model3.mFilteList.add("全部盘");
        model3.mFilteList.add("流通盘");
        model3.mFilteList.add("解禁盘");
        model3.mFilteList.add("市值");

        StockRankFilterModel model4 = new StockRankFilterModel();
        model4.mDefaultPosition = 0;
        model4.mFilteList.add("其它筛选");
        model4.mFilteList.add("高新科技");
        model4.mFilteList.add("雄安新区");
        model4.mFilteList.add("评级较高");

        response.mRankFilterList.add(model1);
        response.mRankFilterList.add(model2);
        response.mRankFilterList.add(model3);
        response.mRankFilterList.add(model4);
        return response;
    }

    public static StockRankDetailResponse getRankDetailResponse() {
        StockRankDetailResponse response = new StockRankDetailResponse();
        response.mTitle = "本日融资融券的前十家公司";

        StockRankResultModel model0 = new StockRankResultModel();
        model0.mPosition = 0;
        model0.mStockName = "名称";
//        model0.mStockCode = "加入自选";
        model0.mAttr1 = "连续跌停天数";
        model0.mAttr2 = "今年表现";
        model0.mAttr3 = "今天涨跌";

        StockRankResultModel model1 = new StockRankResultModel();
        model1.mPosition = 1;
        model1.mStockName = "中国银行";
        model1.mStockCode = "601988";
        model1.mAttr1 = "7";
        model1.mAttr2 = "-36%";
        model1.mAttr3 = "-7%";

        StockRankResultModel model2 = new StockRankResultModel();
        model2.mPosition = 2;
        model2.mStockName = "农业银行";
        model2.mStockCode = "601288";
        model2.mAttr1 = "5";
        model2.mAttr2 = "-30%";
        model2.mAttr3 = "-5%";

        response.mRankResultList.add(model0);
        response.mRankResultList.add(model1);
        response.mRankResultList.add(model2);
        return response;
    }
}
