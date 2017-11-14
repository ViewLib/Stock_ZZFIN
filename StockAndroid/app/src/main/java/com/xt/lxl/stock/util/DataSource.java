package com.xt.lxl.stock.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.xt.lxl.stock.config.StockConfig;
import com.xt.lxl.stock.model.model.StockDateDataModel;
import com.xt.lxl.stock.model.model.StockEventsDataList;
import com.xt.lxl.stock.model.model.StockEventsDataModel;
import com.xt.lxl.stock.model.model.StockRankFilterGroupModel;
import com.xt.lxl.stock.model.model.StockRankResultModel;
import com.xt.lxl.stock.model.model.StockSyncModel;
import com.xt.lxl.stock.model.reponse.StockEventsDataResponse;
import com.xt.lxl.stock.model.reponse.StockRankDetailFilterlResponse;
import com.xt.lxl.stock.model.reponse.StockRankDetailResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xiangleiliu on 2017/8/6.
 */
public class DataSource {


//    public static StockGetMinuteDataResponse getMinuteDataResponses() {
//        StockGetMinuteDataResponse response = new StockGetMinuteDataResponse();
//        response.stockCode = "300170";
//        response.stockName = "汉得信息";
//        Calendar instance = Calendar.getInstance();
//        instance.set(Calendar.YEAR, 2017);
//        instance.set(Calendar.DAY_OF_MONTH, 15);
//        instance.set(Calendar.HOUR_OF_DAY, 8);
//        instance.set(Calendar.MINUTE, 29);
//        List<StockMinuteDataModel> minuteDataList = response.stockMinuteDataModels;
//        for (int i = 0; i < 100; i++) {
//            int v = (int) (Math.random() * 10 - 5) * 10 + 1107;
//            Log.i("lxltest", "v:" + v);
//            StockMinuteDataModel stockMinuteData = new StockMinuteDataModel(DateUtil.calendar2Time(instance.getTimeInMillis(), DateUtil.SIMPLEFORMATTYPESTRING12), v, 10, 1290);
//            minuteDataList.add(stockMinuteData);
//            instance.add(Calendar.MINUTE, 1);
//        }
//        return response;
//    }

//    public static StockGetDateDataResponse getDataResponses(String type) {
//        StockGetDateDataResponse response = new StockGetDateDataResponse();
//        response.stockCode = "300170";
//        response.stockName = "汉得信息";
//        response.stockKDataType = type;
//        Calendar instance = Calendar.getInstance();
//        instance.set(Calendar.YEAR, 2017);
//        instance.set(Calendar.MONTH, 0);
//        instance.set(Calendar.DAY_OF_MONTH, 0);
//        List<StockDateDataModel> dateDataList = response.dateDataList;
//        for (int i = 0; i < 100; i++) {
//            int basePrice = (int) (Math.random() * 10 - 5) * 10 + 1290;
//            int maxprice = basePrice + 30;
//            int minPrice = basePrice - 30;
//            int openPrice = basePrice + (int) (Math.random() * 10 - 5) * 5;
//            int closePrice = basePrice + (int) (Math.random() * 10 - 5) * 5;
//
//            String dateStr = DateUtil.calendar2Time(instance, DateUtil.SIMPLEFORMATTYPESTRING7);
//            StockDateDataModel stockDateData = new StockDateDataModel(dateStr, maxprice, minPrice, openPrice, closePrice, (int) (100000 * Math.random()));
//            dateDataList.add(stockDateData);
//            instance.add(Calendar.DAY_OF_MONTH, 1);
//        }
//        return response;
//    }

    public static List<String> getDefaultStockCodeList() {
        List<String> list = new ArrayList<>();
        list.add("sh000001");
        list.add("sz399001");
        list.add("sz399006");
        return list;
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

    public static boolean deleteStockCode(Context context, String code) {
        if (StringUtil.emptyOrNull(code)) {
            return false;
        }
        List<String> list = getSaveStockCodeList(context);
        ArrayList<String> codeList = new ArrayList<>();
        codeList.addAll(list);
        Iterator<String> iterator = codeList.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (code.equals(next)) {
                iterator.remove();
                break;
            }
        }
        StringBuilder builder = new StringBuilder();
        for (String str : codeList) {
            builder.append(str);
            builder.append(",");
        }
        SharedPreferences codeListSP = context.getSharedPreferences(StockConfig.STOCK_SAVE_DB_NAME, 0);
        return codeListSP.edit().putString(StockConfig.STOCK_SAVE_DATA_NAME, builder.toString()).commit();
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

//    public static List<StockFoundRankModel> getRankList(Context context) {
//        List<StockFoundRankModel> list = new ArrayList<>();
//        list.add(new StockFoundRankModel("本日融资融券的前十家公司"));
//        list.add(new StockFoundRankModel("社保公司重仓流通股排行（持股数量变化）"));
//        list.add(new StockFoundRankModel("本月限售解禁前十名"));
//        list.add(new StockFoundRankModel("央企净资产从小到大排行"));
//        list.add(new StockFoundRankModel("放量大涨前十名"));
//        list.add(new StockFoundRankModel("沪港通活跃程序-买入"));
//        list.add(new StockFoundRankModel("本日融资融券的前十家公司"));
//        list.add(new StockFoundRankModel("放量大涨前十名"));
//        list.add(new StockFoundRankModel("年度最佳"));
//        list.add(new StockFoundRankModel("本日融资融券的前十家公司"));
//        return list;
//    }

//    public static StockHotSearchResponse getStockHotSearchResponse(StockSearchActivity stockSearchActivity) {
//        List<StockSearchModel> list = new ArrayList<>();
//        StockSearchModel model1 = new StockSearchModel();
//        model1.searchType = StockSearchModel.STOCK_FOUND_TYPE_RNAK;
//        model1.rankModel = new StockFoundRankModel("事件一一一一一一一啊");
//
//        StockSearchModel model2 = new StockSearchModel();
//        model2.searchType = StockSearchModel.STOCK_FOUND_TYPE_RNAK;
//        model2.rankModel = new StockFoundRankModel("事件二喽喽");
//
//        StockSearchModel model3 = new StockSearchModel();
//        model3.stockViewModel = new StockViewModel();
//        model3.searchType = StockSearchModel.STOCK_FOUND_TYPE_STOCK;
//        model3.stockViewModel.stockName = "汉得信息";
//        model3.stockViewModel.stockCode = "300170";
//
//        StockSearchModel model4 = new StockSearchModel();
//        model4.searchType = StockSearchModel.STOCK_FOUND_TYPE_RNAK;
//        model4.rankModel = new StockFoundRankModel("事件四");
//
//        list.add(model1);
//        list.add(model2);
//        list.add(model3);
//        list.add(model4);
//
//        StockHotSearchResponse response = new StockHotSearchResponse();
//        response.resultCode = 200;
//        response.resultMessage = "success";
//        response.hotSearchList = list;
//        return response;
//    }

    public static StockRankDetailFilterlResponse getRankDetailFilterResponse() {
        StockRankDetailFilterlResponse response = new StockRankDetailFilterlResponse();
        StockRankFilterGroupModel model1 = new StockRankFilterGroupModel("行业");
//        model1.filteList.add(new StockRankFilterItemModel("银行业", "11", 1));
//        model1.filteList.add(new StockRankFilterItemModel("建筑业", "12", 1));
//        model1.filteList.add(new StockRankFilterItemModel("计算机", "13", 1));
//        model1.filteList.add(new StockRankFilterItemModel("金融", "14", 1));
//
//        StockRankFilterGroupModel model2 = new StockRankFilterGroupModel("企业性质");
//        model2.filteList.add(new StockRankFilterItemModel("央企", "11", 1));
//        model2.filteList.add(new StockRankFilterItemModel("私企", "12", 1));
//        model2.filteList.add(new StockRankFilterItemModel("欧美", "13", 1));
//
//
//        StockRankFilterGroupModel model3 = new StockRankFilterGroupModel("地区");
//        model1.filteList.add(new StockRankFilterItemModel("山东", "11", 1));
//        model1.filteList.add(new StockRankFilterItemModel("上海", "12", 1));
//        model1.filteList.add(new StockRankFilterItemModel("福建", "13", 1));
//        model1.filteList.add(new StockRankFilterItemModel("北京", "14", 1));
//        model1.filteList.add(new StockRankFilterItemModel("浙江", "14", 1));
//
//
//        StockRankFilterGroupModel model4 = new StockRankFilterGroupModel("其它筛选");
//        StockRankFilterGroupModel sub1 = new StockRankFilterGroupModel("流通盘");
//        sub1.filteList.add(new StockRankFilterItemModel("30亿以下", "411", 1));
//        sub1.filteList.add(new StockRankFilterItemModel("30到100亿", "412", 1));
//        sub1.filteList.add(new StockRankFilterItemModel("100到500亿", "413", 1));
//        sub1.filteList.add(new StockRankFilterItemModel("500亿以上", "414", 1));
//
//        StockRankFilterGroupModel sub2 = new StockRankFilterGroupModel("市值");
//        sub2.filteList.add(new StockRankFilterItemModel("30亿以下", "411", 1));
//        sub2.filteList.add(new StockRankFilterItemModel("30到100亿", "412", 1));
//        sub2.filteList.add(new StockRankFilterItemModel("100到500亿", "413", 1));
//        sub2.filteList.add(new StockRankFilterItemModel("500亿以上", "414", 1));
//
//        StockRankFilterGroupModel sub3 = new StockRankFilterGroupModel("市盈率");
//        sub3.filteList.add(new StockRankFilterItemModel("30亿以下", "411", 1));
//        sub3.filteList.add(new StockRankFilterItemModel("30到100亿", "412", 1));
//        sub3.filteList.add(new StockRankFilterItemModel("100到500亿", "413", 1));
//        sub3.filteList.add(new StockRankFilterItemModel("500亿以上", "414", 1));
//
//        StockRankFilterGroupModel sub4 = new StockRankFilterGroupModel("市净率");
//        sub4.filteList.add(new StockRankFilterItemModel("30亿以下", "411", 1));
//        sub4.filteList.add(new StockRankFilterItemModel("30到100亿", "412", 1));
//        sub4.filteList.add(new StockRankFilterItemModel("100到500亿", "413", 1));
//        sub4.filteList.add(new StockRankFilterItemModel("500亿以上", "414", 1));

//        model4.filterGroupList.add(sub1);
//        model4.filterGroupList.add(sub2);
//        model4.filterGroupList.add(sub3);
//        model4.filterGroupList.add(sub4);
//
//        response.rankFilterList.add(model1);
//        response.rankFilterList.add(model2);
//        response.rankFilterList.add(model3);
//        response.rankFilterList.add(model4);
        return response;
    }

    public static StockRankDetailResponse getRankDetailResponse() {
        StockRankDetailResponse response = new StockRankDetailResponse();
        response.title = "本日融资融券的前十家公司";

        StockRankResultModel model0 = new StockRankResultModel();
        model0.position = 0;
        model0.stockName = "名称";
//        model0.stockCode = "加入自选";
        model0.attr1 = "连续跌停天数";
        model0.attr2 = "今年表现";
        model0.attr3 = "今天涨跌";

        StockRankResultModel model1 = new StockRankResultModel();
        model1.position = 1;
        model1.stockName = "中国银行";
        model1.stockCode = "601988";
        model1.attr1 = "7";
        model1.attr2 = "-36%";
        model1.attr3 = "-7%";

        StockRankResultModel model2 = new StockRankResultModel();
        model2.position = 2;
        model2.stockName = "农业银行";
        model2.stockCode = "601288";
        model2.attr1 = "5";
        model2.attr2 = "-30%";
        model2.attr3 = "-5%";

        StockRankResultModel model3 = new StockRankResultModel();
        model3.position = 3;
        model3.stockName = "汉得信息";
        model3.stockCode = "300170";
        model3.attr1 = "5";
        model3.attr2 = "-30%";
        model3.attr3 = "-5%";

        response.rankResultList.add(model0);
        response.rankResultList.add(model1);
        response.rankResultList.add(model2);
        response.rankResultList.add(model3);

        return response;
    }

    public static List<StockDateDataModel> getDayDataPriceList() {
        List<StockDateDataModel> list = new ArrayList<>();

        float maxPrice = 11.00f;//当前股票最高价格，单位：分
        float minPrice = 10.00f;//当前股票最低价格，单位：分
        float openPrice = 10.40f;//当前股票最低价格，单位：分
        float closePrice = 10.60f;//当前股票最低价格，单位：分

        Calendar instance = Calendar.getInstance();
        for (int i = 0; i < 300; i++) {
            if (instance.get(Calendar.DAY_OF_WEEK) == 7 || instance.get(Calendar.DAY_OF_WEEK) == 1) {//周六或者周天跳过
                instance.add(Calendar.DAY_OF_MONTH, -1);
                continue;
            }
            StockDateDataModel model = new StockDateDataModel();
            String s = DateUtil.calendar2String(instance, DateUtil.SIMPLEFORMATTYPESTRING7);

            model.date = s;
            model.high = maxPrice;
            model.low = minPrice;
            model.open = openPrice;
            model.close = closePrice;
            model.volume = (float) (Math.random() * 1000);
            list.add(model);

            //add value
            instance.add(Calendar.DAY_OF_MONTH, -1);
            maxPrice += 1;
            minPrice += 1;
            openPrice += 1;
            closePrice += 1;
        }

        List<StockDateDataModel> list2 = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            list2.add(list.get(i));
        }
        return list2;
    }

    public static StockEventsDataResponse getNewsResponse() {
        StockEventsDataResponse response = new StockEventsDataResponse();
        StockEventsDataList dataList1 = new StockEventsDataList();
        dataList1.eventType = StockEventsDataList.TYPE_LIFTED;
        dataList1.eventName = "解禁事件";

        StockEventsDataModel eventModel2 = new StockEventsDataModel();
        eventModel2.eventDate = "2017-10-18";
        eventModel2.eventTitle = "范建震解禁1851万";
        eventModel2.eventDesc = "大股东减持30万股，平均每股38元";

        StockEventsDataModel eventModel3 = new StockEventsDataModel();
        eventModel3.eventDate = "2017-10-19";
        eventModel3.eventTitle = "范建震解禁1851万";
        eventModel3.eventDesc = "大股东减持30万股，平均每股38元";


        StockEventsDataModel eventModel4 = new StockEventsDataModel();
        eventModel4.eventDate = "2017-10-20";
        eventModel4.eventTitle = "范建震解禁1851万";
        eventModel4.eventDesc = "大股东减持30万股，平均每股38元";


        StockEventsDataModel eventModel5 = new StockEventsDataModel();
        eventModel5.eventDate = "2017-10-23";
        eventModel5.eventTitle = "范建震解禁1851万";
        eventModel5.eventDesc = "大股东减持30万股，平均每股38元";


        StockEventsDataModel eventModel6 = new StockEventsDataModel();
        eventModel6.eventDate = "2017-10-24";
        eventModel6.eventTitle = "范建震解禁1851万";
        eventModel6.eventDesc = "大股东减持30万股，平均每股38元";

        StockEventsDataModel eventModel7 = new StockEventsDataModel();
        eventModel7.eventDate = "2017-10-31";
        eventModel7.eventTitle = "范建震解禁1851万";
        eventModel7.eventDesc = "大股东减持30万股，平均每股38元";


        StockEventsDataModel eventModel8 = new StockEventsDataModel();
        eventModel8.eventDate = "2017-11-01";
        eventModel8.eventTitle = "范建震解禁1851万";
        eventModel8.eventDesc = "大股东减持30万股，平均每股38元";

        StockEventsDataModel eventModel9 = new StockEventsDataModel();
        eventModel8.eventDate = "2017-11-02";
        eventModel8.eventTitle = "范建震解禁1851万";
        eventModel8.eventDesc = "大股东减持30万股，平均每股38元";

        dataList1.stockEventsDataModels.add(eventModel2);
        dataList1.stockEventsDataModels.add(eventModel3);
        dataList1.stockEventsDataModels.add(eventModel4);
        dataList1.stockEventsDataModels.add(eventModel5);
        dataList1.stockEventsDataModels.add(eventModel6);
        dataList1.stockEventsDataModels.add(eventModel7);
        dataList1.stockEventsDataModels.add(eventModel8);
        dataList1.stockEventsDataModels.add(eventModel9);


        StockEventsDataList dataList2 = new StockEventsDataList();
        dataList2.eventType = StockEventsDataList.TYPE_PLEDGE;
        dataList2.eventName = "股票质押";

        StockEventsDataModel eventModel21 = new StockEventsDataModel();
        eventModel21.eventDate = "2017-10-19";
        eventModel21.eventTitle = "范建震质押1851万";
        eventModel21.eventDesc = "大股东质押30万股";

        dataList2.stockEventsDataModels.add(eventModel21);

        response.moduleName = "重大消息";
        response.stockEventsDataLists.add(dataList1);
        response.stockEventsDataLists.add(dataList2);
        return response;
    }

    /**
     * 获取所有本地存储的股票信息
     *
     * @return
     */
    public static List<StockSyncModel> getSearchAllData() {
        List<StockSyncModel> list = new ArrayList<>();
        list.addAll(StockData.getSearchAllDataBy000());
        list.addAll(StockData.getSearchAllDataBy002());
        list.addAll(StockData.getSearchAllDataBy300());
        list.addAll(StockData.getSearchAllDataBy600());
        return list;
    }
}
